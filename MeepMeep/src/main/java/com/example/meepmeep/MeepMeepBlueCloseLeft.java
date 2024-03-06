package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueCloseLeft extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true,false);
        sequence = action.actionBuilder(new Pose2d(0,0,0))
                        .setTangent(270)
                        .splineToConstantHeading(vector2dM(22, 42), angleConvert(270))
                        //intake up
                        //slide up
                        .splineToLinearHeading(pose2dM(54, 41, angleConvert(0)), angleConvert(0))
                        //drop pixels
                        .build();
        other();
    }
}