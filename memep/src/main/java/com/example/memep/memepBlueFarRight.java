package com.example.memep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class memepBlueFarRight {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        double multipler = -1;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        //drive.trajectorySequenceBuilder(new Pose2d(-38.1,61, Math.toRadians(90)))
                        drive.trajectorySequenceBuilder(new Pose2d(-33, -61, Math.toRadians(-90)))
                                .lineTo(new Vector2d(-38.1, 50 * multipler))
                                .splineToLinearHeading(new Pose2d(-32, 35 * multipler, Math.toRadians(180)), Math.toRadians(0))
                                .splineToLinearHeading(new Pose2d(-41, 24 * multipler,Math.toRadians(270*multipler)),Math.toRadians(270*multipler))
                                .splineToLinearHeading(new Pose2d(-24, 12 * multipler,Math.toRadians(0)),0)
                                .splineToConstantHeading(new Vector2d(0, 10 * multipler),0)
                                .splineToConstantHeading(new Vector2d(38, 15 * multipler),0)
                               .splineToConstantHeading(new Vector2d(47, 40 * multipler),0)
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}