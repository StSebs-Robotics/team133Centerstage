package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class ReturnFromStack3Gate extends MeepMeepBaseClass {
    public static void main(String[] args) {
        sequence = drive ->
                drive.trajectorySequenceBuilder(pose2dM(-58, 34, angleConvert(0)))
                        .setTangent(0)
                        .splineToConstantHeading(vector2dM(-52, 14), angleConvert(340))
                        .splineToConstantHeading(vector2dM(0, 10), angleConvert(0))
                        .splineToConstantHeading(vector2dM(32, 11.5), angleConvert(0))
                        .splineToConstantHeading(vector2dM(47, 28), angleConvert(0))
                        .build();
        other();
    }
}