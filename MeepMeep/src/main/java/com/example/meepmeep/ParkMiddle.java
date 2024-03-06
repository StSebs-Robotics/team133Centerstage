package com.example.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class ParkMiddle extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(false, false);
        sequence = action.actionBuilder(new Pose2d(0,0,0))
                //.back(2)
                .strafeTo(vector2dM(51, 16))
                //Have the option to use lines or splines (I think Lines are faster)
                //.splineToConstantHeading(vector2dM(45.5,18 * multiplier),angleConvert(300))
                .splineToConstantHeading(vector2dM(65, 12), angleConvert(350))
                .build();
        other();
    }
}