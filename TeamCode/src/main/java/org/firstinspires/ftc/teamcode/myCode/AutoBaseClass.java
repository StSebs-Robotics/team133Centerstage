package org.firstinspires.ftc.teamcode.myCode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.MarkerCallback;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

public abstract class AutoBaseClass extends LinearOpMode {
    //region Servo Variables Definitions
    protected Servo intakeRotate;
    protected Servo dump;
    protected Servo plane;
    protected Servo planeLock;
    protected Servo planeRotate;
    protected CRServo dumpLocker;
    //endregion

    //region Motors Variables Definitions
    protected DcMotor leftSlide;
    protected DcMotor rightSlide;
    protected DcMotor intake;
    //endregion

    //region Camera Variables Definitions
    protected TfodProcessor tfod;
    protected String whereIsTeamElement = "left";
    protected static final boolean USE_WEBCAM = true;
    protected static final String TFOD_MODEL_ASSET = "teamElement.tflite";
    protected static final String[] LABELS = {"blue", "red"};
    protected VisionPortal visionPortal;
    //endregion

    //region Trajectories
    protected int multipler = 1;
    protected MarkerCallback intakeUp;
    protected MarkerCallback slideUp;
    protected Pose2d intPose;
    protected Pose2d dropOffPose;
    protected double angleAdjust = 0;
    
    protected SampleMecanumDrive drive;
    protected TrajectorySequence trajectoryCloseBeam;
    protected TrajectorySequence trajectoryMiddleBeam;
    protected TrajectorySequence trajectoryFarBeam;
    protected TrajectorySequence trajectoryGoToPile;
    protected TrajectorySequence trajectoryDropOffPixels;

    protected TrajectorySequence trajectoryback;

    //endregion
    protected void getHardware() {
        //region Servo Hardware
        intakeRotate = hardwareMap.servo.get("intakerotate");
        dump = hardwareMap.servo.get("dump");
        dumpLocker = hardwareMap.get(CRServo.class, "controt");
        plane = hardwareMap.servo.get("planelock");
        planeRotate = hardwareMap.servo.get("planerotate");
        planeLock = hardwareMap.servo.get("plane");
        //endregion

        //region Motor Hardware
        rightSlide = hardwareMap.get(DcMotor.class, "rightslide");
        leftSlide = hardwareMap.get(DcMotor.class, "leftslide");
        intake = hardwareMap.get(DcMotor.class, "intake");

        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        //endregion

        //region Camera Setup
        initTfod();
        //endregion
    }

    //creates camera recognition
    protected void initTfod() {
        tfod = new TfodProcessor.Builder().setModelAssetName(TFOD_MODEL_ASSET)
                .setModelLabels(LABELS)
                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();
    }

    protected void readTeamElement() {

        List<Recognition> currentRecognitions = tfod.getRecognitions();
        telemetry.addData("# Objects Detected", currentRecognitions.size());

        // Step through the list of recognitions and display info for each one.
        double xT = -100;
        for (Recognition recognition : currentRecognitions) {
            double x = (recognition.getLeft() + recognition.getRight()) / 2;
            double y = (recognition.getTop() + recognition.getBottom()) / 2;
            xT = x;
            telemetry.addData("", " ");
            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
            telemetry.addData("- Position", "%.0f / %.0f", x, y);
            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
        }
        returnLocation(xT);
    }

    protected void returnLocation(double position) {
        if (position > 300) {
            whereIsTeamElement = "close";
            telemetry.addData("WE ARE Close to the beams", " ");
        } else if (position >= 0) {
            whereIsTeamElement = "middle";
            telemetry.addData("WE ARE AT THE Middle", " ");
        } else {
            whereIsTeamElement = "far";
            telemetry.addData("WE ARE Far from the beams I HOPE BECAUSE", "THIS IS A GUESS BECAUSE WE CAN'T SEE IT");
        }
    }

    @Override
    public void runOpMode() {
        intakeUp = ()->{
            intakeRotate.setPosition(1);
        };
        slideUp = ()->{
            moveSlides(1400,1);
            dump.setPosition(1);
        };
        getHardware();

        setupTrajectories();
        drive.setPoseEstimate(intPose);
        sleep(6000);
        telemetry.addData("PosX",intPose.getX());
        telemetry.addData("PosY",intPose.getY());
        telemetry.addData("you good fam","");
        telemetry.update();
        waitForStart();
        if (opModeIsActive()) {
            intakeRotate.setPosition(.53);
            readTeamElement();
            sleep(500);
            TrajectorySequence first;
            if (whereIsTeamElement == "close") {
                first = trajectoryCloseBeam;
            } else if (whereIsTeamElement == "middle") {
                first = trajectoryMiddleBeam;
            } else {
                first = trajectoryFarBeam;
            }
            drive.followTrajectorySequence(first);
            dumpLocker.setPower(-.25);
            sleep(800);
            drive.followTrajectory(drive.trajectoryBuilder(first.end()).splineToConstantHeading(dropOffPose.vec(),Math.toRadians(180)).build());
            dumpLocker.setPower(0);
            dump.setPosition(.14);
            sleep(500);
            moveSlides(0, 1);
//            drive.followTrajectorySequence(trajectoryGoToPile);
//            //do pickup stuff
//            intakeRotate.setPosition(.66);
//            sleep(500);
//            intake.setPower(1);
//            dumpLocker.setPower(1);
//            sleep(2000);
//            intake.setPower(0);
//            dumpLocker.setPower(0);
//            drive.followTrajectorySequence(trajectoryDropOffPixels);
        }
    }

    protected void moveSlides(int position, double velocity) {
        leftSlide.setTargetPosition(position);
        rightSlide.setTargetPosition(position);

        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightSlide.setPower(velocity);
        leftSlide.setPower(velocity);
    }

    protected abstract void setupTrajectories();
//    {
//        intPose = new Pose2d(12, 61, Math.toRadians(90));
//        dropOffPose = new Pose2d(12, 61, Math.toRadians(0));
//
//        drive = new SampleMecanumDrive(hardwareMap);
//
//        trajectoryCloseBeam = drive.trajectorySequenceBuilder(intPose)
//                .lineTo(new Vector2d(-38.1, 50 * multipler))
//                .build();
//        trajectoryMiddleBeam = drive.trajectorySequenceBuilder(intPose)
//                .lineTo(new Vector2d(-38.1, 50 * multipler))
//                .build();
//        trajectoryFarBeam = drive.trajectorySequenceBuilder(intPose)
//                .lineTo(new Vector2d(-38.1, 50 * multipler))
//                .build();
//        trajectoryGoToPile = drive.trajectorySequenceBuilder(dropOffPose)
//                .lineTo(new Vector2d(-38.1, 50 * multipler))
//                .build();
//        trajectoryDropOffPixels = drive.trajectorySequenceBuilder(trajectoryGoToPile.end())
//                .lineTo(new Vector2d(-38.1, 50 * multipler))
//                .build();
//    }

}
