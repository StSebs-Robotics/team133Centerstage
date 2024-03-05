package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class GetPixel extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true);
        sequence = drive ->
                //Other start positions: 28, 39.5, 33
                drive.trajectorySequenceBuilder(pose2dM(54, 33,0))
//                        .lineTo( vector2dM(39,23))
//                        .splineToConstantHeading(vector2dM(32, 10.6), angleConvert(200))
//                        .splineToConstantHeading(vector2dM(1,15),angleConvert(180))
//                        .splineToConstantHeading(vector2dM(-58,17.5),angleConvert(180))
//                        //John New get pixel path
                        .back(2)
                        .splineToConstantHeading(vector2dM(26,13), angleConvert(180))
                        .splineToConstantHeading(vector2dM(-58.75,17.5),angleConvert(180))
                        //Intake
                        .build();
        other();
    }
}