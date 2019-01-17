package org.ftc7244.robotcontroller.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.ftc7244.robotcontroller.hardware.Robot;

@TeleOp(name="YEET")
public class Yeet extends LinearOpMode {
    Robot robot = new Robot(this);

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        waitForStart();
        while (opModeIsActive()) {
            robot.drive(gamepad1.left_stick_y, gamepad1.right_stick_y);
        }
    }
}
