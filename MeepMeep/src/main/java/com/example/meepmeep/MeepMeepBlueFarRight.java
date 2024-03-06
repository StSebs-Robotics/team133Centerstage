package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueFarRight extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true,false);
        sequence = action.actionBuilder()
                        .setTangent(angleConvert(270))
                        .splineToConstantHeading(vector2dM(-47,42),angleConvert(200))
                        //intake up
                        //.splineToLinearHeading(pose2dM(-52,44,angleConvert(45)),angleConvert(200))
                        //.strafeLeft(5)
                        .splineToLinearHeading(pose2dM(-58,36,angleConvert(0)),angleConvert(250))
                        //intake 1
                        .setTangent(angleConvert(270))
                        .splineToConstantHeading(vector2dM(-46, 13), 0)
                        .splineToConstantHeading(vector2dM(0,13),0)
                        //slide up
                       // .addTemporalMarker(slideUp)
                        .splineToConstantHeading(vector2dM(24,13),0)
                        .splineToConstantHeading(vector2dM(54,28),0)
                        //drop 1 pixel
                       // .addTemporalMarker(drop1Pixel)
                        .setTangent(angleConvert(90))
                        .splineToConstantHeading(vector2dM(54,33),angleConvert(90))
                        //drop other pixel
                        //.addTemporalMarker(dropPixels)
                        .build();
        other();
    }
}