package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;

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
    public boolean isBlue;

    private double angleConvert(double angle) {
        return Math.toRadians(angle * (isBlue ? 1 : -1));
    }

    private Vector2d vector2dM(double x, double y) {
        return new Vector2d(x, y * (isBlue ? 1 : -1));
    }

    private Pose2d pose2dM(double x, double y, double angle) {
        return new Pose2d(x, y * (isBlue ? 1 : -1), angle);
    }

    public Positions(boolean mirror) {
        isBlue = !mirror;
        double multiplier = mirror ? -1 : 1;
        double dropX = 54;
        wallParkPos = pose2dM(60, 62 * multiplier, angleConvert(0));
        middleParkPos = pose2dM(60,12 * multiplier, angleConvert(0));

        stack1Pos = pose2dM(-56.2,32.5* multiplier, angleConvert(180));
        stack3Pos = pose2dM(-58.75,17.5* multiplier, angleConvert(180));

        dropClosePos = pose2dM(dropX, 28 * multiplier, angleConvert(0));
        dropFarPos = pose2dM(dropX, 41 * multiplier, angleConvert(0));
        dropMiddlePos = pose2dM(dropX, 33 * multiplier, angleConvert(0));
        //these are where we drop the white pixels we might not use this
        dropOtherClosePos = pose2dM(0,0,0);
        dropOtherFarPos = pose2dM(0,0,0);
        dropOtherMiddlePos = pose2dM(0,0,0);

        centerPos = pose2dM(0,0,0);

        //these ones don't get flipped
        startRedFarPos = new Pose2d(-38.1, -61, Math.toRadians(-90));
        startRedClosePos = new Pose2d(15.75, -61, Math.toRadians(-90));
        startBlueFarPos = new Pose2d(-33, 61 * multiplier, Math.toRadians(90));
        startBlueClosePos = new Pose2d(12, 61 * multiplier, Math.toRadians(90));

    }
}
