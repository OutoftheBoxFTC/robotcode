package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUProvider;

import static org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider.Axis.PITCH;
@TeleOp
public class hangingTests extends LinearOpMode {
    Robot robot = new Robot(this);
    GyroscopeProvider gyro = new RevIMUProvider();
    double offset = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        gyro.init(robot);
        offset = gyro.getRotation(PITCH);
        while(!robot.getOpMode().isStarted()) {
            robot.moveArm(0.31);
        }
        waitForStart();
        while(opModeIsActive() && Math.abs(gyro.getRotation(PITCH) - offset) > Math.toRadians(25)) {
            telemetry.addData("Error", Math.toDegrees(Math.abs(gyro.getRotation(PITCH) - offset)));
            telemetry.update();
            robot.moveArm(0.1);
        }
        robot.moveArm(0);
        sleep(1000);
        robot.getLatch().setPosition(0.7);
        sleep(1000);
        robot.moveArm(1);
        sleep(1000);
        robot.moveArm(0);
        robot.drive(-1, -1);
        sleep(500);
        robot.drive(0, 0);
    }
}
