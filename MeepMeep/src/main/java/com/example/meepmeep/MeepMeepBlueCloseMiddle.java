package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBlueCloseMiddle {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        double multiplier = 1;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                                drive.trajectorySequenceBuilder(new Pose2d(10.2,61, Math.toRadians(90)))
                                        .setTangent(Math.toRadians(270))
                                        .splineTo(new Vector2d(10.2,35 * multiplier), Math.toRadians(270))
                                        .lineToSplineHeading(new Pose2d(30, 33 * multiplier, Math.toRadians(0)))
                                        .splineToConstantHeading(new Vector2d(54,33),0)
                                        .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}