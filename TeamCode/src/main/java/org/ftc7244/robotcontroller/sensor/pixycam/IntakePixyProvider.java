package org.ftc7244.robotcontroller.sensor.pixycam;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

public class IntakePixyProvider {
    Pixycam2Provider pixyGold;
    Pixycam2Provider pixySilver;
    Pixycam2Provider pixyOneSilver;
    public int[] silverValues, goldValues;
    int counterSilver = 0, counterGold = 0;
    int status = 0;
    public int silverAverage, goldAverage;
    public IntakePixyProvider(I2cDeviceSynch pixy){
        pixyGold = new Pixycam2Provider(Pixycam2Provider.Mineral.GOLD, pixy);
        pixySilver = new Pixycam2Provider(Pixycam2Provider.Mineral.SILVER, pixy);
        pixyOneSilver = new Pixycam2Provider(Pixycam2Provider.Mineral.ONE_SILVER, pixy);
        silverValues = new int[20];
        goldValues = new int[20];
        for(int i = 0; i < silverValues.length; i ++){
            silverValues[i] = 0;
        }
        for(int i = 0; i < goldValues.length; i ++){
            goldValues[i] = 0;
        }
        silverAverage = 0;
        goldAverage = 0;
    }

    public void start(){
        pixyGold.start();
        pixySilver.start();
        pixyOneSilver.start();
        pixyGold.setLamps(true, true);
        pixySilver.setLamps(true, true);
        pixyOneSilver.setLamps(true, true);
    }

    public void update(){
        pixyGold.update();
        pixySilver.update();
        pixyOneSilver.update();
        int tmpS = 0, tmpG = 0, maxS = 0, maxG = 0;
        for(int i = 0; i < silverValues.length; i ++){
            if(silverValues[i] == -1){
                tmpS ++;
            }
        }
        for(int i = 0; i < goldValues.length; i ++){
            if(goldValues[i] == -1){
                tmpG ++;
            }
        }
        if(tmpS > 10){
            silverAverage = -1;
        }else{
            for(int i = 0; i < silverValues.length; i ++){
                if(silverValues[i] != -1){
                    tmpS ++;
                    maxS += silverValues[i];
                }
            }
            silverAverage = maxS/tmpS;
        }
        if(tmpG > 10){
            goldAverage = -1;
        }else{
            for(int i = 0; i < goldValues.length; i ++){
                if(goldValues[i] != -1){
                    tmpG ++;
                    maxG += goldValues[i];
                }
            }
            goldAverage = maxG/tmpG;
        }
        if (goldAverage > 275) { //Best so far: 275
            status = 2;
        }else if(silverAverage > 280){ //Best so far: 215
            status = 2;
        } else if(goldAverage > 1 && silverAverage > 1){
            status = 2;
        } else {
            status = 1;
        }
        if(goldAverage == -1 && silverAverage == -1){
            status = 0;
        }
        goldValues[counterGold] = pixyGold.getWidth();
        silverValues[counterSilver] = pixySilver.getWidth();
        counterSilver ++;
        counterGold ++;
        if(counterSilver == silverValues.length){
            counterSilver = 0;
        }
        if(counterGold == goldValues.length){
            counterGold = 0;
        }
    }

    public int getStatus() {
        return status;
    }
}
