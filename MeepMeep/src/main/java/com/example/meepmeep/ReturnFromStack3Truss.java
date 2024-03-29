package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class ReturnFromStack3Truss extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true);
        sequence = drive ->
                drive.trajectorySequenceBuilder(pose2dM(-57, 35, angleConvert(0)))
                        .forward(3)
                        .splineToConstantHeading(vector2dM(-52, 48), angleConvert(45))
                        .splineToConstantHeading(vector2dM(-23, 60), angleConvert(0))
                        .splineToConstantHeading(vector2dM(0, 60), angleConvert(0))
                        .splineToConstantHeading(vector2dM(40, 47), angleConvert(0))
                        .build();
        other();
    }
}