package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.robotcontroller.hardware.Robot;
@Autonomous
@Disabled
public class ArmResetTests extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this);
        robot.init();
        robot.initServos();
        waitForStart();
        while(opModeIsActive() && robot.getArmSwitch().getState()){
            robot.moveArm(0.5);
        }
        robot.moveArm(-0.3);
        sleep(750);
        while(opModeIsActive() && robot.getArmSwitch().getState()){
            robot.moveArm(0.1);
        }
    }
}
