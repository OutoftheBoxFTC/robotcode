package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;

import java.util.ArrayList;

@TeleOp(name = "Ultrasonic Test", group = "Debug")
public class UltrasonicTest extends AutonomousProcedure {
    private static final double LENGTH = 20;

    @Override
    protected void run() {
        ArrayList<Double> data = new ArrayList<>();
        ArrayList<Double> timeData = new ArrayList<>();
        timeData.add((double) 0);
        data.add((double) 0);
        long lastTime = System.nanoTime();
        while (opModeIsActive()) {
            double val = robot.getTestUS().getUltrasonicLevel();
            if(val != data.get(data.size()-1)){
                long now = System.nanoTime();
                timeData.add((double) (now-lastTime));
                lastTime = now;
                data.add(val);
                if(timeData.size()>LENGTH){
                    timeData.remove(0);
                    data.remove(0);
                }
                telemetry.addData("Avg Time", getMean(timeData));
                telemetry.addData("Time MAD", getMAD(timeData));
                telemetry.addData("Avg Distance", getMean(data));
                telemetry.addData("Distance MAD", getMAD(data));
                telemetry.update();
            }
        }
    }

    private double getMean(ArrayList<Double> data){
        double sum = 0;
        for(double val : data){
            sum += val;
        }
        return sum/data.size();
    }

    private double getMAD(ArrayList<Double> data){
        double avg = 0;
        for(double val : data){
            avg += val;
        }
        avg /= data.size();
        double MAD=0;
        for (double val : data){
            MAD += Math.abs(val-avg);
        }
        return MAD/data.size();
    }
}