package org.ftc7244.robotcontroller.autonamous.control;

public class PIDControl extends ControlSystem {

    private final double kp, ki, kd;
    private final boolean integralReset;

    private double proportional, integral;
    private long lastTime;

    public PIDControl(double kp, double ki, double kd, boolean integralReset){
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.integralReset = integralReset;
    }

    @Override
    public double correction(double error) {
        long now = System.nanoTime(),
                dt = now-lastTime;
        double derivative = (error - proportional)/dt;
        if(lastTime==0){
            dt = 0;
            derivative = 0;
        }
        lastTime = now;
        if(integralReset && proportional/error < 0){
            integral = 0;
        }
        integral += error *dt;
        proportional = error;
        return error *kp + integral*ki + derivative*kd;

    }

    @Override
    public void reset() {
        proportional = 0;
        integral = 0;
        lastTime = 0;
    }
}