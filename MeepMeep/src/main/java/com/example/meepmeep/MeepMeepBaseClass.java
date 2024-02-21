package com.example.meepmeep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.AddTrajectorySequenceCallback;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class  MeepMeepBaseClass {
    static protected boolean isBlue = true;
    protected static double angleConvert(double angle) {
        return Math.toRadians(angle * (isBlue ? 1 : -1));
    }

    protected static Vector2d vector2dM(double x, double y) {
        return new Vector2d(x,y * (isBlue ? 1 : -1));
    }
    protected static Pose2d pose2dM(double x, double y, double angle) {
        return new Pose2d(x,y * (isBlue ? 1 : -1), angle);
    }
    public static void other() {
        MeepMeep meepMeep = new MeepMeep(800);
        System.out.println(sequence);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(sequence);

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
    protected static AddTrajectorySequenceCallback sequence;
}
