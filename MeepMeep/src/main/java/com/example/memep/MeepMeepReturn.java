package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepReturn {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-35.91, -65.29, Math.toRadians(90.00)))
                                .splineToLinearHeading(new Pose2d(-34.10, -29.56, Math.toRadians(0.00)), Math.toRadians(0.00))
                                .splineToConstantHeading(new Vector2d(-35.73, -12.15), Math.toRadians(2.16))
                                .splineToConstantHeading(new Vector2d(30.29, -11.43), Math.toRadians(2.12))
                                .splineToConstantHeading(new Vector2d(50.96, -36.27), Math.toRadians(-2.08))
                                //Dump
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}