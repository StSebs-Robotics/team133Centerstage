package org.firstinspires.ftc.teamcode.myCode.autov3;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.roadrunner.InstantFunction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
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
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.myCode.utilities.Constants;
import org.firstinspires.ftc.teamcode.myCode.utilities.Positions;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Autonomous(name = "AutoV3", group = "133")
public class Auto extends LinearOpMode {
    // region Servo Variables Definitions
    private Servo intakeRotate;
    private Servo dump;
    private Servo plane;
    private Servo planeLock;
    private Servo planeRotate;
    private Servo frontClaw;
    private Servo backClaw;
    private Servo rotateClaw;
    private CRServo intakeStage2;
    // endregion

    // region Motors Variables Definitions
    private DcMotor leftSlide;
    private DcMotor rightSlide;
    private DcMotor intake;
    // endregion

    // region Camera Variables Definitions
    private TfodProcessor tfod;
    private String whereIsTeamElement = "left";
    private static final boolean USE_WEBCAM = true;
    private static final String TFOD_MODEL_ASSET = "teamElement.tflite";
    private static final String[] LABELS = {"blue", "red"};
    private VisionPortal visionPortal;
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
    private int multiplier = 1;
    private double angleAdjust = 0;
    private Pose2d intPose;

    private MecanumDrive drive;
    private Action trajectoryClose;
    private Action trajectoryMiddle;
    private Action trajectoryFar;
    Positions positions;

    // endregion

    // region Other Variable Definitions
    private Gamepad currentGamepad1 = new Gamepad();
    private Gamepad previousGamepad1 = new Gamepad();
    // endregion

    private double angleConvert(double angle) {
        return Math.toRadians(angle * (isBlue ? 1 : -1));
    }

    private Vector2d vector2dM(double x, double y) {
        return new Vector2d(x, y * (isBlue ? 1 : -1));
    }

    private Pose2d pose2dM(double x, double y, double angle) {
        return new Pose2d(x, y * (isBlue ? 1 : -1), angle);
    }

    //grabs the hardware for motors/servos/etc
    private void getHardware() {
        // region Servo Hardware
        intakeRotate = hardwareMap.servo.get("intakerotate");
        dump = hardwareMap.servo.get("dump");
        backClaw = hardwareMap.servo.get("backclaw");
        frontClaw = hardwareMap.servo.get("frontclaw");
        intakeStage2 = hardwareMap.get(CRServo.class, "intakestage2");
        plane = hardwareMap.servo.get("planeTrigger");
        planeRotate = hardwareMap.servo.get("Plane tilt");
        planeLock = hardwareMap.servo.get("claaamp");
        rotateClaw = hardwareMap.servo.get("clawrotate");
        // endregion

        // region Motor Hardware
        rightSlide = hardwareMap.get(DcMotor.class, "rightslide");
        leftSlide = hardwareMap.get(DcMotor.class, "leftslide");
        intake = hardwareMap.get(DcMotor.class, "intake");

        rightSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftSlide.setDirection(DcMotorSimple.Direction.REVERSE);
        // endregion

        // region Camera Setup
        initTfod();
        // endregion
    }

