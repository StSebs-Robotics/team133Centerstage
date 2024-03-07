package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueFarLeft extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true,false);
        sequence = action.actionBuilder(new Pose2d(-38.1,61,angleConvert(90)))

                        .setTangent(angleConvert(270))
                        .splineToLinearHeading(pose2dM(-34,30, angleConvert(180)),angleConvert(0))
                        //intake up

                        .setTangent(angleConvert(179))
                                .splineToLinearHeading(pose2dM(-56.2, 32.5, angleConvert(0)), angleConvert(180))

                        //intake one
                        .setTangent(angleConvert(300))
                        //.splineToConstantHeading(vector2dM(-55,23),angleConvert(280))
                        .splineToConstantHeading(vector2dM(-16,13),angleConvert(0))
                        .splineToConstantHeading(vector2dM(0,13),angleConvert(0))
                        //slide up
                        .splineToConstantHeading(vector2dM(24,13),0)
                        .splineToConstantHeading(vector2dM(54,33),0)
                        //drop 1 pixel
                        .setTangent(angleConvert(90))
                        .splineToConstantHeading(vector2dM(54,41),angleConvert(90))

                        .build();


        other();
    }
}
