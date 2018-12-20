package org.ftc7244.robotcontroller.sensor.pixycam;

import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class PixyCam2Provider {
    PixycamProvider.Mineral mineral;
    I2cDeviceSynch pixy;
    byte[] pixyData;
    List<Short> shorts;
    /**
     * Initializer, used to set the pixycam and which mineral it is detecting
     * @param mineral the mineral, GOLD or SILVER to detect
     * @param pixy an I2cDeviceSync class representing the pixy cam
     */
    public PixyCam2Provider(PixycamProvider.Mineral mineral, I2cDeviceSynch pixy){
        this.mineral = mineral;
        this.pixy = pixy;
    }

    /**
     * Update funtion, queries the pixy cam with data header 0x51 if it is looking at gold minerals, or 0x52 if looking at silver minerals, than stores it to the Bye[] pixyData variable
     * During the update, a block with a header is sent to the pixy, and the pixy returns 5 bytes containing information about what it sees
     * */
    public void update(){
        pixy.engage();
        if(mineral == PixycamProvider.Mineral.GOLD) {
            pixyData = pixy.read(0, 26);
        }else if(mineral == PixycamProvider.Mineral.SILVER){
        //    pixyData = pixy.read(0x52, 5);
        }
        shorts = endianToShort(pixyData);

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
    public int getSignature(){
        return shorts.get(2);
    }
    public int getX(){
        return shorts.get(3);
    }
    public int getY(){
        return shorts.get(4);
    }
    public int getWidth(){
        return shorts.get(5);
    }
    public int getHeight(){
        return shorts.get(6);
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

    private List<Short> endianToShort(byte[] byteArray){
        List<Short> shorts = new ArrayList<Short>();
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        bb.order( ByteOrder.LITTLE_ENDIAN);
        while( bb.hasRemaining()) {
            shorts.add(bb.getShort());
        }
        return shorts;
    }
}
