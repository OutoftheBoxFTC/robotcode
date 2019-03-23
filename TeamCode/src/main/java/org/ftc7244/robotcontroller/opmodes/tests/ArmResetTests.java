package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.robotcontroller.hardware.Robot;
@Autonomous
public class ArmResetTests extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this);
        robot.init();
        robot.initServos();
        waitForStart();
        double offset = robot.getRaisingArm1().getCurrentPosition();
        while (opModeIsActive()){
            if(!robot.getArmSwitch().getState()){
                offset = robot.getRaisingArm1().getCurrentPosition();
            }
            telemetry.addData("ArmPosition", robot.getRaisingArm1().getCurrentPosition() - offset);
            telemetry.update();
        }
    }
}
