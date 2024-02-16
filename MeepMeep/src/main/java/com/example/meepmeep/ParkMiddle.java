package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class ParkMiddle {
    protected int multiplier = 1;
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        //other start positions : 28, 33, 41
                        drive.trajectorySequenceBuilder(new Pose2d(52.5, 41, Math.toRadians(0)))
                                .back(2)
                                .lineTo(new Vector2d (51,16 ))
                                //Have the option to use lines or splines (I think Lines are faster)
                                //.splineToConstantHeading(new Vector2d(45.5,18 * multiplier),Math.toRadians(300))
                                .splineToConstantHeading(new Vector2d(65,12 ),Math.toRadians(350))
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}