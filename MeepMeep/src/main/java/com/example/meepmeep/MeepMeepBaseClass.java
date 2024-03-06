package com.example.meepmeep;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepBaseClass {
    static protected boolean isBlue = true;
    static protected boolean isFar = false;
    static Positions positions = new Positions(!isBlue);
    static RoadRunnerBotEntity myBot;
    static MeepMeep meepMeep;
    static DriveShim action;

    protected static Action sequence;

    protected static double angleConvert(double angle) {
        return Math.toRadians(angle * (isBlue ? 1 : -1));
    }


    protected static Vector2d vector2dM(double x, double y) {
        return new Vector2d(x, y * (isBlue ? 1 : -1));
    }

    protected static Pose2d pose2dM(double x, double y, double angle) {
        return new Pose2d(x, y * (isBlue ? 1 : -1), angle);
    }

    public static void other1(boolean isBlueL, boolean isFarL) {
        isBlue = isBlueL;
        isFar = isFarL;
        positions = new Positions(!isBlue);
        meepMeep = new MeepMeep(800);
        myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();
        action = myBot.getDrive();
    }

    public static void other() {


        myBot.runAction(sequence);

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }

}
