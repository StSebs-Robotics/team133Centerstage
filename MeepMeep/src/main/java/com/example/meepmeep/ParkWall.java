package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;

public class ParkWall extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true, false);
        sequence = action.actionBuilder(pose2dM(52.5,28,angleConvert(0)))
                .lineToX(48)
                .setTangent(angleConvert(90))
                .splineToConstantHeading(vector2dM(48, 53),angleConvert(90))
                .splineToConstantHeading(vector2dM(60, 56), angleConvert(0))
                .build();
        other();
    }
}