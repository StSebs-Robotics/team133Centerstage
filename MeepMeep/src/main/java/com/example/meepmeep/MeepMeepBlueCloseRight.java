package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueCloseRight extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true);
        sequence = drive ->
                drive.trajectorySequenceBuilder(pose2dM(10.2, 61, angleConvert(90)))
                        .setTangent(angleConvert(270))
                        .splineTo(vector2dM(10, 45), angleConvert(270))
                        .splineTo(vector2dM(7, 38), angleConvert(225))
                        .lineToSplineHeading(pose2dM(20, 30, angleConvert(0)))
                        .splineToConstantHeading(vector2dM(54, 28), angleConvert(0))
                        .build();
        other();
    }
}