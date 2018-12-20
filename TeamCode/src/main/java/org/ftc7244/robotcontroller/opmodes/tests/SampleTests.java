package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.pixycam.PixyCam2Provider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample;
@TeleOp
public class SampleTests extends LinearOpMode {
    Robot robot = new Robot(this);
    PixyCam2Provider pixy;
    PixycamSample sample;
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        pixy = new PixyCam2Provider(PixycamProvider.Mineral.GOLD, robot.getSampleI2c());
        sample = new PixycamSample(pixy);
        while(!sample.start()){
            telemetry.addLine("Wait");
            telemetry.update();
        }
        waitForStart();
        while (opModeIsActive()){
            telemetry.addData("Test", sample.run());
            telemetry.update();
        }
    }
}
