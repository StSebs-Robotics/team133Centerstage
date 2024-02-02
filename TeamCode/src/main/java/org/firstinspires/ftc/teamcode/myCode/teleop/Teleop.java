package org.firstinspires.ftc.teamcode.myCode.teleop;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.myCode.utilities.Constants;

import java.util.Timer;
import java.util.TimerTask;

@TeleOp(name = "Teleop2.0")
public class Teleop extends LinearOpMode {
    // region Hardware Variables
    private Servo intakeRotate;
    protected SampleMecanumDrive drive;

    private double yMultiplier = 1;
    private Servo dump;

    private IMU imu;
    private CRServo intakeStage2;
    private Servo frontClaw;
    private Servo backClaw;

    private Servo plane;
    private Servo planeLock;
    private Servo planeRotate;
    private DcMotor leftSlide;
    private DcMotor rightsSlide;
    private DcMotor intake;

    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    Gamepad currentGamepad1 = new Gamepad();
    Gamepad currentGamepad2 = new Gamepad();

    Gamepad previousGamepad1 = new Gamepad();
    Gamepad previousGamepad2 = new Gamepad();

    // endregion

    private int[] slidePosition = { 0 };
    private boolean[] isSlideLocked = { false };
    private int intakePower = 0;
    private double dumpPos = 0;
    private boolean clawFrontOpen = true;
    private boolean clawBackOpen = true;

    private void moveSlides(int distance, double velocity) {
        leftSlide.setTargetPosition(-distance);
        rightsSlide.setTargetPosition(distance);

        leftSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightsSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightsSlide.setPower(velocity);
        leftSlide.setPower(velocity);

    }

    private void LaunchPlane() {
        planeRotate.setPosition(.25);
        sleep(150);
        planeLock.setPosition(0);
        sleep(100);
        plane.setPosition(0);
    }

    private void StorePlane() {
        plane.setPosition(.4);
        planeLock.setPosition(.4);
        planeRotate.setPosition(0);
    }

