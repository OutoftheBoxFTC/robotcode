package org.ftc7244.robotcontroller.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.robotcontroller.hardware.Robot;

public class TeleOp extends LinearOpMode {
    Robot robot = new Robot(this);
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        robot.initServos();
        waitForStart();
        while(opModeIsActive()){
            robot.drive(gamepad1.left_stick_y, gamepad1.right_stick_y);
        }
    }
}
