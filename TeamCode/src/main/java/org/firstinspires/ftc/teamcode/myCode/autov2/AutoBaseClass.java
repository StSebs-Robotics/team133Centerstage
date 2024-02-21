package org.firstinspires.ftc.teamcode.myCode.autov2;//package org.firstinspires.ftc.teamcode.myCode.autov2;
//
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.acmerobotics.roadrunner.trajectory.MarkerCallback;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.hardware.CRServo;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.Servo;
//
//import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
//import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
//import org.firstinspires.ftc.teamcode.myCode.utilities.Constants;
//import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
//import org.firstinspires.ftc.vision.VisionPortal;
//import org.firstinspires.ftc.vision.tfod.TfodProcessor;
//
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//@Disabled
//public abstract class AutoBaseClass extends LinearOpMode {
//    // region Servo Variables Definitions
//    protected Servo intakeRotate;
//    protected Servo dump;
//    protected Servo plane;
//    protected Servo planeLock;
//    protected Servo planeRotate;
//    private Servo frontClaw;
//    private Servo backClaw;
//    private CRServo intakeStage2;
//    // endregion
//
//    // region Motors Variables Definitions
//    protected DcMotor leftSlide;
//    protected DcMotor rightSlide;
//    protected DcMotor intake;
//    // endregion
//
//    // region Camera Variables Definitions
//    protected TfodProcessor tfod;
//    protected String whereIsTeamElement = "left";
//    protected static final boolean USE_WEBCAM = true;
//    protected static final String TFOD_MODEL_ASSET = "teamElement.tflite";
//    protected static final String[] LABELS = { "blue", "red" };
//    protected VisionPortal visionPortal;
//    // endregion
//
//    // region Trajectories
//    protected int multiplier = 1;
//    protected MarkerCallback intakeUp;
//    protected MarkerCallback slideUp;
//    protected MarkerCallback slideUp2;
//    protected Pose2d intPose;
//    protected Pose2d dropOffPose;
//    protected double angleAdjust = 0;
//
//    protected SampleMecanumDrive drive;
//    protected TrajectorySequence trajectoryCloseBeam;
//    protected TrajectorySequence trajectoryMiddleBeam;
//    protected TrajectorySequence trajectoryFarBeam;
//    protected TrajectorySequence trajectoryGoToPile;
//    protected TrajectorySequence trajectoryDropOffPixels;
//    protected TrajectorySequence trajectoryPark;
//    protected boolean fullAuto = true;
//
//    protected TrajectorySequence trajectoryBack;
//
//    // endregion
//    protected void getHardware() {
//        // region Servo Hardware
//        intakeRotate = hardwareMap.servo.get("intakerotate");
//        dump = hardwareMap.servo.get("dump");
//        backClaw = hardwareMap.servo.get("backclaw");
//        frontClaw = hardwareMap.servo.get("frontclaw");
//        intakeStage2 = hardwareMap.get(CRServo.class, "intakestage2");
//        plane = hardwareMap.servo.get("planeTrigger");
//        planeRotate = hardwareMap.servo.get("Plane tilt");
//        planeLock = hardwareMap.servo.get("claaamp");
//        // endregion
//
//        // region Motor Hardware
//        rightSlide = hardwareMap.get(DcMotor.class, "rightslide");
//        leftSlide = hardwareMap.get(DcMotor.class, "leftslide");
//        intake = hardwareMap.get(DcMotor.class, "intake");
//
//        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//
//        leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
//        // endregion
//
//        // region Camera Setup
//        initTfod();
//        // endregion
//    }
//
//    // creates camera recognition
//    protected void initTfod() {
//        tfod = new TfodProcessor.Builder().setModelAssetName(TFOD_MODEL_ASSET)
//                .setModelLabels(LABELS)
//                .build();
//
//        // Create the vision portal by using a builder.
//        VisionPortal.Builder builder = new VisionPortal.Builder();
//
//        // Set the camera (webcam vs. built-in RC phone camera).
//        if (USE_WEBCAM) {
//            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
//        } else {
//            builder.setCamera(BuiltinCameraDirection.BACK);
//        }
//
//        // Set and enable the processor.
//        builder.addProcessor(tfod);
//
//        // Build the Vision Portal, using the above settings.
//        visionPortal = builder.build();
//    }
//
//    protected abstract void setVariables();
//
//    protected void readTeamElement() {
//
//        List<Recognition> currentRecognitions = tfod.getRecognitions();
//        telemetry.addData("# Objects Detected", currentRecognitions.size());
//
//        // Step through the list of recognitions and display info for each one.
//        double xT = -100;
//        for (Recognition recognition : currentRecognitions) {
//            double x = (recognition.getLeft() + recognition.getRight()) / 2;
//            double y = (recognition.getTop() + recognition.getBottom()) / 2;
//            xT = x;
//            telemetry.addData("", " ");
//            telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
//            telemetry.addData("- Position", "%.0f / %.0f", x, y);
//            telemetry.addData("- Size", "%.0f x %.0f", recognition.getWidth(), recognition.getHeight());
//        }
//        returnLocation(xT);
//    }
//
//    protected void returnLocation(double position) {
//        telemetry.addData("Base Class","");
//        if (position > 300) {
//            whereIsTeamElement = "close";
//            telemetry.addData("WE ARE Close to the beams", " ");
//        } else if (position >= 0) {
//            whereIsTeamElement = "middle";
//            telemetry.addData("WE ARE AT THE Middle", " ");
//        } else {
//            whereIsTeamElement = "far";
//            telemetry.addData("WE ARE Far from the beams I HOPE BECAUSE", "THIS IS A GUESS BECAUSE WE CAN'T SEE IT");
//        }
//        telemetry.update();
//    }
//
//    @Override
//    public void runOpMode() {
//        intakeUp = () -> {
//            intakeRotate.setPosition(Constants.intakeUp);
//        };
//        slideUp = () -> {
//            moveSlides(1100, 1);
//            dump.setPosition(Constants.dumpUp);
//        };
//        slideUp2 = () -> {
//            frontClaw.setPosition(Constants.frontClawClosed);
//            backClaw.setPosition(Constants.backClawClosed);
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    intake.setPower(0);
//                    intakeStage2.setPower(0);
//                    moveSlides(1500, 1);
//                    dump.setPosition(Constants.dumpUp);
//                }
//            }, 200L);
//        };
//        setVariables();
//        getHardware();
//
//        setupTrajectories();
//        drive.setPoseEstimate(intPose);
//        sleep(6000);
//        plane.setPosition(Constants.triggerStore);
//        planeLock.setPosition(Constants.planeLockStore);
//        planeRotate.setPosition(Constants.planeRotateStore);
//        frontClaw.setPosition(Constants.frontClawClosed);
//        backClaw.setPosition(Constants.backClawClosed);
//        telemetry.addData("PosX", intPose.getX());
//        telemetry.addData("PosY", intPose.getY());
//        telemetry.addData("you good fam", "");
//        telemetry.update();
//        waitForStart();
//        if (opModeIsActive()) {
//            intakeRotate.setPosition(Constants.intakePickupStack1);
//            readTeamElement();
//            sleep(500);
//            TrajectorySequence first;
//            if (whereIsTeamElement == "close") {
//                first = trajectoryCloseBeam;
//            } else if (whereIsTeamElement == "middle") {
//                first = trajectoryMiddleBeam;
//            } else {
//                first = trajectoryFarBeam;
//            }
//            drive.followTrajectorySequence(first);
//            frontClaw.setPosition(Constants.frontClawOpen);
//            backClaw.setPosition(Constants.backClawOpen);
//            sleep(100);
//            dump.setPosition(Constants.dumpDown);
//                drive.followTrajectory(drive.trajectoryBuilder(first.end())
//                        .lineToConstantHeading(dropOffPose.vec()).build());
//
//            moveSlides(0, 1);
//            if (fullAuto) {
//                drive.followTrajectorySequence(trajectoryGoToPile);
//                intake.setPower(1);
//                intakeStage2.setPower(-1);
//                intakeRotate.setPosition(Constants.intakePickupStack5);
//                sleep(1000);
//                intakeRotate.setPosition(Constants.intakePickupStack4);
//                sleep(750);
//                intakeRotate.setPosition(Constants.intakeUp);
//                sleep(500);
//                drive.followTrajectorySequence(trajectoryDropOffPixels);
//
//                intake.setPower(0);
//                intakeStage2.setPower(0);
//                frontClaw.setPosition(Constants.frontClawOpen);
//                backClaw.setPosition(Constants.backClawOpen);
//                drive.followTrajectory(drive.trajectoryBuilder(trajectoryDropOffPixels.end()).back(3).build());
//                dump.setPosition(Constants.dumpDown);
//                sleep(200);
//                moveSlides(0, 1);
//            } else {
//                drive.followTrajectorySequence(trajectoryPark);
//            }
//
//            sleep(10000);
//            // drive.followTrajectorySequence(trajectoryGoToPile);
//            // //do pickup stuff
//            // intakeRotate.setPosition(.66);
//            // sleep(500);
//            // intake.setPower(1);
//            // dumpLocker.setPower(1);
//            // sleep(2000);
//            // intake.setPower(0);
//            // dumpLocker.setPower(0);
//            // drive.followTrajectorySequence(trajectoryDropOffPixels);
//        }
//    }
//
//    protected void moveSlides(int position, double velocity) {
//        leftSlide.setTargetPosition(position);
//        rightSlide.setTargetPosition(position);
//
//        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//
//        rightSlide.setPower(velocity);
//        leftSlide.setPower(velocity);
//    }
//
//    protected abstract void setupTrajectories();
//
//}