    @Override
    public void runOpMode() {
        // region Hardware Initialization

        imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters myIMUparameters;

        myIMUparameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));

        // Initialize IMU using Parameters
        imu.initialize(myIMUparameters);
        imu.resetYaw();

        backLeft = hardwareMap.get(DcMotor.class, "backleft");
        backRight = hardwareMap.get(DcMotor.class, "backright");
        frontLeft = hardwareMap.get(DcMotor.class, "frontleft");
        frontRight = hardwareMap.get(DcMotor.class, "frontright");
        drive = new SampleMecanumDrive(hardwareMap);
        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightsSlide = hardwareMap.get(DcMotor.class, "rightslide");
        leftSlide = hardwareMap.get(DcMotor.class, "leftslide");

        intake = hardwareMap.get(DcMotor.class, "intake");

        intakeRotate = hardwareMap.servo.get("intakerotate");
        dump = hardwareMap.servo.get("dump");

        backClaw = hardwareMap.servo.get("backclaw");
        frontClaw = hardwareMap.servo.get("frontclaw");
        intakeStage2 = hardwareMap.get(CRServo.class, "intakestage2");

        plane = hardwareMap.servo.get("planeTrigger");
        planeRotate = hardwareMap.servo.get("Plane tilt");
        planeLock = hardwareMap.servo.get("claaamp");

        rightsSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // endregion

        dump.setPosition(Constants.dumpDown);
        backClaw.setPosition(Constants.backClawOpen);
        frontClaw.setPosition(Constants.frontClawOpen);
        clawBackOpen = true;
        clawFrontOpen = true;
        StorePlane();
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {

                previousGamepad1.copy(currentGamepad1);
                previousGamepad2.copy(currentGamepad2);

                currentGamepad1.copy(gamepad1);
                currentGamepad2.copy(gamepad2);

                // region Movement Controls
                Pose2d poseEstimate = drive.getPoseEstimate();

                // Create a vector from the gamepad x/y inputs
                // Then, rotate that vector by the inverse of that heading
                YawPitchRollAngles robotOrientation;
                robotOrientation = imu.getRobotYawPitchRollAngles();
                Vector2d input = new Vector2d(-currentGamepad1.left_stick_x * yMultiplier,
                        currentGamepad1.left_stick_y * yMultiplier)
                        .rotated(-robotOrientation.getYaw(AngleUnit.RADIANS));
                if (currentGamepad1.left_bumper) {
                    input = new Vector2d(-currentGamepad1.left_stick_x * yMultiplier / 3,
                            currentGamepad1.left_stick_y * yMultiplier / 3)
                            .rotated(-robotOrientation.getYaw(AngleUnit.RADIANS));
                }
                // Pass in the rotated input + right stick value for rotation
                // Rotation is not part of the rotated input thus must be passed in separately

                drive.setWeightedDrivePower(new Pose2d(input.getX(), input.getY(), -currentGamepad1.right_stick_x));

                if (currentGamepad1.options) {
                    if (yMultiplier == 1) {
                        yMultiplier = -1;
                    } else {
                        yMultiplier = 1;
                    }
                }

                // Update everything. Odometry. Etc.
                drive.update();
                // endregion

                // region CurrentGamepad1

                // Rotates intake to bottom position
                if (currentGamepad1.dpad_right) {
                    // used to be function intake 1
                    intakeRotate.setPosition(Constants.intakePickupStack1);
                }
                // Rotates intake up
                if (currentGamepad1.share) {
                    intakeRotate.setPosition(Constants.intakeUp);
                }
                // Resets the position {Home Button}
                if (currentGamepad1.ps) {
                    imu.resetYaw();
                    drive.setPoseEstimate(new Pose2d(0, 0, 0));
                    currentGamepad1.rumble(200);
                }

                if (currentGamepad1.triangle) {
                    dump.setPosition(dump.getPosition() + .001);
                    dumpPos = dump.getPosition();
                }

                if (currentGamepad1.square) {
                    dump.setPosition(dump.getPosition() - .001);
                    dumpPos = dump.getPosition();
                }

                // endregion

                // region CurrentGamepad2

                // Checks if the intake is locked from going down to prevent conflict
                if (!isSlideLocked[0]) {
                    // Move Slide Up
                    if (currentGamepad2.right_trigger > 0) {
                        clawFrontOpen = false;
                        clawBackOpen = false;
                        backClaw.setPosition(Constants.backClawClosed);
                        frontClaw.setPosition(Constants.frontClawClosed);
                        slidePosition[0] += 20 * currentGamepad2.right_trigger;
                    }

                    // Move Slide Down
                    if (currentGamepad2.left_trigger > 0 && slidePosition[0] > -2300) {
                        slidePosition[0] -= 20 * currentGamepad2.left_trigger;
                    }

                    // Slides Up
                    if (currentGamepad2.triangle && !previousGamepad2.triangle) {

                        backClaw.setPosition(Constants.backClawClosed);
                        frontClaw.setPosition(Constants.frontClawClosed);
                        dump.setPosition(Constants.dumpUp);
                        isSlideLocked[0] = true;
                        intakePower = 0;
                        // Does an asynchronous wait
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                slidePosition[0] = 1800;
                                isSlideLocked[0] = false;
                            }
                        }, 400L);
                    }
                    if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper && slidePosition[0] > 0) {
                        if (clawBackOpen) {
                            backClaw.setPosition(Constants.backClawClosed);
                            clawBackOpen = false;
                        } else {
                            frontClaw.setPosition(Constants.frontClawOpen);
                            backClaw.setPosition(Constants.backClawOpen);
                            clawBackOpen = true;
                            clawFrontOpen = true;

                        }
                    }
                    if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper && slidePosition[0] > 0) {
                        if (clawFrontOpen) {
                            frontClaw.setPosition(Constants.frontClawClosed);
                            clawFrontOpen = false;
                        } else {
                            frontClaw.setPosition(Constants.frontClawOpen);
                            clawFrontOpen = true;
                        }
                    }

                    // Bring Slides down
                    if (currentGamepad2.cross && !previousGamepad2.cross) {
                        dump.setPosition(Constants.dumpDown);
                        clawFrontOpen = true;
                        clawBackOpen = true;

                        backClaw.setPosition(Constants.backClawOpen);

                        frontClaw.setPosition(Constants.frontClawOpen);
                        isSlideLocked[0] = true;
                        // Does an asynchronous wait
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                slidePosition[0] = 0;
                                isSlideLocked[0] = false;
                            }
                        }, 500L);
                    }
                    if (slidePosition[0] < 0) {
                        slidePosition[0] = 0;
                    }

                    if (slidePosition[0] > Constants.slideMax) {
                        slidePosition[0] = Constants.slideMax;
                    }
                    moveSlides(slidePosition[0], 1);
                }

                // Drops Pixels
                if (currentGamepad2.circle) {
                    clawFrontOpen = true;
                    clawBackOpen = true;
                    backClaw.setPosition(Constants.backClawOpen);
                    frontClaw.setPosition(Constants.frontClawOpen);
                }

                // Launches plane
                if (currentGamepad2.options) {
                    LaunchPlane();
                }

                // Anti jam
                if (currentGamepad2.dpad_left) {
                    intake.setPower(-1);
                    sleep(50);
                    intake.setPower(1);
                }

                // region Intake Positions
                if (currentGamepad1.circle) {
                    intakeRotate.setPosition(Constants.intakePickupStack4);
                }

                if (currentGamepad2.square) {
                    intakeRotate.setPosition(Constants.intakePickupStack1);
                }

                if (currentGamepad1.triangle) {
                    intakeRotate.setPosition(Constants.intakePickupStack5);
                }
                if (currentGamepad1.circle) {
                    intakeRotate.setPosition(Constants.intakeUp);
                }
                // endregion

                // region Intake Power Management

                // if the power is 0 turn of intake
                if (intakePower == 0) {
                    intake.setPower(0);
                    intakeStage2.setPower(0);
                }

                // if power is 1 run intake
                if (intakePower == 1 && slidePosition[0] == 0) {
                    intake.setPower(1);
                    intakeStage2.setPower(-1);
                }

                // if power is -1 run intake backwards
                if (intakePower == -1) {
                    intake.setPower(-1);
                    intakeStage2.setPower(1);
                }

                // Checking the previous state so it does not fire multiple times
                // then toggles the intake power
                if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
                    if (intakePower != 1) {
                        intakePower = 1;
                    } else {
                        intakePower = 0;
                    }
                }

                // Checking the previous state so it does not fire multiple times
                // then toggles the intake power reverse
                if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
                    if (intakePower != -1) {
                        intakePower = -1;
                    } else {
                        intakePower = 0;
                    }
                }
                // endregion

                // endregion

                // region Telemetry
                telemetry.addData("dump", dump.getPosition());
                telemetry.addData("x", poseEstimate.getX());
                telemetry.addData("y", poseEstimate.getY());
                telemetry.addData("heading", poseEstimate.getHeading());
                telemetry.addData("slide position", leftSlide.getCurrentPosition());

                telemetry.update();
                // endregion
            }
        }
    }
}
