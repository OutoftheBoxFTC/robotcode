package org.ftc7244.robotcontroller.autonamous.drive;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

public class MecanumDrive {
    private static final double A = Math.PI/2, TAU = Math.PI*2;


    private Robot robot;
    private GyroscopeProvider gyro;
    public MecanumDrive(Robot robot, GyroscopeProvider gyro){
        this.robot = robot;
        this.gyro = gyro;
    }

    public void drive(double angle, double translation, double rotation, double power){
        angle = clockWiseDifference(angle, gyro.getRotation(GyroscopeProvider.Axis.YAW));
        double total = translation + rotation;
        translation /= total;
        double vR = rotation/total*2;

        double vY = Math.sin(angle)*translation, vX = Math.cos(angle)*translation/Math.sin(A);
        double a = vY + vX + vR,
               b = -vX - vR + vY,
               c = -vX + vR + vY,
               d = vX - vR + vY;

        double factor = power/Math.max(Math.max(Math.max(a, b), c), d);
        robot.drive(factor*a, factor*b, factor*c, factor*d);


        /*Factors
            Average y pull between both sides equals vy
            Net y pull between both sides equals rotation
            Average x pull between both sides equals vx
            Net x pull between both sides equals 0
         */
    }

    public double clockWiseDifference(double a, double b){
        //angle moving clockwise from a to arrive at b
        return (a-b+TAU)%TAU;
    }
}
