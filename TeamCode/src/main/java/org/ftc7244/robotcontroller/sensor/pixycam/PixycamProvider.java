package org.ftc7244.robotcontroller.sensor.pixycam;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

public class PixycamProvider {
    //TODO: Find coordinates for Left, Right, and Center
    I2cDeviceSynch pixyCam;
    byte[] pixyData;
    public PixycamProvider(HardwareMap map){
        pixyCam = map.i2cDeviceSynch.get("pixy");
    }
    public void start(){
        pixyCam.setI2cAddress(I2cAddr.create7bit(0x54));
        I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow (1, 26, I2cDeviceSynch.ReadMode.REPEAT);
        pixyCam.setReadWindow(readWindow);
        pixyCam.engage();
    }
    public void update(){
        pixyData = pixyCam.read(0x51, 5);
    }
    public double getX(){
        return pixyData[1];
    }
    public double getY(){
        return  pixyData[2];
    }
    public double getWidth(){
        return pixyData[3];
    }
    public double getObjectsSeen(){
        return pixyData[0];
    }
    public boolean isEngaged(){
        return pixyCam.isEngaged();
    }
    public boolean isArmed(){
        return pixyCam.isArmed();
    }
}
