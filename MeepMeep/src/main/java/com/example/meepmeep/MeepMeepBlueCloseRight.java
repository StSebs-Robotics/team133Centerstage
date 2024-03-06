package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueCloseRight extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true,false);
        sequence = action.actionBuilder(new Pose2d(0,0,0))
                        .setTangent(angleConvert(270))
                        .splineToConstantHeading(vector2dM(12, 45), angleConvert(270))
                        .splineTo(vector2dM(7, 38), angleConvert(225))
                        //intake up
                        //slides up
                        .strafeToSplineHeading(vector2dM(20, 30), angleConvert(0))
                        .splineToConstantHeading(vector2dM(54, 28), angleConvert(0))
                        //drop pixels
                        .build();
        other();
    }
}