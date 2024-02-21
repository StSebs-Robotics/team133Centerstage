package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class GetPixelUnderTruss {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        double multiplier = -1;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                      //Other start positions: 28, 33, 41
                        drive.trajectorySequenceBuilder(new Pose2d(52.5, 28, Math.toRadians(0)))
                                .back(2)
                                .splineToConstantHeading(new Vector2d(30,52),Math.toRadians(110))
                                .splineToConstantHeading(new Vector2d(0,59),Math.toRadians(180))
                                .splineToConstantHeading(new Vector2d(-24,59),Math.toRadians(180))
                                .splineToConstantHeading(new Vector2d(-52,50),Math.toRadians(220))
                                .splineToConstantHeading(new Vector2d(-57,35),Math.toRadians(270))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}