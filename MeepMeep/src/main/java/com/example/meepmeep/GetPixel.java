package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class GetPixel extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true,false);
        sequence = action.actionBuilder(pose2dM(52.5,28,angleConvert(0)))
//                        .lineTo( vector2dM(39,23))
//                        .splineToConstantHeading(vector2dM(32, 10.6), angleConvert(200))
//                        .splineToConstantHeading(vector2dM(1,15),angleConvert(180))
//                        .splineToConstantHeading(vector2dM(-58,17.5),angleConvert(180))
//                        //John New get pixel path
                        .lineToX(48)
                        .splineToConstantHeading(vector2dM(26,13), angleConvert(180))
                        .splineToConstantHeading(vector2dM(-58.75,17.5),angleConvert(180))
                        //Intake
                        .build();
        other();
    }
}