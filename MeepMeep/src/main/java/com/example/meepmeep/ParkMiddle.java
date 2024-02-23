package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class ParkMiddle extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(false);
        sequence = drive ->
                //other start positions : 28, 33, 41
                drive.trajectorySequenceBuilder(pose2dM(52.5, 41, angleConvert(0)))
                        .back(2)
                        .lineTo(vector2dM(51, 16))
                        //Have the option to use lines or splines (I think Lines are faster)
                        //.splineToConstantHeading(vector2dM(45.5,18 * multiplier),angleConvert(300))
                        .splineToConstantHeading(vector2dM(65, 12), angleConvert(350))
                        .build();
        other();
    }
}