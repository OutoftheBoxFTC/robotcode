package org.ftc7244.robotcontroller.sensor.pixycam;

import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.ftc7244.robotcontroller.hardware.Robot;

import java.util.concurrent.atomic.AtomicInteger;

public class IntakePixyProvider implements Runnable {
    AtomicInteger status;
    private Robot robot;
    private double fps;
    I2cDeviceSynch pixy;
    public IntakePixyProvider(I2cDeviceSynch pixy, Robot robot){
        status = new AtomicInteger(0);
        this.robot = robot;
        this.pixy = pixy;
    }

    @Override
    public void run(){
        int counterSilver = 0;
        int counterGold = 0;
        int tempMax = 0;
        int max = 0;
        int silverAverage, goldAverage;
        int prevSilver = 0, prevGold = 0;
        long lastTime = System.currentTimeMillis();
        int[] silverValues, goldValues;
        Pixycam2Provider pixyGold;
        Pixycam2Provider pixySilver;
        pixyGold = new Pixycam2Provider(Pixycam2Provider.Mineral.GOLD, pixy);
        pixySilver = new Pixycam2Provider(Pixycam2Provider.Mineral.SILVER, pixy);
        silverValues = new int[20]; //BEST 20
        goldValues = new int[20]; //BEST 20
        for(int i = 0; i < silverValues.length; i ++){
            silverValues[i] = 0;
        }
        for(int i = 0; i < goldValues.length; i ++){
            goldValues[i] = 0;
        }
        pixyGold.start();
        pixySilver.start();
        pixyGold.setLamps(true, true);
        pixySilver.setLamps(true, true);
        while (robot.getOpMode().opModeIsActive()){
            long now = System.currentTimeMillis();

            fps = 1000.0/(now-lastTime);
            lastTime = now;

            pixyGold.update();
            pixySilver.update();
            int tmpS = 0, tmpG = 0, maxS = 0, maxG = 0;
            for (int silverValue : silverValues) {
                if (silverValue == -1) {
                    tmpS++;
                }
            }
            for (int goldValue : goldValues) {
                if (goldValue == -1) {
                    tmpG++;
                }
            }
            if (tmpS > silverValues.length-1) {
                silverAverage = -1;
            } else {
                silverAverage = 0;
                tmpS = 0;
                for (int silverValue : silverValues) {
                    if (silverValue != -1) {
                        tmpS++;
                        maxS += silverValue;
                    }
                }
                silverAverage = maxS / tmpS;
            }
            if (tmpG > goldValues.length-1) {
                goldAverage = -1;
            } else {
                goldAverage = 0;
                tmpG = 0;
                for (int goldValue : goldValues) {
                    if (goldValue != -1) {
                        tmpG++;
                        maxG += goldValue;
                    }
                }
                goldAverage = maxG / tmpG;
            }
            if (goldAverage > 275) { //Best so far: 275
                status.set(2);
            } else if (silverAverage > 280) { //Best so far: 215
                status.set(2);
                tempMax++;
            } else if (goldAverage > 40 && silverAverage > 40) {
                status.set(3);
            } else {
                status.set(1);
                max = Math.max(tempMax, max);
                tempMax = 0;
            }
            if (goldAverage == -1 && silverAverage == -1) {
                status.set(0);
            }
            goldValues[counterGold] = pixyGold.getWidth();
            silverValues[counterSilver] = pixySilver.getWidth();
            counterSilver++;
            counterGold++;
            if (counterSilver == silverValues.length) {
                counterSilver = 0;
            }
            if (counterGold == goldValues.length) {
                counterGold = 0;
            }
        }
    }

    public int getStatus() {
        return status.get();
    }

    public double getFps() {
        return fps;
    }
}
