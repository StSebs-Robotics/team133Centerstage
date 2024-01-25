package org.firstinspires.ftc.teamcode.myCode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
@Autonomous(name = "AutoV2CloseBlue", group = "2023-2024")
public class AutoClose extends AutoBaseClass{
    protected double multiplier = 1;
    @Override
    protected void returnLocation(double position) {
        if (position > 300) {
            whereIsTeamElement = "far";
            telemetry.addData("WE ARE Far to the beams", " ");
        } else if (position >= 0) {
            whereIsTeamElement = "middle";
            telemetry.addData("WE ARE AT THE Middle", " ");
        } else {
            whereIsTeamElement = "close";
            telemetry.addData("WE ARE Close from the beams I HOPE BECAUSE", "THIS IS A GUESS BECAUSE WE CAN'T SEE IT");
        }
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

        trajectoryCloseBeam = drive.trajectorySequenceBuilder(intPose)
                .lineTo(new Vector2d(10.2, 55*multipler))
                .lineTo(new Vector2d(25, 45*multipler))
                .addDisplacementMarker(intakeUp)
                .splineToLinearHeading(new Pose2d(32, 30*multipler, Math.toRadians(0)), Math.toRadians(180))
                .addDisplacementMarker(slideUp)
                .splineToConstantHeading(new Vector2d(51, 41*multipler), 0)
                .build();
        trajectoryMiddleBeam = drive.trajectorySequenceBuilder(intPose)
                .lineTo(new Vector2d(10.2, 34*multipler))
                .addDisplacementMarker(intakeUp)
                .lineTo(new Vector2d(10.2, 40*multipler))
                .addDisplacementMarker(slideUp)
                .splineToLinearHeading(new Pose2d(51, 34.5*multipler, 0), 0)
                //.lineTo(new Vector2d(51, 34.5*multipler))
                .build();
        trajectoryFarBeam = drive.trajectorySequenceBuilder(intPose)
                .lineTo(new Vector2d(10.2, 55*multipler))
                .lineTo(new Vector2d(20, 45*multipler))
                .splineToLinearHeading(new Pose2d(10, 30*multipler, Math.toRadians(0)), Math.toRadians(180))
                .addDisplacementMarker(intakeUp)
                .addDisplacementMarker(slideUp)
                .splineToConstantHeading(new Vector2d(51, 28*multipler), 0)
                .build();
        trajectoryGoToPile = drive.trajectorySequenceBuilder(dropOffPose)
                .lineTo(new Vector2d(-39, 50 * multipler))
                .build();
        trajectoryDropOffPixels = drive.trajectorySequenceBuilder(trajectoryGoToPile.end())
                .lineTo(new Vector2d(-40, 50 * multipler))
                .build();
    }
}
