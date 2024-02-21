//package org.firstinspires.ftc.teamcode.myCode.autov1;
//
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.acmerobotics.roadrunner.geometry.Vector2d;
//import com.acmerobotics.roadrunner.trajectory.MarkerCallback;
//
//import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
//import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
//
//public class GetTrag {
//    private MarkerCallback intakeUpDisplace;
//    private MarkerCallback slideUpDisplace;
//    private MarkerCallback liftDisplace;
//    private MarkerCallback intakeDisplace;
//    private MarkerCallback intakeOffDisplace;
//    private Pose2d intPose;
//    private SampleMecanumDrive drive;
//    private int multipler = 1;
//    private double angleAdjust = 0;
//    private TrajectorySequence farMiddle;
//    private TrajectorySequence farLeft;
//    private TrajectorySequence farRight;
//
//    private TrajectorySequence closeMiddle;
//    private TrajectorySequence closeLeft;
//    private TrajectorySequence closeRight;
//
//
//    GetTrag(boolean isBlue, Pose2d intPose, SampleMecanumDrive drive, MarkerCallback intakeUpDisplace,
//            MarkerCallback slideUpDisplace, MarkerCallback liftDisplace, MarkerCallback intakeDisplace, MarkerCallback intakeOffDisplace) {
//
//
//        this.multipler = isBlue ? 1 : -1;
//        this.angleAdjust = isBlue ? 0 : Math.PI;
//        this.intPose = intPose;
//        this.drive = drive;
//        this.intakeUpDisplace = intakeUpDisplace;
//        this.slideUpDisplace = slideUpDisplace;
//        this.liftDisplace = liftDisplace;
//        this.intakeDisplace = intakeDisplace;
//        this.intakeOffDisplace = intakeOffDisplace;
//
//    }
//
//    public TrajectorySequence getFarMiddle() {
//        return drive.trajectorySequenceBuilder(intPose)
//                .lineTo(new Vector2d(-38.1, 33*multipler))
//                .addDisplacementMarker(intakeUpDisplace)
//                .lineTo(new Vector2d(-38.1, 36*multipler))
//                .splineToLinearHeading(new Pose2d(-53.19, 38*multipler,Math.toRadians(0)),Math.toRadians(-90))
//                .lineTo(new Vector2d(-53.19, 20*multipler))
//                .splineToConstantHeading(new Vector2d(0, 10*multipler),0)
//                .addDisplacementMarker(slideUpDisplace)
//                .splineToConstantHeading(new Vector2d(38, 15*multipler),0)
//                .splineToConstantHeading(new Vector2d(51.5, 35*multipler),0)
//                .build();
//    }
//
//    public TrajectorySequence getFarLeft() {
//        return drive.trajectorySequenceBuilder(intPose)
//
//                .lineTo(new Vector2d(-38.1, 50 * multipler))
//                .splineToLinearHeading(new Pose2d(-32, 35 * multipler, Math.toRadians(180)), Math.toRadians(0))
//                .addDisplacementMarker(intakeUpDisplace)
//                .splineToLinearHeading(new Pose2d(-41, 24 * multipler,Math.toRadians(270*multipler)),Math.toRadians(270*multipler))
//                .splineToLinearHeading(new Pose2d(-24, 12 * multipler,Math.toRadians(0)),0)
//                .splineToConstantHeading(new Vector2d(0, 10 * multipler),0)
//                .addDisplacementMarker(slideUpDisplace)
//                .splineToConstantHeading(new Vector2d(38, 15 * multipler),0)
//                .splineToConstantHeading(new Vector2d(51.5, 45 * multipler),0)
//                .build();
//    }
//
//    public TrajectorySequence getFarRight() {
//        return drive.trajectorySequenceBuilder(intPose)
//                .lineTo(new Vector2d(-38.1, 50 * multipler))
//                .splineToLinearHeading(new Pose2d(-36.1, 30 * multipler, Math.toRadians(0)), Math.toRadians(-90))
//                .addDisplacementMarker(intakeUpDisplace)
//                .splineToConstantHeading(new Vector2d(-35.1, 10 * multipler), 0)
//                .splineToConstantHeading(new Vector2d(0, 10 * multipler), 0)
//                .addDisplacementMarker(slideUpDisplace)
//                .splineToConstantHeading(new Vector2d(38, 15 * multipler), 0)
//                .splineToConstantHeading(new Vector2d(51.5, 29 * multipler), 0)
//                .build();
//    }
//
//    public TrajectorySequence getCloseMiddle() {
//        return drive.trajectorySequenceBuilder(intPose)
//                .lineTo(new Vector2d(10.2, 33*multipler))
//                .addDisplacementMarker(intakeUpDisplace)
//                .lineTo(new Vector2d(10.2, 40*multipler))
//                .addDisplacementMarker(slideUpDisplace)
//                .splineToLinearHeading(new Pose2d(45, 35.5*multipler, 0), 0)
//                .lineTo(new Vector2d(51, 34.5*multipler))
//                .build();
//    }
//
//    public TrajectorySequence getCloseLeft() {
//        return drive.trajectorySequenceBuilder(intPose)
//                .lineTo(new Vector2d(10.2, 55*multipler))
//                .lineTo(new Vector2d(25, 45*multipler))
//                .addDisplacementMarker(intakeUpDisplace)
//                .splineToLinearHeading(new Pose2d(32, 30*multipler, Math.toRadians(0)), Math.toRadians(180))
//                .addDisplacementMarker(slideUpDisplace)
//                .splineToConstantHeading(new Vector2d(51, 41*multipler), 0)
//                .build();
//    }
//
//    public TrajectorySequence getCloseRight() {
//        return drive.trajectorySequenceBuilder(intPose)
//                .lineTo(new Vector2d(10.2, 55*multipler))
//                .lineTo(new Vector2d(20, 45*multipler))
//                .splineToLinearHeading(new Pose2d(10, 30*multipler, Math.toRadians(0)), Math.toRadians(180))
//                .addDisplacementMarker(intakeUpDisplace)
//                .addDisplacementMarker(slideUpDisplace)
//                .splineToConstantHeading(new Vector2d(51, 28*multipler), 0)
//                .build();
//    }
//
//}
