package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@TeleOp
/*
Bytes    16-bit word    Description
        ----------------------------------------------------------------
        0, 1     y              sync: 0xaa55=normal object, 0xaa56=color code object
        2, 3     y              checksum (sum of all 16-bit words 2-6, that is, bytes 4-13)
        4, 5     y              signature number
        6, 7     y              x center of object
        8, 9     y              y center of object
        10, 11   y              width of object
        12, 13   y              height of object
        */
public class PixyCamTests extends OpMode {
    I2cDeviceSynch pixy;
    I2cDevice pixyI2c;
    byte[] data = new byte[12];
    byte[] request = {(byte)0xae, (byte)0xc1, (byte)32, (byte)2, (byte)0xFF, (byte)0xFF};
    byte[] lampRequest = {(byte)0xae, (byte)0xc1, (byte)22, (byte)2, (byte)1, (byte)0};
    short temp = 0;
    List<Short> shorts;
    boolean showData = false;

    @Override
    public void init() {
        pixy = hardwareMap.i2cDeviceSynch.get("pixy");
        pixy.setI2cAddress(I2cAddr.create7bit(0x54));
        I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow (1, 26, I2cDeviceSynch.ReadMode.REPEAT);
        pixy.setReadWindow(readWindow);
        pixy.engage();
        pixy.write(0, lampRequest);
    }

    @Override
    public void loop() {
        pixy.write(0, request);
        data = pixy.read(0, 26);
        shorts = endianToShort(data);
        showData = true;
        for(int i = 0; i < shorts.size(); i ++){
            telemetry.addData(String.valueOf(i), shorts.get(i));
        }
        telemetry.update();
    }

    private short bytesToShort(byte a, byte b) {
        short sh = (short)a;
        sh <<= 8;
        short ret = (short)(sh | b);
        return ret;
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