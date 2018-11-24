package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamProvider;
@TeleOp
public class PixyCamTests extends OpMode {
    I2cDeviceSynch pixy;
    @Override
    public void init() {
        pixy = hardwareMap.i2cDeviceSynch.get("pixy");
        pixy.engage();
    }

    @Override
    public void loop() {
        telemetry.addData("Heartbeat", pixy.getHeartbeatInterval());
        telemetry.addData("Data Dump", pixy.read8(0x54));
        telemetry.update();
    }
}
