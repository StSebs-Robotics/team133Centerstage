package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class Park extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true);
        sequence = drive ->
                //Start Positions: 28, 33, 41
                drive.trajectorySequenceBuilder(new Pose2d(52.5, 41, 0))
                        .back(2)
                        .lineTo(vector2dM(50.5, 53))
                        .splineToConstantHeading(vector2dM(60, 56), angleConvert(0))
                        //.splineToConstantHeading(vector2dM(58, 62), angleConvert(0))
                        .build();
        other();
    }
}