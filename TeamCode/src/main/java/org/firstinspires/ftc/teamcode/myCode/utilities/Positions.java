package org.firstinspires.ftc.teamcode.myCode.utilities;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

@Config
public class Positions {
    public Pose2d leftParkPos;
    public Pose2d rightParkPos;
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
        double multipler = mirror ? -1 :1;
        leftParkPos = new Pose2d();
        rightParkPos = new Pose2d();
        stack1Pos = new Pose2d();
        stack3Pos = new Pose2d();
        dropClosePos = new Pose2d();
        dropFarPos = new Pose2d();
        dropMiddlePos = new Pose2d();
        dropOtherClosePos = new Pose2d();
        dropOtherFarPos = new Pose2d();
        dropOtherMiddlePos = new Pose2d();
        centerPos = new Pose2d();

        startRedFarPos = new Pose2d(-38.1,61);
        startRedClosePos = new Pose2d(15.75, -61);
        startBlueFarPos = new Pose2d(-33,-61);
        startBlueClosePos = new Pose2d(40,25);
    }
}
