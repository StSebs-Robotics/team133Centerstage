package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class ParkMiddle extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true, false);
        sequence = action.actionBuilder(pose2dM(52.25,33,angleConvert(0)))
                .lineToX(48)
                .splineToConstantHeading(vector2dM(48,16), angleConvert(0))
                .splineToConstantHeading(vector2dM(58, 16), angleConvert(0))
                .build();
        other();
    }
}