package org.ftc7244.robotcontroller.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.input.Button;
import org.ftc7244.robotcontroller.input.ButtonType;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUProvider;

@TeleOp(name="TeleOp")
public class MainTeleOp extends LinearOpMode {
    Robot robot = new Robot(this);
    Button trigger, flipButton;
    double modifier = 0;
    GyroscopeProvider gyro = new RevIMUProvider();
    boolean flipping = false;
    double time = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        gyro.init(robot);
        robot.initServos();
        trigger = new Button(gamepad1, ButtonType.RIGHT_TRIGGER);
        flipButton = new Button(gamepad1, ButtonType.A);
        waitForStart();
        while(opModeIsActive()){
            if(trigger.isPressed()){
                modifier = 0.3;
            }else{
                modifier = 1;
            }
            if(!flipping) {
                robot.drive(gamepad1.left_stick_y * modifier, gamepad1.right_stick_y * modifier);
            }
            if(flipButton.isPressed()){
                flipping = true;
                time = System.currentTimeMillis() + 750;
                modifier = 0;
            }
            if(flipping && System.currentTimeMillis() < time){
                robot.drive(0, 1);
            }else if(flipping){
                modifier = 1;
                flipping = false;
            }
        }
    }
}
