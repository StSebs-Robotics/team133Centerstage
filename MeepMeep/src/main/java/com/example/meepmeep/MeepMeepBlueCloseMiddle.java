package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueCloseMiddle extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true);
        sequence = drive ->
                drive.trajectorySequenceBuilder(pose2dM(10.2, 61, angleConvert(90)))
                        .setTangent(angleConvert(270))
                        .splineTo(vector2dM(10.2, 35), angleConvert(270))
                        //intake up
                        //slides up
                        .setTangent(0)
                        .splineToLinearHeading(pose2dM(54, 33, angleConvert(0)),angleConvert(0))
                        //Drop pixels
                        .build();
        other();
    }
}