package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.ftc7244.robotcontroller.sensor.pixycam.Pixycam2Provider;

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
    Pixycam2Provider provider;
    @Override
    public void init() {
        pixy = hardwareMap.i2cDeviceSynch.get("pixy");
        pixy.setI2cAddress(I2cAddr.create7bit(0x54));
        I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow (1, 26, I2cDeviceSynch.ReadMode.REPEAT);
        pixy.setReadWindow(readWindow);
        provider = new Pixycam2Provider(Pixycam2Provider.Mineral.GOLD, pixy);
        provider.start();
    }

    @Override
    public void loop() {
        provider.update();
        telemetry.addData("X", provider.getX());
        telemetry.update();
    }

}