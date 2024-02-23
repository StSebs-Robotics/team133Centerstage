package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueFarMiddle extends MeepMeepBaseClass {
    public static void main(String[] args) {
        other1(true);
        sequence = drive ->
                drive.trajectorySequenceBuilder(pose2dM(-38.1, 61, angleConvert(90)))
                        .setTangent(angleConvert(270))
                        .splineTo(vector2dM(-38.1,34  ),angleConvert(270))
                        .setTangent(angleConvert(180))
                        //intake up
                        .forward(3)
                        .splineToLinearHeading(pose2dM(-56.2, 32.5, angleConvert(0)), angleConvert(180))
                        //Intake 1
                        .setTangent(angleConvert(270))
                        .splineToConstantHeading(vector2dM(-46, 13), 0)
                        .splineToConstantHeading(vector2dM(0,13),0)
                        //slide up
                        .splineToConstantHeading(vector2dM(24,13),0)
                        .splineToConstantHeading(vector2dM(54,33),0)
                        //drop pixels
                        .build();
        other();
    }
}