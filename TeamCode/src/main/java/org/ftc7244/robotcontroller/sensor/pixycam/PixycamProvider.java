package org.ftc7244.robotcontroller.sensor.pixycam;

import com.qualcomm.robotcore.hardware.I2cDeviceSynch;


/**
 * This class is a provider for the PixyCam, returning various data from the pixy cam
 */
public class PixycamProvider {
    Mineral mineral;
    I2cDeviceSynch pixy;
    byte[] pixyData;

    /**
     * Initializer, used to set the pixycam and which mineral it is detecting
     * @param mineral the mineral, GOLD or SILVER to detect
     * @param pixy an I2cDeviceSync class representing the pixy cam
     */
    public PixycamProvider(Mineral mineral, I2cDeviceSynch pixy){
        this.mineral = mineral;
        this.pixy = pixy;
    }

    /**
     * Update funtion, queries the pixy cam with data header 0x51 if it is looking at gold minerals, or 0x52 if looking at silver minerals, than stores it to the Bye[] pixyData variable
     * During the update, a block with a header is sent to the pixy, and the pixy returns 5 bytes containing information about what it sees {@link #getNumberOfObjectsSeen() getNumberOfObjectsSeen()}
     * */
    public void update(){
        pixy.engage();
        if(mineral == Mineral.GOLD) {
            pixyData = pixy.read(0x51, 5);
        }else if(mineral == Mineral.SILVER){
            pixyData = pixy.read(0x52, 5);
        }
    }

    /**
     * Engages the pixy, turning it on and quering data for the first time. Must be run in init, to give enough time for it to turn on
     */
    public void start(){
        pixy.engage();
    }

    /**
     * These functions return data from the pixy cam, getting data about the largest object seen
     * @return returns the data, a rounded byte from 0-255
     */
    public int getNumberOfObjectsSeen(){
        return 0xff & pixyData[0];
    }
    public int getX(){
        return 0xff & pixyData[1];
    }
    public int getY(){
        return 0xff & pixyData[2];
    }
    public int getWidth(){
        return 0xff & pixyData[3];
    }
    public int getHeight(){
        return 0xff & pixyData[4];
    }

    /**
     * Returns if the pixy cam is currently on
     * @return boolean if the pixy is on
     */
    public boolean isEngaged(){
        return pixy.isEngaged();
    }

    /**
     * Enumerator storing GOLD and SILVER, used to initialize the pixy cam and know what to return
     */
    public enum Mineral{
        GOLD,
        SILVER
    }
}
