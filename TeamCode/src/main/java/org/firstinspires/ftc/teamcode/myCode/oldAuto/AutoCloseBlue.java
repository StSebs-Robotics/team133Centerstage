package org.firstinspires.ftc.teamcode.myCode.oldAuto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;

@Config
@Autonomous(name = "AutoCloseBlue", group = "old Auto")
public class AutoCloseBlue extends LinearOpMode {

    public static double X = 5;
    public static double Y = 5;

    private static final boolean USE_WEBCAM = true;
    private static final String TFOD_MODEL_ASSET = "teamElement.tflite";
    private static final String[] LABELS = {
            //"teamElement",
            "blue", "red",};

    /**
     * The variable to store our instance of the TensorFlow Object Detection processor.
     */
    private TfodProcessor tfod;
    private String whereIsTeamElement = "left";
    private double yMultiplier = 1;
    private Servo intakeRotate;
    private Servo dump;
    private CRServo dumpLocker;

    private DcMotor leftSlide;
    private DcMotor rightSlide;
    private DcMotor intake;

    private Servo plane;
    private Servo planeLock;
    private Servo planeRotate;
    private Servo gateLift;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    private void moveSlides(int distance, double velocity) {
        leftSlide.setTargetPosition(-distance);
        rightSlide.setTargetPosition(distance);

        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightSlide.setPower(velocity);
        leftSlide.setPower(velocity);
    }

    @Override
    public void runOpMode() {
        intakeRotate = hardwareMap.servo.get("intakerotate");
        dump = hardwareMap.servo.get("dump");
        dumpLocker = hardwareMap.get(CRServo.class, "controt");
        rightSlide = hardwareMap.get(DcMotor.class, "rightslide");
        leftSlide = hardwareMap.get(DcMotor.class, "leftslide");
        intake = hardwareMap.get(DcMotor.class, "intake");
        gateLift = hardwareMap.servo.get("gatelift");


        plane = hardwareMap.servo.get("planelock");
        planeRotate = hardwareMap.servo.get("planerotate");
        planeLock = hardwareMap.servo.get("plane");

        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch Plays to start OpMode");
        telemetry.update();
        initTfod();
        sleep(6000);
        telemetry.addData("GOGOGO", "You Good Fam");
        telemetry.update();

        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Pose2d intPose = new Pose2d(12, 61, Math.toRadians(90));
        GetTrag trags = new GetTrag(true, intPose, drive,
                () -> {
                    intakeRotate.setPosition(1);
                },
                () -> {
                    moveSlides(-825, 1);
                    dump.setPosition(1);
                },
                () -> {
                    gateLift.setPosition(.3);
                },
                () -> {
                    dumpLocker.setPower(1);
                    gateLift.setPosition(0);
                    intakeRotate.setPosition(.66);
                    intake.setPower(1);
                },
                () -> {
                    dumpLocker.setPower(0);
                    intakeRotate.setPosition(1);
                    intake.setPower(0);
                });
        dump.setPosition(.14);
        plane.setPosition(0);
        planeLock.setPosition(.4);
        planeRotate.setPosition(.3);


        waitForStart();
        if (opModeIsActive()) {
            intakeRotate.setPosition(.53);
            drive.setPoseEstimate(intPose);
            telemetryTfod();
            sleep(500);
            TrajectorySequence go;
            if (whereIsTeamElement == "middle") {
                go = trags.getCloseMiddle();
            } else if (whereIsTeamElement == "right") {
                go = trags.getCloseRight();
            } else {
                go = trags.getCloseLeft();
            }
            Trajectory back = drive.trajectoryBuilder(go.end()).back(6).build();

            telemetry.update();
            visionPortal.stopStreaming();


            //Follow inital trag
            drive.followTrajectorySequence(go);
            //return to position
            dumpLocker.setPower(-.5);
            sleep(800);
            drive.followTrajectory(back);
            dumpLocker.setPower(0);
            dump.setPosition(.14);
            sleep(500);
            moveSlides(0, 1);
            while (opModeIsActive()) {


                // Share the CPU.
                sleep(20);
            }
        }

        // Save more CPU resources when camera is no longer needed.
        visionPortal.close();

    }   // end runOpMode()

    /**
     * Initialize the TensorFlow Object Detection processor.
     */
    private void initTfod() {

        // Create the TensorFlow processor by using a builder.
        tfod = new TfodProcessor.Builder().setModelAssetName(TFOD_MODEL_ASSET)
                // The following default settings are available to un-comment and edit as needed to
                // set parameters for custom models.
                .setModelLabels(LABELS)
                //.setIsModelTensorFlow2(true)
                //.setIsModelQuantized(true)
                //.setModelInputSize(300)
                //.setModelAspectRatio(16.0 / 9.0)
                .build();

        // Create the vision portal by using a builder.
        VisionPortal.Builder builder = new VisionPortal.Builder();

        // Set the camera (webcam vs. built-in RC phone camera).
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }

        // Choose a camera resolution. Not all cameras support all resolutions.
        //builder.setCameraResolution(new Size(640, 480));

        // Enable the RC preview (LiveView).  Set "false" to omit camera monitoring.
        //builder.enableLiveView(true);

        // Set the stream format; MJPEG uses less bandwidth than default YUY2.
        //builder.setStreamFormat(VisionPortal.StreamFormat.YUY2);

        // Choose whether or not LiveView stops if no processors are enabled.
        // If set "true", monitor shows solid orange screen if no processors enabled.
        // If set "false", monitor shows camera view without annotations.
        //builder.setAutoStopLiveView(false);

        // Set and enable the processor.
        builder.addProcessor(tfod);

        // Build the Vision Portal, using the above settings.
        visionPortal = builder.build();

        // Set confidence threshold for TFOD recognitions, at any time.
        //tfod.setMinResultConfidence(0.75f);

        // Disable or re-enable the TFOD processor at any time.
        //visionPortal.setProcessorEnabled(tfod, true);

    }   // end method initTfod()

    /**
     * Add telemetry about TensorFlow Object Detection (TFOD) recognitions.
     */
    private void telemetryTfod() {

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
        if (xT > 300) {
            whereIsTeamElement = "right";
            telemetry.addData("WE ARE AT THE RIGHT", " ");
        } else if (xT >= 0) {
            whereIsTeamElement = "middle";
            telemetry.addData("WE ARE AT THE Middle", " ");
        } else {
            whereIsTeamElement = "left";
            telemetry.addData("WE ARE AT THE LEFT I HOPE BECAUSE", "THIS IS A GUESS BECAUSE WE CAN'T SEE IT");
        }

    }   // end method telemetryTfod()
}