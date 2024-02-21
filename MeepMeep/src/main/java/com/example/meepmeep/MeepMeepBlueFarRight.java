package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueFarRight extends MeepMeepBaseClass {
    public static void main(String[] args) {
        sequence = drive ->
                //drive.trajectorySequenceBuilder(pose2dM(-38.1,61, angleConvert(90)))
                drive.trajectorySequenceBuilder(pose2dM(-33, -61, angleConvert(-90)))
                        .lineTo(vector2dM(-38.1, 50))
                        .splineToLinearHeading(pose2dM(-32, 35, angleConvert(180)), angleConvert(0))
                        .splineToLinearHeading(pose2dM(-41, 24, angleConvert(270)), angleConvert(270))
                        .splineToLinearHeading(pose2dM(-24, 12, angleConvert(0)), 0)
                        .splineToConstantHeading(vector2dM(0, 10), 0)
                        .splineToConstantHeading(vector2dM(38, 15), 0)
                        .splineToConstantHeading(vector2dM(47, 40), 0)
                        .build();
        other();
    }
}