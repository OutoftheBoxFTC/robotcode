package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample;

import static org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider.Axis.PITCH;
@TeleOp
public class hangingTests extends LinearOpMode {
    Robot robot = new Robot(this);
    GyroscopeProvider gyro = new RevIMUProvider();
    PixycamProvider pixycamProvider;
    PixycamSample sample;
    PixycamSample.SampleTransform transfom;
    double offset = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        pixycamProvider = new PixycamProvider(PixycamProvider.Mineral.GOLD, robot.getSampleI2c());
        sample = new PixycamSample(pixycamProvider);
        sample.start();
        gyro.init(robot);
        offset = gyro.getRotation(PITCH);
        while(!robot.getOpMode().isStarted()) {
            robot.moveArm(0.31);
            transfom = sample.run();
            telemetry.update();
        }
        waitForStart();
        while(opModeIsActive() && Math.abs(gyro.getRotation(PITCH) - offset) > Math.toRadians(25)) {
            robot.moveArm(0.1);
        }
        robot.moveArm(0);
        sleep(1000);
        robot.getLatch().setPosition(0.7);
        sleep(1000);
        robot.moveArm(1);
        sleep(1000);
        robot.moveArm(0);
        sleep(1000);
    }
}
