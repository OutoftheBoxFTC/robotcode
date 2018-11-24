package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.ftc7244.robotcontroller.hardware.Robot;
@Autonomous
public class ServoTst extends LinearOpMode {
    Robot robot = new Robot(this);

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        waitForStart();
        while(opModeIsActive()){
            robot.getLatch().setPosition(gamepad1.a ? 0.7 : 0.3);
        }
    }
}
