package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.pixycam.Pixycam2Provider;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
@Disabled
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
    Pixycam2Provider silverPixy;
    Pixycam2Provider goldPixy;
    Robot robot;
    int silverWidth = 0;
    int goldWidth = 0;
    boolean twoMinerals;
    @Override
    public void init(){
        robot = new Robot(this);
        silverPixy = new Pixycam2Provider(Pixycam2Provider.Mineral.SILVER, robot.getIntakeI2c());
        goldPixy = new Pixycam2Provider(Pixycam2Provider.Mineral.GOLD, robot.getIntakeI2c());
        silverPixy.start();
        goldPixy.start();
    }

    @Override
    public void loop() {
        silverWidth = silverPixy.getWidth();
        goldWidth = goldPixy.getWidth();
        if(goldWidth > 290){
            twoMinerals = true;
        }else if(silverWidth > 290){
            twoMinerals = true;
        }else if(goldWidth > 0 && silverWidth > 0){
            twoMinerals = true;
        }
        telemetry.addData("Two minerals", twoMinerals);
        telemetry.update();
    }
}