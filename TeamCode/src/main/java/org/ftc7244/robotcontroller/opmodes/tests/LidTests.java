package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Robot;
@Disabled
@TeleOp
public class LidTests extends OpMode {
    Robot robot;
    double lidPos;
    @Override
    public void init() {
        robot = new Robot(this);
        robot.init();
        robot.initServos();
        lidPos = 0.78;
    }

    @Override
    public void loop() {
        if(gamepad1.left_trigger > 0.1){
            lidPos -= 0.1;
        }else if(gamepad1.right_trigger > 0.1){
            lidPos += 0.1;
        }
        robot.getLid().setPosition(lidPos);
    }
}
