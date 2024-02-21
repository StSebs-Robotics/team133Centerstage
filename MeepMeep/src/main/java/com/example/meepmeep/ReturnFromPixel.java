package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class ReturnFromPixel extends MeepMeepBaseClass {
    public static void main(String[] args) {
        sequence = drive ->
                drive.trajectorySequenceBuilder(pose2dM(-57, 17.5, angleConvert(0)))
                        .setTangent(0)
                        .splineToConstantHeading(vector2dM(-34, 13), angleConvert(350))
                        .splineToConstantHeading(vector2dM(0, 13), angleConvert(0))
                        .splineToConstantHeading(vector2dM(36, 13), angleConvert(0))
                        .splineToConstantHeading(vector2dM(54, 28), angleConvert(45))
                        .build();
        other();
    }
}