    // creates camera recognition
    private void initTfod() {
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
    private void readTeamElement() {

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
    private void returnLocation(double position) {
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
    private void moveSlides(int position, double velocity) {
        leftSlide.setTargetPosition(position);
        rightSlide.setTargetPosition(position);

        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightSlide.setPower(velocity);
        leftSlide.setPower(velocity);
    }

    /*creates the trajectories and other stuff for paths*/
    private void setupTrajectories() {
        InstantFunction intakeUp = () -> {
            intakeRotate.setPosition(Constants.intakeUp);
        };

        InstantFunction intake1Pixel = () -> {
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

        InstantFunction intake2Pixel = () -> {
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
        InstantFunction slideUp = () -> {
            moveSlides(1100, 1);
            dump.setPosition(Constants.dumpUp);
        };
        InstantFunction slideUp2 = () -> {
            intake.setPower(0);
            intakeStage2.setPower(0);
            moveSlides(1500, 1);
            dump.setPosition(Constants.dumpUp);
        };
        InstantFunction dropPixels = () -> {
            frontClaw.setPosition(Constants.frontClawOpen);
            backClaw.setPosition(Constants.backClawOpen);
            sleep(100);
            dump.setPosition(Constants.dumpDown);
            sleep(200);
            moveSlides(0, 1);
        };
        InstantFunction drop1Pixel = () -> {
            frontClaw.setPosition(Constants.frontClawOpen);
            sleep(100);
            dump.setPosition(Constants.dumpDown);
            sleep(200);
            moveSlides(0, 1);
        };
        InstantFunction intakeBack = () -> {
            frontClaw.setPosition(Constants.frontClawClosed);
            backClaw.setPosition(Constants.backClawClosed);
            //intake.setPower(-1);
        };
        InstantFunction intakeOff = () -> {
            intake.setPower(0);
            intakeStage2.setPower(0);
        };

        positions = new Positions(isRed);
        TrajectoryActionBuilder unfinishedMiddle;
        TrajectoryActionBuilder unfinishedClose;
        TrajectoryActionBuilder unfinishedFar;
        if (isClose) {
            intPose = isBlue ? positions.startBlueClosePos : positions.startRedClosePos;
            drive = new MecanumDrive(hardwareMap, intPose);
            telemetry.update();
            multiplier = isBlue ? 1 : -1;
            unfinishedMiddle = drive.actionBuilder(intPose)
//                    .splineTo(vector2dM(10.2, 33.5 ))
//                    .afterTime(0,intakeUp)
//                    .splineTo(vector2dM(10.2, 40 ))
//                    .afterTime(0,slideUp)
//                    .splineToLinearHeading(positions.dropMiddlePos,0)

                    .setTangent(angleConvert(270))
                    //.splineTo(vector2dM(10.2, 35), angleConvert(270))
                    //.back(26)
                    .afterTime(0, intakeUp)
                    .afterTime(0, slideUp)
                    .setTangent(0)
                    .splineToLinearHeading(positions.dropMiddlePos, angleConvert(0))
                    .afterTime(0, dropPixels)
                    .waitSeconds(.3);

            unfinishedClose = drive.actionBuilder(intPose)

//                    .splineTo(vector2dM(10.2, 55 ))
//                    .splineTo(vector2dM(20, 45 ))
//                    .splineToLinearHeading(pose2dM(10, 30 , angleConvert(0)), angleConvert(180))
//                    .afterTime(0,intakeUp)
//                    .afterTime(0,slideUp)
//                    .splineToConstantHeading(positions.dropClosePos.position, 0)

                    .setTangent(angleConvert(270))
                    .splineToConstantHeading(vector2dM(12, 45), angleConvert(270))
                    .splineTo(vector2dM(8, 38), angleConvert(225))
                    .afterTime(0, intakeUp)
                    .afterTime(0, intakeUp)
                    .afterTime(0, slideUp)
                    .strafeToSplineHeading(vector2dM(20, 30), angleConvert(0))
                    .splineToConstantHeading(positions.dropClosePos.position, angleConvert(0))
                    .afterTime(0, dropPixels)
                    .waitSeconds(.3);

            unfinishedFar = drive.actionBuilder(intPose)

//                    .splineTo(vector2dM(10.2, 55 ))
//                    .splineTo(vector2dM(25, 45 ))
//                    .afterTime(0,intakeUp)
//                    .splineToLinearHeading(pose2dM(32, 30 , angleConvert(0)), angleConvert(180))
//                    .afterTime(0,slideUp)
//                    .splineToConstantHeading(positions.dropFarPos.position, 0)

                    .setTangent(270)
                    .splineToConstantHeading(vector2dM(25, 42), angleConvert(270))
                    .afterTime(0, intakeUp)
                    .afterTime(0, slideUp)
                    .splineToLinearHeading(positions.dropFarPos, angleConvert(0))
                    .afterTime(0, dropPixels)
                    .turn(0)
                    .waitSeconds(.3);
        } else {
            intPose = isBlue ? positions.startBlueFarPos : positions.startRedFarPos;
            drive = new MecanumDrive(hardwareMap, intPose);
            //JOHNATHON ADD THEM HERE IDIOT
            unfinishedMiddle = drive.actionBuilder(intPose)

                    .setTangent(angleConvert(270))
                    .splineTo(vector2dM(-38.1, 34), angleConvert(270))
                    .setTangent(angleConvert(180))
                    //intake up
                    .afterTime(0, intakeUp)
                    //.waitSeconds(.3)
                    //.forward(3)
                    .splineToLinearHeading(positions.stack1Pos, angleConvert(180))
                    //Intake 1
                    //.afterTime(0,intake1Pixel)
                    .setTangent(angleConvert(270))
                    .splineToConstantHeading(vector2dM(-46, 13), 0)
                    .splineToConstantHeading(vector2dM(0, 13), 0)
//                     //slide up
//                     .afterTime(0,slideUp)
                    .splineToConstantHeading(vector2dM(24, 13), 0)
                    .splineToConstantHeading(vector2dM(54, 33), 0)
//                     //drop 1 pixel
//                     .afterTime(0,drop1Pixel)
                    .setTangent(angleConvert(90))
                    .splineToConstantHeading(positions.dropMiddlePos.position, angleConvert(90))
//                     //drop other pixel
//                     .afterTime(0,dropPixels)
            ;

            unfinishedClose = drive.actionBuilder(intPose)

//                     .setTangent(angleConvert(270))
//                     .splineToConstantHeading(vector2dM(-47,42),angleConvert(200))
//                     //intake up
//                     .afterTime(0,intakeUp)
//                     .strafeLeft(5)
//                     .splineToLinearHeading(positions.stack3Pos,angleConvert(250))
//                     //intake 1
//                     .afterTime(0,intake1Pixel)
//                     .setTangent(angleConvert(270))
//                     .splineToConstantHeading(vector2dM(-46, 13), 0)
//                     .splineToConstantHeading(vector2dM(0,13),0)
//                     //slide up
//                     .afterTime(0,slideUp)
//                     .splineToConstantHeading(vector2dM(24,13),0)
//                     .splineToConstantHeading(vector2dM(54,28),0)
//                     //drop 1 pixel
//                      .afterTime(0,drop1Pixel)
//                     .setTangent(angleConvert(90))
//                     .splineToConstantHeading(vector2dM(54,33),angleConvert(90))
//                     //drop other pixel
//                    .afterTime(0,dropPixels)
            ;

            unfinishedFar = drive.actionBuilder(intPose)

//                     .setTangent(angleConvert(270))
//                     .splineToLinearHeading(pose2dM(-34,30, angleConvert(180)),angleConvert(0))
//                     //intake up
//                     .afterTime(0,intakeUp)
//                     .setTangent(angleConvert(179))
//                     .splineToLinearHeading(positions.stack1Pos, angleConvert(180))
//                     //intake one
//                     .afterTime(0,intake1Pixel)
//                     .setTangent(angleConvert(300))
//                     //.splineToConstantHeading(vector2dM(-55,23),angleConvert(280))
//                     .splineToConstantHeading(vector2dM(-16,13),angleConvert(0))
//                     .splineToConstantHeading(vector2dM(0,13),angleConvert(0))
//                     //slide up
//                     .afterTime(0,slideUp2)
//                     .splineToConstantHeading(vector2dM(24,13),0)
//                     .splineToConstantHeading(positions.dropClosePos.position,0)
//                     //drop 1 pixel
//                     .afterTime(0,drop1Pixel)
//                     .setTangent(angleConvert(90))
//                     .splineToConstantHeading(positions.dropFarPos.position,angleConvert(90))
//                     //drop other pixel
//                     .afterTime(0,dropPixels)
            ;


        }
        if (morePixels) {
            if (throughTruss) {
                unfinishedMiddle = unfinishedMiddle
                        //.back(2)
                        .splineToConstantHeading(vector2dM(30, 52), angleConvert(110))
                        .splineToConstantHeading(vector2dM(0, 59), angleConvert(180))
                        .splineToConstantHeading(vector2dM(-24, 59), angleConvert(180))
                        .splineToConstantHeading(vector2dM(-52, 50), angleConvert(220))
                        .splineToConstantHeading(positions.stack1Pos.position, angleConvert(270))
                        .afterTime(0, intake2Pixel)
                        .waitSeconds(1.250)
                        .afterTime(0, intakeBack)
                        //GO Pack
                        //.forward(3)
                        .splineToConstantHeading(vector2dM(-52, 48), angleConvert(45))
                        .splineToConstantHeading(vector2dM(-23, 60), angleConvert(0))
                        .splineToConstantHeading(vector2dM(0, 60), angleConvert(0))
                        .afterTime(0, slideUp2)
                        .afterTime(0, intakeOff)
                        .splineToConstantHeading(positions.dropMiddlePos.position.plus(vector2dM(0, -2.5)), angleConvert(0))
                        .afterTime(0, dropPixels)
                ;

                unfinishedClose = unfinishedClose
                        //.back(2)
                        .splineToConstantHeading(vector2dM(30, 52), angleConvert(110))
                        .splineToConstantHeading(vector2dM(0, 59), angleConvert(180))
                        .splineToConstantHeading(vector2dM(-24, 59), angleConvert(180))
                        .splineToConstantHeading(vector2dM(-52, 50), angleConvert(220))
                        .splineToConstantHeading(positions.stack1Pos.position, angleConvert(270))
                        .afterTime(0, intake2Pixel)
                        .waitSeconds(1.250)
                        .afterTime(0, intakeBack)
                        //GO Back
                        //.forward(3)
                        .splineToConstantHeading(vector2dM(-52, 48), angleConvert(45))
                        .splineToConstantHeading(vector2dM(-23, 60), angleConvert(0))
                        .splineToConstantHeading(vector2dM(0, 60), angleConvert(0))
                        .afterTime(0, slideUp2)
                        .afterTime(0, intakeOff)
                        .splineToConstantHeading(positions.dropMiddlePos.position.plus(vector2dM(0, -2.5)), angleConvert(0))
                        .afterTime(0, dropPixels)
                ;

                unfinishedFar = unfinishedFar
                        //.back(2)
                        .splineToConstantHeading(vector2dM(30, 52), angleConvert(110))
                        .splineToConstantHeading(vector2dM(0, 59), angleConvert(180))
                        .splineToConstantHeading(vector2dM(-24, 59), angleConvert(180))
                        .splineToConstantHeading(vector2dM(-52, 50), angleConvert(220))
                        .splineToConstantHeading(positions.stack1Pos.position, angleConvert(270))
                        .afterTime(0, intake2Pixel)
                        .waitSeconds(1.250)
                        .afterTime(0, intakeBack)
                        //GO Back
                        //.forward(3)
                        .splineToConstantHeading(vector2dM(-52, 48), angleConvert(45))
                        .splineToConstantHeading(vector2dM(-23, 60), angleConvert(0))
                        .splineToConstantHeading(vector2dM(0, 60), angleConvert(0))
                        .afterTime(0, slideUp2)
                        .afterTime(0, intakeOff)
                        .splineToConstantHeading(positions.dropMiddlePos.position.plus(vector2dM(0, -2.5)), angleConvert(0))
                        .afterTime(0, dropPixels)
                ;
            } else {
                unfinishedMiddle = unfinishedMiddle
                        //.back(2)
                        .splineToConstantHeading(vector2dM(26, 13), angleConvert(180))
                        .splineToConstantHeading(positions.stack3Pos.position, angleConvert(180))
                        .afterTime(0, intake2Pixel)
                        .waitSeconds(2)
                        .afterTime(0, intakeBack)
                        //Go back
                        .setTangent(0)
                        .splineToConstantHeading(vector2dM(-34, 13), angleConvert(350))
                        .splineToConstantHeading(vector2dM(0, 13), angleConvert(0))
                        .afterTime(0, slideUp2)
                        .splineToConstantHeading(vector2dM(18, 13), angleConvert(0))
                        .afterTime(0, intakeOff)
                        .splineToConstantHeading(positions.dropMiddlePos.position.plus(vector2dM(0, -2.5)), angleConvert(45))
                        .afterTime(0, dropPixels)
                ;


                unfinishedClose = unfinishedClose
                        //.back(2)
                        .splineToConstantHeading(vector2dM(26, 13), angleConvert(180))
                        .splineToConstantHeading(positions.stack3Pos.position, angleConvert(180))
                        .afterTime(0, intake2Pixel)
                        .waitSeconds(2.5)
                        .afterTime(0, intakeBack)
                        //Go Back
                        .setTangent(0)
                        .splineToConstantHeading(vector2dM(-34, 13), angleConvert(350))
                        .splineToConstantHeading(vector2dM(0, 13), angleConvert(0))
                        .afterTime(0, slideUp2)
                        .splineToConstantHeading(vector2dM(18, 13), angleConvert(0))
                        .afterTime(0, intakeOff)
                        .splineToConstantHeading(positions.dropClosePos.position.plus(vector2dM(0, 2.5)), angleConvert(45))
                        .afterTime(0, dropPixels)
                ;

                unfinishedFar = unfinishedFar
                        //.back(2)
                        .splineToConstantHeading(vector2dM(26, 13), angleConvert(180))
                        .splineToConstantHeading(positions.stack3Pos.position, angleConvert(180))
                        .afterTime(0, intake2Pixel)
                        .waitSeconds(2.5)
                        .afterTime(0, intakeBack)
                        //Go Back
                        .setTangent(0)
                        .splineToConstantHeading(vector2dM(-34, 13), angleConvert(350))
                        .splineToConstantHeading(vector2dM(0, 13), angleConvert(0))
                        .afterTime(0, slideUp2)
                        .splineToConstantHeading(vector2dM(18, 13), angleConvert(0))
                        .afterTime(0, intakeOff)
                        .splineToConstantHeading(positions.dropFarPos.position.plus(vector2dM(0, -2.5)), angleConvert(45))
                        .afterTime(0, dropPixels)
                ;
            }
        }

        if (parkWall) {
            unfinishedMiddle = unfinishedMiddle
                    .lineToX(48)
                    .setTangent(angleConvert(90))
                    .splineToConstantHeading(vector2dM(48, 53),angleConvert(90))
                    .splineToConstantHeading(positions.wallParkPos.position, angleConvert(0));
            unfinishedClose = unfinishedClose
                    .lineToX(48)
                    .setTangent(angleConvert(90))
                    .splineToConstantHeading(vector2dM(48, 53),angleConvert(90))
                    .splineToConstantHeading(positions.wallParkPos.position, angleConvert(0));
            unfinishedFar = unfinishedFar
                    .lineToX(48)
                    .setTangent(angleConvert(90))
                    .splineToConstantHeading(vector2dM(48, 53),angleConvert(90))
                    .splineToConstantHeading(positions.wallParkPos.position, angleConvert(0));
        } else {
            unfinishedMiddle = unfinishedMiddle
                    .lineToX(48)
                    .splineToConstantHeading(vector2dM(48,16), angleConvert(0))
                    .splineToConstantHeading(positions.middleParkPos.position, angleConvert(0));

            unfinishedClose = unfinishedClose
                    .lineToX(48)
                    .splineToConstantHeading(vector2dM(48,16), angleConvert(0))
                    .splineToConstantHeading(positions.middleParkPos.position, angleConvert(0));
            unfinishedFar = unfinishedFar
                    .lineToX(48)
                    .splineToConstantHeading(vector2dM(48,16), angleConvert(0))
                    .splineToConstantHeading(positions.middleParkPos.position, angleConvert(0));

        }

        trajectoryMiddle = unfinishedMiddle.build();
        trajectoryClose = unfinishedClose.build();
        trajectoryFar = unfinishedFar.build();

    }


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
                morePixels = !morePixels;
            }

            //toggle for is get more pixels
            if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                throughTruss = !throughTruss;
            }

            //starts the program
            if (currentGamepad1.left_trigger == 1 && currentGamepad1.right_trigger == 1) {
                doneSetup = true;
            }
            //telemetry
            telemetry.addData("(Δ) We are on alliance", isBlue ? "blue" : "red");
            telemetry.addData("(○) We are starting ", isClose ? "close" : "far");
            telemetry.addData("(□) We will Park near", parkWall ? "wall" : "middle");
            telemetry.addData("(×) We will Grab", morePixels ? "extra pixels" : "we will not grab extra pixels");
            if (morePixels) {
                telemetry.addData("(Dpad Up) We will go through the", throughTruss ? "truss" : "gate");
            }
            telemetry.addData("Press both triggers to start", "");
            telemetry.update();


        }
        telemetry.addData("You need to wait", "Fam");
        telemetry.update();
        //Sets up the Trajectories the sleep
        //is to ensure that the camera works
        setupTrajectories();
        drive.pose = intPose;
        sleep(6000);

        // region Initialize Servos
        plane.setPosition(Constants.triggerStore);
        planeLock.setPosition(Constants.planeLockStore);
        planeRotate.setPosition(Constants.planeRotateStore);
        frontClaw.setPosition(Constants.frontClawClosed);
        backClaw.setPosition(Constants.backClawClosed);
        rotateClaw.setPosition(Constants.clawRotateMiddle);
        //endregion

        //telemetry to say that initialization is done
        telemetry.addData("you good fam", "");
        telemetry.update();

        waitForStart();
        if (opModeIsActive()) {
            //rotates intake down to grab pixel
            intakeRotate.setPosition(Constants.intakePickupStack1 - .01);
            sleep(500);

            //Read team element and wait
            readTeamElement();
            Action chosen;
            if (whereIsTeamElement == "close") {
                chosen = trajectoryClose;
            } else if (whereIsTeamElement == "middle") {
                chosen = trajectoryMiddle;
            } else {
                chosen = trajectoryFar;
            }
            Actions.runBlocking(
                    new SequentialAction(
                            chosen
                    )
            );
            sleep(10000);
        }
    }


}
