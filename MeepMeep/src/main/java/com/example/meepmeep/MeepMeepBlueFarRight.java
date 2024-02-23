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
                //drive.trajectorySequenceBuilder(pose2dM(-38.1,61, angleConvert(90)))
                drive.trajectorySequenceBuilder(pose2dM(-33, 61, angleConvert(90)))
                        .setTangent(180)
                        .splineToLinearHeading(pose2dM(-34,30, angleConvert(180)),angleConvert(0))
                        //intake up
                        .setTangent(angleConvert(179))
                        .splineToLinearHeading(pose2dM(-56.2, 32.5, angleConvert(0)), angleConvert(180))
                        .splineToConstantHeading(vector2dM(-55,23),angleConvert(0))
                        //intake one

//                        .splineToLinearHeading(pose2dM(-24, 12, angleConvert(0)), 0)
//                        .splineToConstantHeading(vector2dM(0, 10), 0)
//                        .splineToConstantHeading(vector2dM(38, 15), 0)
//                        .splineToConstantHeading(vector2dM(47, 40), 0)
                        .build();
        other();
    }
}