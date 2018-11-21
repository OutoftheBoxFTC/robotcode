package org.ftc7244.robotcontroller.autonamous.control;

import android.content.Context;

public class PIDControl extends ControlSystem {

    private final double kp, ki, kd;
    private final boolean integralReset;

    private double proportional, integral, integralRange;
    private long lastTime;

    public PIDControl(double kp, double ki, double kd, double integralRange, boolean integralReset){
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.integralReset = integralReset;
        this.integralRange = integralRange;
    }

    public PIDControl(double integralRange, boolean integralReset, String path, Context context){
        double[] parameters = configFromFile(path, context);
        kp = parameters[0];
        ki = parameters[1];
        kd = parameters[2];
        this.integralRange = integralRange;
        this.integralReset = integralReset;
    }

    @Override
    public double correction(double error) {
        long now = System.nanoTime(),
                dt = now-lastTime;
        double derivative = (error - proportional)/dt;
        derivative = (Double.isNaN(derivative) || Double.isInfinite(derivative) ? 0 : derivative);
        if(lastTime==0){
            dt = 0;
            derivative = 0;
        }
        lastTime = now;
        if(integralReset && proportional/error < 0 || Math.abs(error) >= integralRange){
            integral = 0;
        }
        integral += error*dt;
        proportional = error;
        return -(error *kp + integral*ki + derivative*kd);
    }



    @Override
    public void reset() {
        proportional = 0;
        integral = 0;
        lastTime = 0;
    }
}
