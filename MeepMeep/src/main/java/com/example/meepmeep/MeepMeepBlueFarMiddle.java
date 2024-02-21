package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueFarMiddle extends MeepMeepBaseClass {
    public static void main(String[] args) {
        sequence = drive ->
                drive.trajectorySequenceBuilder(pose2dM(-38.1, 61, angleConvert(90)))
                        .splineTo(vector2dM(-38.1, 33), 0)
                        .splineTo(vector2dM(-38.1, 36), 0)
                        .splineToLinearHeading(pose2dM(-56.2, 32.5, angleConvert(0)), angleConvert(-90))
                        //Intake 1
                        .splineToConstantHeading(vector2dM(-46, 13), 0)

//                                .lineTo(vector2dM(-53.19, 14))
//                                .splineToConstantHeading(vector2dM(0, 10),0)
//                                .splineToConstantHeading(vector2dM(38, 15),0)
//                                .splineToConstantHeading(vector2dM(47, 34),0)
                        .build();
        other();
    }
}