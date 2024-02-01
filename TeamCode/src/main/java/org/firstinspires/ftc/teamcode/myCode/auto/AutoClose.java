package org.firstinspires.ftc.teamcode.myCode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

@Autonomous(name = "AutoV2CloseBlue", group = "2023-2024")
public class AutoClose extends AutoBaseClass {
    protected double multiplier = 1;

    @Override
    protected void returnLocation(double position) {
        telemetry.addData("Blue Close Class","");
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
        telemetry.update();
    }

    @Override
    public void runOpMode() {
        super.runOpMode();
    }

    @Override
    protected void setupTrajectories() {
        intPose = new Pose2d(12, 61, Math.toRadians(90));
        dropOffPose = new Pose2d(40, 25, Math.toRadians(0));
        drive = new SampleMecanumDrive(hardwareMap);


        trajectoryFarBeam = drive.trajectorySequenceBuilder(intPose)
                .lineTo(new Vector2d(10.2, 55 * multiplier))
                .lineTo(new Vector2d(25, 45 * multiplier))
                .addDisplacementMarker(intakeUp)
                .splineToLinearHeading(new Pose2d(32, 30 * multiplier, Math.toRadians(0)), Math.toRadians(180))
                .addDisplacementMarker(slideUp)
                .splineToConstantHeading(new Vector2d(52.5, 41 * multiplier), 0)
                .build();
        trajectoryMiddleBeam = drive.trajectorySequenceBuilder(intPose)
                .lineTo(new Vector2d(10.2, 34 * multiplier))
                .addDisplacementMarker(intakeUp)
                .lineTo(new Vector2d(10.2, 40 * multiplier))
                .addDisplacementMarker(slideUp)
                .splineToLinearHeading(new Pose2d(52.5, 33 * multiplier, 0), 0)
                // .lineTo(new Vector2d(51, 34.5*multiplier))
                .build();
         trajectoryCloseBeam = drive.trajectorySequenceBuilder(intPose)
                .lineTo(new Vector2d(10.2, 55 * multiplier))
                .lineTo(new Vector2d(20, 45 * multiplier))
                .splineToLinearHeading(new Pose2d(10, 30 * multiplier, Math.toRadians(0)), Math.toRadians(180))
                .addDisplacementMarker(intakeUp)
                .addDisplacementMarker(slideUp)
                .splineToConstantHeading(new Vector2d(52.5, 28 * multiplier), 0)
                .build();
        trajectoryGoToPile = drive.trajectorySequenceBuilder(dropOffPose)
                .lineTo(new Vector2d(12, 60))
                .splineToConstantHeading(new Vector2d(11.61, 33.73), Math.toRadians(270.00))
                .splineToConstantHeading(new Vector2d(11.61, 42.44), Math.toRadians(90.00))
                .splineTo(new Vector2d(27.57, 37.18), Math.toRadians(7.57))
                .splineToLinearHeading(new Pose2d(50.78, 36.82, Math.toRadians(0.00)), Math.toRadians(0.00))
                .build();
        trajectoryDropOffPixels = drive.trajectorySequenceBuilder(trajectoryGoToPile.end())
                .lineTo(new Vector2d(-40, 50 * multiplier))
                .build();
    }
}
