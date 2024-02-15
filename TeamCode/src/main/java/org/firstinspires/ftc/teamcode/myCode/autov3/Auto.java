package org.firstinspires.ftc.teamcode.myCode.autov3;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.MarkerCallback;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.myCode.utilities.Constants;
import org.firstinspires.ftc.teamcode.myCode.utilities.Positions;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Autonomous(name = "AutoV3", group = "133")
public class Auto extends LinearOpMode {
    // region Servo Variables Definitions
    protected Servo intakeRotate;
    protected Servo dump;
    protected Servo plane;
    protected Servo planeLock;
    protected Servo planeRotate;
    private Servo frontClaw;
    private Servo backClaw;
    private CRServo intakeStage2;
    // endregion

    // region Motors Variables Definitions
    protected DcMotor leftSlide;
    protected DcMotor rightSlide;
    protected DcMotor intake;
    // endregion

    // region Camera Variables Definitions
    protected TfodProcessor tfod;
    protected String whereIsTeamElement = "left";
    protected static final boolean USE_WEBCAM = true;
    protected static final String TFOD_MODEL_ASSET = "teamElement.tflite";
    protected static final String[] LABELS = {"blue", "red"};
    protected VisionPortal visionPortal;
    // endregion

    //region Boolean Variables Definitions

    private boolean doneSetup = false;
    private boolean isBlue = true;
    private boolean isClose = true;
    private boolean parkWall = false;
    private boolean morePixels = true;
    private boolean throughTruss = false;

    private boolean isRed = !isBlue;
    private boolean isFar = !isClose;
    private boolean parkMiddle = !parkWall;
    //endregion

    // region Trajectories
    protected int multiplier = 1;
    protected MarkerCallback intakeUp;
    protected MarkerCallback intake1Pixel;
    protected MarkerCallback intake2Pixel;
    protected MarkerCallback slideUp;
    protected MarkerCallback slideUp2;
    protected double angleAdjust = 0;
    protected Pose2d intPose;

    protected SampleMecanumDrive drive;
    protected TrajectorySequence trajectoryClose;
    protected TrajectorySequence trajectoryMiddle;
    protected TrajectorySequence trajectoryFar;
    Positions positions;

    // endregion

    // region Other Variable Definitions
    private Gamepad currentGamepad1 = new Gamepad();
    private Gamepad previousGamepad1 = new Gamepad();
    // endregion

    //grabs the hardware for motors/servos/etc
    protected void getHardware() {
        // region Servo Hardware
        intakeRotate = hardwareMap.servo.get("intakerotate");
        dump = hardwareMap.servo.get("dump");
        backClaw = hardwareMap.servo.get("backclaw");
        frontClaw = hardwareMap.servo.get("frontclaw");
        intakeStage2 = hardwareMap.get(CRServo.class, "intakestage2");
        plane = hardwareMap.servo.get("planeTrigger");
        planeRotate = hardwareMap.servo.get("Plane tilt");
        planeLock = hardwareMap.servo.get("claaamp");
        // endregion

        // region Motor Hardware
        rightSlide = hardwareMap.get(DcMotor.class, "rightslide");
        leftSlide = hardwareMap.get(DcMotor.class, "leftslide");
        intake = hardwareMap.get(DcMotor.class, "intake");

        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        drive = new SampleMecanumDrive(hardwareMap);
        // endregion

        // region Camera Setup
        initTfod();
        // endregion
    }

    // creates camera recognition
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

    //this function does all the heavy lifting for the team element recognition
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

    //This function takes the position of the team element and sets the where is team element variable
    protected void returnLocation(double position) {
        if ((isBlue && isClose) || (isRed && isFar)) {

            if (isBlue) {
                telemetry.addData("Tfod", "Starting at Blue Close");
            } else {
                telemetry.addData("Tfod", "Starting at Red Far");
            }
            if (position > 300) {
                whereIsTeamElement = "close";
                telemetry.addData("WE ARE close to the beams", " ");
            } else if (position >= 0) {
                whereIsTeamElement = "middle";
                telemetry.addData("WE ARE AT THE Middle", " ");
            } else {
                whereIsTeamElement = "far";
                telemetry.addData("WE ARE far from the beams I HOPE BECAUSE", "THIS IS A GUESS BECAUSE WE CAN'T SEE IT");
            }
        } else {
            if (isBlue) {
                telemetry.addData("Tfod", "Starting at Blue Far");
            } else {
                telemetry.addData("Tfod", "Starting at Red close");
            }

            if (position > 300) {
                whereIsTeamElement = "far";
                telemetry.addData("WE ARE far to the beams", " ");
            } else if (position >= 0) {
                whereIsTeamElement = "middle";
                telemetry.addData("WE ARE AT THE Middle", " ");
            } else {
                whereIsTeamElement = "close";
                telemetry.addData("WE ARE close from the beams I HOPE BECAUSE", "THIS IS A GUESS BECAUSE WE CAN'T SEE IT");
            }
        }
        telemetry.update();
    }

