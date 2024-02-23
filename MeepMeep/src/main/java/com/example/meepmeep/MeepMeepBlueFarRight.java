package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueFarRight extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true);
        sequence = drive ->
                drive.trajectorySequenceBuilder(pose2dM(-33, 61, angleConvert(90)))
                        .lineTo(vector2dM(-38.1, 33))
                        .splineToConstantHeading(vector2dM(-53.19, 38), 0)
                        .splineToLinearHeading(pose2dM(-53.19, 20, angleConvert(0)), 0)
                        .splineToConstantHeading(vector2dM(0, 10), 0)
                        .splineToConstantHeading(vector2dM(38, 15), 0)
                        .splineToConstantHeading(vector2dM(47, 34), 0)
                        .build();
        other();
    }
}