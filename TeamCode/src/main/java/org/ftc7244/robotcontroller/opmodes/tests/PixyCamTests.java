package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.ftc7244.robotcontroller.sensor.pixycam.PixycamProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.SampleProvider;
@TeleOp
public class PixyCamTests extends OpMode {
    I2cDeviceSynch pixy;
    PixycamProvider pixyProvider;
    SampleProvider sampleProvider;
    @Override
    public void init() {
        pixy = hardwareMap.i2cDeviceSynch.get("pixy");
        pixyProvider = new PixycamProvider(PixycamProvider.Mineral.GOLD, pixy);
        sampleProvider = new SampleProvider(pixyProvider);
        while(!sampleProvider.start()){}
    }

    @Override
    public void loop() {
        telemetry.addData("Location", sampleProvider.run());
        telemetry.update();
    }
}
