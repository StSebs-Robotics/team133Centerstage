//package org.firstinspires.ftc.teamcode.myCode.autov2;
//
//import com.acmerobotics.roadrunner.geometry.Pose2d;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.Disabled;
//
//@Autonomous(name = "AutoV2CloseRed", group = "2023-2024")
//@Disabled
//public class AutoCloseRed extends AutoClose {
//
//    @Override
//    protected void returnLocation(double position) {
//        telemetry.addData("Blue Close Class","");
//        if (position > 300) {
//            whereIsTeamElement = "far";
//            telemetry.addData("WE ARE far to the beams", " ");
//        } else if (position >= 0) {
//            whereIsTeamElement = "middle";
//            telemetry.addData("WE ARE AT THE Middle", " ");
//        } else {
//            whereIsTeamElement = "close";
//            telemetry.addData("WE ARE close from the beams I HOPE BECAUSE", "THIS IS A GUESS BECAUSE WE CAN'T SEE IT");
//        }
//        telemetry.update();
//    }
//
//    @Override
//    protected  void setVariables() {
//        intPose = new Pose2d(15.75, -61, Math.toRadians(-90));
//        multiplier = -1;
//    }
//
//    @Override
//    public void runOpMode() {
//        super.runOpMode();
//    }
//
//    @Override
//    protected void setupTrajectories() {
//        super.setupTrajectories();
//    }
//}