    //function that moves the slides to a set position at a given speed
    protected void moveSlides(int position, double velocity) {
        leftSlide.setTargetPosition(position);
        rightSlide.setTargetPosition(position);

        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightSlide.setPower(velocity);
        leftSlide.setPower(velocity);
    }

    //creates the trajectories and other stuff for paths
    protected void setupTrajectories() {
        intakeUp = () -> {
            intakeRotate.setPosition(Constants.intakeUp);
        };

        intake1Pixel = () -> {
            intake.setPower(1);
            intakeStage2.setPower(-1);
            intakeRotate.setPosition(Constants.intakePickupStack5);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    intakeRotate.setPosition(Constants.intakeUp);
                }
            }, 1000L);
        };

        intake2Pixel = () -> {
            intake.setPower(1);
            intakeStage2.setPower(-1);
            intakeRotate.setPosition(Constants.intakePickupStack5);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    intakeRotate.setPosition(Constants.intakePickupStack4);
                }
            }, 1000L);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    intakeRotate.setPosition(Constants.intakeUp);
                }
            }, 1750L);
        };
        slideUp = () -> {
            moveSlides(1100, 1);
            dump.setPosition(Constants.dumpUp);
        };
        slideUp2 = () -> {
            frontClaw.setPosition(Constants.frontClawClosed);
            backClaw.setPosition(Constants.backClawClosed);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    intake.setPower(0);
                    intakeStage2.setPower(0);
                    moveSlides(1500, 1);
                    dump.setPosition(Constants.dumpUp);
                }
            }, 200L);
        };
        MarkerCallback intakePixel = () -> {
            intake.setPower(1);
            intakeStage2.setPower(-1);
            intakeRotate.setPosition(Constants.intakePickupStack5);
            sleep(1000);
            intakeRotate.setPosition(Constants.intakePickupStack4);
            sleep(750);
            intakeRotate.setPosition(Constants.intakeUp);
            sleep(500);
        };

        positions = new Positions(isRed);

        if (isClose) {
            intPose = isBlue ? positions.startBlueClosePos : positions.startRedClosePos;
            double multipler = isBlue ? 1 : -1;
            TrajectorySequenceBuilder unfinishedMiddle = drive.trajectorySequenceBuilder(intPose)
                    .lineTo(new Vector2d(10.2, 33.5 * multiplier))
                    .addDisplacementMarker(intakeUp)
                    .lineTo(new Vector2d(10.2, 40 * multiplier))
                    .addDisplacementMarker(slideUp)
                    .splineToLinearHeading(positions.dropMiddlePos, 0);
            TrajectorySequenceBuilder unfinishedClose = drive.trajectorySequenceBuilder(intPose)
                    .lineTo(new Vector2d(10.2, 55 * multiplier))
                    .lineTo(new Vector2d(20, 45 * multiplier))
                    .splineToLinearHeading(new Pose2d(10, 30 * multiplier, Math.toRadians(0)), Math.toRadians(180))
                    .addDisplacementMarker(intakeUp)
                    .addDisplacementMarker(slideUp)
                    .splineToConstantHeading(positions.dropClosePos.vec(), 0);
            TrajectorySequenceBuilder unfinishedFar = drive.trajectorySequenceBuilder(intPose)
                    .lineTo(new Vector2d(10.2, 55 * multiplier))
                    .lineTo(new Vector2d(25, 45 * multiplier))
                    .addDisplacementMarker(intakeUp)
                    .splineToLinearHeading(new Pose2d(32, 30 * multiplier, Math.toRadians(0)), Math.toRadians(180))
                    .addDisplacementMarker(slideUp)
                    .splineToConstantHeading(positions.dropFarPos.vec(), 0);
            if (morePixels) {

            }
            if (parkWall) {
                unfinishedMiddle = unfinishedMiddle.lineTo(new Vector2d(40, 30 * multiplier))
                        .splineToConstantHeading(new Vector2d(47, 60 * multiplier), Math.toRadians(45))
                        .splineToConstantHeading(new Vector2d(58, 62 * multiplier), Math.toRadians(0));
                unfinishedClose = unfinishedClose.lineTo(new Vector2d(40, 30 * multiplier))
                        .splineToConstantHeading(new Vector2d(47, 60 * multiplier), Math.toRadians(45))
                        .splineToConstantHeading(new Vector2d(58, 62 * multiplier), Math.toRadians(0));
                unfinishedFar = unfinishedFar.lineTo(new Vector2d(40, 30 * multiplier))
                        .splineToConstantHeading(new Vector2d(47, 60 * multiplier), Math.toRadians(45))
                        .splineToConstantHeading(new Vector2d(58, 62 * multiplier), Math.toRadians(0));
            } else {
                unfinishedMiddle = unfinishedMiddle.lineTo(new Vector2d(40, 30 * multiplier))
                        .splineToConstantHeading(new Vector2d(47, 60 * multiplier), Math.toRadians(45))
                        .splineToConstantHeading(new Vector2d(58, 62 * multiplier), Math.toRadians(0));
                unfinishedClose = unfinishedClose.lineTo(new Vector2d(40, 30 * multiplier))
                        .splineToConstantHeading(new Vector2d(47, 60 * multiplier), Math.toRadians(45))
                        .splineToConstantHeading(new Vector2d(58, 62 * multiplier), Math.toRadians(0));
                unfinishedFar = unfinishedFar.lineTo(new Vector2d(40, 30 * multiplier))
                        .splineToConstantHeading(new Vector2d(47, 60 * multiplier), Math.toRadians(45))
                        .splineToConstantHeading(new Vector2d(58, 62 * multiplier), Math.toRadians(0));

            }


            trajectoryMiddle = unfinishedMiddle.build();
            trajectoryClose = unfinishedClose.build();
            trajectoryFar = unfinishedFar.build();
        } else {
            intPose = isBlue ? positions.startBlueFarPos : positions.startRedFarPos;
            double multipler = isBlue ? 1 : -1;
            TrajectorySequenceBuilder unfinishedMiddle = drive.trajectorySequenceBuilder(intPose);
            //Liam this is where i added code

            TrajectorySequenceBuilder unfinishedClose = drive.trajectorySequenceBuilder(intPose);
            TrajectorySequenceBuilder unfinishedFar = drive.trajectorySequenceBuilder(intPose);


            trajectoryMiddle = unfinishedMiddle.build();
            trajectoryClose = unfinishedClose.build();
            trajectoryFar = unfinishedFar.build();

        }

    }

    ;

    @Override
    public void runOpMode() {
        getHardware();
        //this while loop will repeat until both triggers are clicked or program starts
        while (!isStopRequested() && opModeInInit() && !doneSetup) {
            //used to detect if button pressed
            previousGamepad1.copy(currentGamepad1);
            currentGamepad1.copy(gamepad1);
            //toggle for is blue
            if (currentGamepad1.triangle && !previousGamepad1.triangle) {
                isBlue = !isBlue;
                isRed = !isBlue;
            }
            //toggle for is close
            if (currentGamepad1.circle && !previousGamepad1.circle) {
                isClose = !isClose;
                isFar = !isClose;
            }
            //toggle for is park left
            if (currentGamepad1.square && !previousGamepad1.square) {
                parkWall = !parkWall;
                parkMiddle = !parkWall;
            }

            //toggle for is through truss
            if (currentGamepad1.cross && !previousGamepad1.cross) {
                throughTruss = !throughTruss;
            }

            //toggle for is get more pixels
            if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                morePixels = !morePixels;
            }

            //starts the program
            if (currentGamepad1.left_trigger == 1 && currentGamepad1.right_trigger == 1) {
                doneSetup = true;
            }
            //telemetry
            telemetry.addData("(Δ) We are on alliance", isBlue ? "blue" : "red");
            telemetry.addData("(○) We are starting ", isClose ? "close" : "far");
            telemetry.addData("(□) We will Park near", parkWall ? "wall" : "middle");
            telemetry.addData("(×) We will go through the", throughTruss ? "truss" : "gate");
            telemetry.addData("(Dpad Up) We will Grab", morePixels ? "extra pixels" : "we will not grab extra pixels");
            telemetry.addData("Press both triggers to start", "");
            telemetry.update();


        }
        telemetry.addData("You need to wait", "Fam");
        telemetry.update();
        //Sets up the Trajectories the sleep
        //is to ensure that the camera works
        setupTrajectories();
        drive.setPoseEstimate(intPose);
        sleep(6000);

        // region Initialize Servos
        plane.setPosition(Constants.triggerStore);
        planeLock.setPosition(Constants.planeLockStore);
        planeRotate.setPosition(Constants.planeRotateStore);
        frontClaw.setPosition(Constants.frontClawClosed);
        backClaw.setPosition(Constants.backClawClosed);
        //endregion

        //telemetry to say that initialization is done
        telemetry.addData("you good fam", "");
        telemetry.update();

        waitForStart();
        if (opModeIsActive()) {
            //rotates intake down to grab pixel
            intakeRotate.setPosition(Constants.intakePickupStack1);

            //Read team element and wait
            readTeamElement();
            if (whereIsTeamElement == "close") {
                drive.followTrajectorySequence(trajectoryClose);
            } else if (whereIsTeamElement == "middle") {
                drive.followTrajectorySequence(trajectoryMiddle);
            } else {
                drive.followTrajectorySequence(trajectoryFar);
            }
            sleep(10000);
        }
    }


}
