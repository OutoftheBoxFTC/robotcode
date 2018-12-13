package org.ftc7244.robotcontroller.TeleOp;

import android.media.MediaPlayer;
import android.net.Uri;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.R;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.opmodes.input.Button;
import org.ftc7244.robotcontroller.opmodes.input.ButtonType;
import org.ftc7244.robotcontroller.opmodes.input.PressButton;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamProvider;

@TeleOp(name="TeleOp")
public class MainTeleOp extends LinearOpMode {
    Robot robot = new Robot(this);
    double timer = 0, modifier = 1, armMod = 1;
    boolean slowingDown = false;
    PixycamProvider goldPixy, silverPixy;
    GyroscopeProvider gyro;
    /**
     * Driver Controls:
     *
     * Operator Controls:
     * Left Trigger: Intake, stopping when it gets two of the same color
     * Right Trigger: Outtake
     * Left Bumper: Intake
     * A: Closes hanging latch
     * B: Opens hanging latch
     */
    Button intakeTrigger, outtakeTrigger, flipButton, slowButton, leftBumper, lidButton, armLockButton;
    PressButton Bbutton;
    @Override
    public void runOpMode() throws InterruptedException {
        gyro = new RevIMUProvider();
        robot.init();
        robot.initServos();
        gyro.init(robot);
        goldPixy = new PixycamProvider(PixycamProvider.Mineral.GOLD, robot.getGoldI2c());
        silverPixy = new PixycamProvider(PixycamProvider.Mineral.SILVER, robot.getSilverI2c());
        intakeTrigger = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        outtakeTrigger = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        lidButton = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        Bbutton = new PressButton(gamepad2, ButtonType.B);
        armLockButton = new PressButton(gamepad2, ButtonType.D_PAD_UP);
        waitForStart();
        while(opModeIsActive()) {
            if(modifier != 0.2) {
                robot.drive(gamepad1.left_stick_y, gamepad1.right_stick_y); //Uses the left and right sticks to drive the robot
            }else{
                robot.drive(Math.abs(modifier * gamepad1.left_stick_y), Math.abs(modifier * gamepad1.right_stick_y));
            }
            if (intakeTrigger.isPressed()) {
                armMod = 0.15;
                robot.intake(1);
            }else if(outtakeTrigger.isPressed()){ //Else if the right trigger is pressed
                robot.intake(-1); //Outtake
            }else{
                robot.intake(0);
            }
            if(robot.getLeftDrive().getVelocity(AngleUnit.RADIANS) > 6 && robot.getRightDrive().getVelocity(AngleUnit.RADIANS) > 6){ //If both wheels are going full speed backwards
                if(gamepad1.left_stick_y < -0.5 && gamepad1.right_stick_y < -0.5){ //And both sticks are pushed forwards
                    modifier = 0.2; //Set the modifier to 0.2
                    slowingDown = true;
                }
            }else{//else set the modifier to 1
                modifier = 1;
            }
            robot.getRaisingArm1().setPower((gamepad2.right_stick_y + armMod) * -1);
            robot.getRaisingArm2().setPower((gamepad2.right_stick_y + armMod) * -1);
            if(Bbutton.isPressed()){
                robot.getLatch().setPosition(0.1);
            }else{
                robot.getLatch().setPosition(0.7);
            }
            if(lidButton.isPressed()){
                robot.getLid().setPosition(0.78);
            }else{
                robot.getLid().setPosition(0.4);
            }
            if(armLockButton.isPressed()){
                armMod = 0.3;
            }else if(!intakeTrigger.isPressed()){
                armMod = 0;
            }
        }
    }
}
