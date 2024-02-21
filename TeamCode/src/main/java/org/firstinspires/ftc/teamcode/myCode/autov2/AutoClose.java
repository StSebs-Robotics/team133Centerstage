//package org.firstinspires.ftc.teamcode.myCode.autov2;
//
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.acmerobotics.roadrunner.geometry.Vector2d;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//
//import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
//
//@Autonomous(name = "AutoV2CloseBlue", group = "2023-2024")
//@Disabled
//public class AutoClose extends AutoBaseClass {
//
//    @Override
//    protected void returnLocation(double position) {
//        telemetry.addData("Blue Close Class","");
//        if (position > 300) {
//            whereIsTeamElement = "close";
//            telemetry.addData("WE ARE close to the beams", " ");
//        } else if (position >= 0) {
//            whereIsTeamElement = "middle";
//            telemetry.addData("WE ARE AT THE Middle", " ");
//        } else {
//            whereIsTeamElement = "far";
//            telemetry.addData("WE ARE far from the beams I HOPE BECAUSE", "THIS IS A GUESS BECAUSE WE CAN'T SEE IT");
//        }
//        telemetry.update();
//    }
//
//    @Override
//    public void runOpMode() {
//        super.runOpMode();
//    }
//
//    @Override
//    protected void setVariables() {
//        intPose = new Pose2d(12, 61, Math.toRadians(90));
//        multiplier = 1;
//    }
//
//    @Override
//    protected void setupTrajectories() {
//        telemetry.addData("pos", multiplier);
//        dropOffPose = new Pose2d(40, 25*multiplier, Math.toRadians(0));
//        drive = new SampleMecanumDrive(hardwareMap);
//
//
//        trajectoryFarBeam = drive.trajectorySequenceBuilder(intPose)
//                .lineTo(new Vector2d(10.2, 55 * multiplier))
//                .lineTo(new Vector2d(25, 45 * multiplier))
//                .addDisplacementMarker(intakeUp)
//                .splineToLinearHeading(new Pose2d(32, 30 * multiplier, Math.toRadians(0)), Math.toRadians(180))
//                .addDisplacementMarker(slideUp)
//                .splineToConstantHeading(new Vector2d(52.5, 41 * multiplier), 0)
//                .build();
//        trajectoryMiddleBeam = drive.trajectorySequenceBuilder(intPose)
//                .lineTo(new Vector2d(10.2, 33.5 * multiplier))
//                .addDisplacementMarker(intakeUp)
//                .lineTo(new Vector2d(10.2, 40 * multiplier))
//                .addDisplacementMarker(slideUp)
//                .splineToLinearHeading(new Pose2d(52.5, 33 * multiplier, 0), 0)
//                 // .lineTo(new Vector2d(51, 34.5*multiplier))
//                .build();
//         trajectoryCloseBeam = drive.trajectorySequenceBuilder(intPose)
//                .lineTo(new Vector2d(10.2, 55 * multiplier))
//                .lineTo(new Vector2d(20, 45 * multiplier))
//                .splineToLinearHeading(new Pose2d(10, 30 * multiplier, Math.toRadians(0)), Math.toRadians(180))
//                .addDisplacementMarker(intakeUp)
//                .addDisplacementMarker(slideUp)
//                .splineToConstantHeading(new Vector2d(52.5, 28 * multiplier), 0)
//                .build();
//        trajectoryGoToPile = drive.trajectorySequenceBuilder(dropOffPose)
//                .lineTo( new Vector2d(39,23 * multiplier))
//                .splineToConstantHeading(new Vector2d(32, 10.6 * multiplier), Math.toRadians(200))
//                .splineToConstantHeading(new Vector2d(1,15 * multiplier),Math.toRadians(180))
//                .splineToConstantHeading(new Vector2d(-58,(17.5 * multiplier) + (multiplier == 1 ? 0 : -.5) ),Math.toRadians(180))
//                //Intake
//                .build();
//        trajectoryDropOffPixels = drive.trajectorySequenceBuilder(trajectoryGoToPile.end())
//                .lineTo(new Vector2d(-52,11 * multiplier))
//                .splineToConstantHeading(new Vector2d(32,11.5 * multiplier),Math.toRadians(0))
//                .addDisplacementMarker(slideUp2)
//                .splineToConstantHeading(new Vector2d(52.5, 29 * multiplier),Math.toRadians(0))
//                .build();
//        trajectoryPark = drive.trajectorySequenceBuilder(dropOffPose)
//                .lineTo( new Vector2d(40,30*multiplier))
//                .splineToConstantHeading(new Vector2d(47, 60*multiplier), Math.toRadians(45))
//                .splineToConstantHeading(new Vector2d(58, 62*multiplier), Math.toRadians(0)).build();
//    }
//}
