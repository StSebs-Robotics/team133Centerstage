package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class GetPixelUnderTruss {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(40, 25, Math.toRadians(0)))
                                .lineTo(new Vector2d(39,28))
                                .splineToConstantHeading(new Vector2d(30,52),Math.toRadians(110))
                                .splineToConstantHeading(new Vector2d(0,60),Math.toRadians(180))
                                .splineToConstantHeading(new Vector2d(-24,60),Math.toRadians(180))
                                .splineToConstantHeading(new Vector2d(-47,48),Math.toRadians(220))
                                .splineToConstantHeading(new Vector2d(-57,35),Math.toRadians(225))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}