package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamProvider;
@TeleOp
public class PixyCamTests extends OpMode {
    PixycamProvider pixy;
    I2cDeviceSynch pixyI2c;
    @Override
    public void init() {
        pixyI2c = hardwareMap.i2cDeviceSynch.get("pixy");
        pixy = new PixycamProvider(PixycamProvider.Mineral.GOLD, pixyI2c);
        pixy.start();
    }

    @Override
    public void loop() {
        pixy.update();
        telemetry.addData("Num Objects Seen", pixy.getX());
    }
}
