package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Robot;
@Disabled
@TeleOp
public class ArmSensorTests extends LinearOpMode {
    Robot robot;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        robot.init();
        robot.initServos();
        waitForStart();
        while (opModeIsActive()){
            telemetry.addData("Pressed", !robot.getArmSwitch().getState());
            telemetry.update();
        }
    }
}
