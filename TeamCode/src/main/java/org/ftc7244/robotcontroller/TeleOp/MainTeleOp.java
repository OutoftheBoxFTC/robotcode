package org.ftc7244.robotcontroller.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Robot;
@TeleOp(name="TeleOp")
public class MainTeleOp extends LinearOpMode {
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
