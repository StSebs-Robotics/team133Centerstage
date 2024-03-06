package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class GetPixelUnderTruss extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true,false);
        sequence = action.actionBuilder(new Pose2d(0,0,0))
                //.back(2)
                .splineToConstantHeading(vector2dM(30, 52), angleConvert(110))
                .splineToConstantHeading(vector2dM(0, 62), angleConvert(180))
//                        .splineToConstantHeading(vector2dM(-24, 59), angleConvert(180))
//                        .splineToConstantHeading(vector2dM(-52, 50), angleConvert(220))
//                        .splineToConstantHeading(vector2dM(-57, 35), angleConvert(270))
                .build();
        other();
    }
}