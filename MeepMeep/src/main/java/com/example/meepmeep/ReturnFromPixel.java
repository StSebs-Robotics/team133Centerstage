package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class ReturnFromPixel extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true, false);
        sequence = action.actionBuilder(new Pose2d(0,0,0))
                .setTangent(0)
                .splineToConstantHeading(vector2dM(-34, 13), angleConvert(350))
                .splineToConstantHeading(vector2dM(0, 13), angleConvert(0))
                //slide up 2
                .splineToConstantHeading(vector2dM(18, 13), angleConvert(0))
                .splineToConstantHeading(vector2dM(54, 28), angleConvert(0))
                .build();
        other();
    }
}
