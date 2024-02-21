package org.firstinspires.ftc.teamcode.myCode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

@Config
public class Positions {
    public Pose2d wallParkPos;
    public Pose2d middleParkPos;
    public Pose2d stack1Pos;
    public Pose2d stack3Pos;
    public Pose2d dropClosePos;
    public Pose2d dropFarPos;
    public Pose2d dropMiddlePos;
    public Pose2d dropOtherClosePos;
    public Pose2d dropOtherFarPos;
    public Pose2d dropOtherMiddlePos;
    public Pose2d centerPos;
    public Pose2d startRedFarPos;
    public Pose2d startRedClosePos;
    public Pose2d startBlueFarPos;
    public Pose2d startBlueClosePos;

    public Positions(boolean mirror) {
        double multiplier = mirror ? -1 : 1;
        double dropX = 54;
        wallParkPos = new Pose2d(60, 62 * multiplier, Math.toRadians(0));
        middleParkPos = new Pose2d(60,12 * multiplier, Math.toRadians(0));

        stack1Pos = new Pose2d(-56.2,32.5* multiplier, Math.toRadians(180));
        stack3Pos = new Pose2d(-58.75,17.5* multiplier, Math.toRadians(180));

        dropClosePos = new Pose2d(dropX, 28 * multiplier, Math.toRadians(0));
        dropFarPos = new Pose2d(dropX, 41 * multiplier, Math.toRadians(0));
        dropMiddlePos = new Pose2d(dropX, 33 * multiplier, Math.toRadians(0));
        //these are where we drop the white pixels we might not use this
        dropOtherClosePos = new Pose2d();
        dropOtherFarPos = new Pose2d();
        dropOtherMiddlePos = new Pose2d();
        centerPos = new Pose2d();

        startRedFarPos = new Pose2d(-38.1, -61, Math.toRadians(-90));
        startRedClosePos = new Pose2d(15.75, -61, Math.toRadians(-90));

        startBlueFarPos = new Pose2d(-33, 61 * multiplier, Math.toRadians(90));
        startBlueClosePos = new Pose2d(12, 61 * multiplier, Math.toRadians(90));

    }
}
