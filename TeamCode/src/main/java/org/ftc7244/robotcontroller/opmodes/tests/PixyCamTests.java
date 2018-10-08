package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.robotcontroller.sensor.pixycam.PixycamProvider;

public class PixyCamTests extends LinearOpMode {
    PixycamProvider pixyCam = new PixycamProvider(hardwareMap);
    @Override
    public void runOpMode() {
        pixyCam.start();
    }
}
