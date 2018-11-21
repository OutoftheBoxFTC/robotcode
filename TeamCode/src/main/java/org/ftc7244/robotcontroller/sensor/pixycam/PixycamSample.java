package org.ftc7244.robotcontroller.sensor.pixycam;

import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import static org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample.Sample.CENTER;
import static org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample.Sample.LEFT;
import static org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample.Sample.RIGHT;

public class PixycamSample {
    PixycamProvider pixy;
    I2cDeviceSynch pixyI2c;
    public PixycamSample(I2cDeviceSynch pixy){
        pixyI2c = pixy;
    }

    public void start(){
        pixy = new PixycamProvider(PixycamProvider.Mineral.GOLD, pixyI2c);
        pixy.start();
    }

    public Sample getSample(){
        pixy.update();
        if(pixy.getX() < 110){
            return LEFT;
        }else if(pixy.getX() > 140){
            return RIGHT;
        }else{
            return CENTER;
        }
    }
    public enum Sample{
        LEFT,
        RIGHT,
        CENTER
    }
}
