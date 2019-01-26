package org.ftc7244.robotcontroller.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.opmodes.input.Button;
import org.ftc7244.robotcontroller.opmodes.input.ButtonType;
import org.ftc7244.robotcontroller.opmodes.input.PressButton;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TeleOp(name="NewTeleOp")
public class NewTeleOp extends LinearOpMode {
    private static final double ARM_DOWN_PRESSURE = 0.1, ARM_HANG_OFFSET = 0.3, ANTI_TIP_TRIGGER_SPEED = 343, DRIVE_MODIFIER = 0.5;
    private Robot robot = new Robot(this);
    private double timer = 0, modifier = 1, armMod = 1, armOffset, timeTarget = 0, antiTipTimeTarget = 0;
    private boolean slowingDown = false, raisingArm = false, test = false, intakeKickerUpdated = false, resetting = false;
    private GyroscopeProvider gyro;
    private ExecutorService threadManager;
    private Runnable latchMove, antiTip, resetArm;
    /**
     * Driver Controls:
     *
     * Operator Controls:
     * Left Trigger: Intake
     * Right Trigger: Outtake
     * Left Bumper: Intake
     * Right Bumper: Lid
     * A: Closes hanging latch
     * B: Opens hanging latch
     * Y: Closes Intake and Lifts
     * X: Move down and reset
     */
    Button intakeTrigger, outtakeTrigger, slowButton, lidButton, armLockButton, intakeKicker, intakeReset;
    PressButton Bbutton;
    @Override
    public void runOpMode() throws InterruptedException {
        threadManager = Executors.newCachedThreadPool();
        gyro = new RevIMUProvider();
        robot.init();
        robot.initServos();
        gyro.init(robot);
        slowButton = new PressButton(gamepad1, ButtonType.RIGHT_TRIGGER);
        intakeTrigger = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        outtakeTrigger = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        lidButton = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        Bbutton = new PressButton(gamepad2, ButtonType.B);
        armLockButton = new PressButton(gamepad2, ButtonType.D_PAD_UP);
        intakeKicker = new Button (gamepad2, ButtonType.Y); //New servo made refer to notes below, ask builders about this.
        intakeReset = new Button(gamepad2, ButtonType.X);
        armOffset = robot.getRaisingArm1().getCurrentPosition();
        resetArm = () -> {
            resetting = true;
            robot.getIntakeLatch().setPosition(0.2);
            robot.moveArm(0.75);
            while(opModeIsActive() && robot.getArmSwitch().getState()){}
            robot.moveArm(0);
            armOffset = robot.getRaisingArm1().getCurrentPosition();
            resetting = false;
        };
        latchMove = () -> {
            raisingArm = true;
            robot.getIntakeLatch().setPosition(0.2);
            sleep(500);
            while(robot.getRaisingArm1().getCurrentPosition() - armOffset < 836 && opModeIsActive()) {
                robot.moveArm(-1);
            }
            robot.moveArm(0);
            raisingArm = false;
        };
        antiTip = () -> {
            sleep(30);
            modifier = 0;
            sleep(720);
            slowingDown = false;
        };
        waitForStart();
        while(opModeIsActive()) {
            intakeKickerUpdated = intakeKicker.isUpdated();
            if(!slowingDown) {
                if(slowButton.isPressed()) {
                    robot.drive(gamepad1.left_stick_y, gamepad1.right_stick_y); //Uses the left and right sticks to drive the robot
                }else{
                    robot.drive(gamepad1.left_stick_y * DRIVE_MODIFIER, gamepad1.right_stick_y * DRIVE_MODIFIER);
                }
                antiTipTimeTarget = System.currentTimeMillis() + 750;
            }else{
                robot.brakeDriveMotors(modifier);
            }
            if (intakeTrigger.isPressed()) {
                armMod = ARM_DOWN_PRESSURE;
                robot.intake(1);
            }else if(outtakeTrigger.isPressed()){ //Else if the right trigger is pressed
                robot.intake(-1); //Outtake
            }else{
                robot.intake(0);
            }
            if(robot.getLeftDrive().getVelocity(AngleUnit.DEGREES) < (-1 * ANTI_TIP_TRIGGER_SPEED) && robot.getRightDrive().getVelocity(AngleUnit.DEGREES) < (-1 * ANTI_TIP_TRIGGER_SPEED)){ //If both wheels are going full speed backwards
                if(gamepad1.left_stick_y < -0.5 && gamepad1.right_stick_y < -0.5){ //And both sticks are pushed forwards
                    modifier = -1;
                    threadManager.submit(antiTip);
                    slowingDown = true;
                }
            }else if(robot.getLeftDrive().getVelocity(AngleUnit.DEGREES) > ANTI_TIP_TRIGGER_SPEED && robot.getRightDrive().getVelocity(AngleUnit.DEGREES) > ANTI_TIP_TRIGGER_SPEED){
                if(gamepad1.left_stick_y > 0.5 && gamepad1.right_stick_y > 0.5){
                    modifier = 1;
                    threadManager.submit(antiTip);
                    slowingDown = true;
                }
            }
            if(Bbutton.isPressed()){
                robot.getLatch().setPosition(0.7);
            }else{
                robot.getLatch().setPosition(0.2);
            }
            if(lidButton.isPressed()){
                robot.getLid().setPosition(0.78);
                if(robot.getRaisingArm1().getCurrentPosition() - armOffset > 2200){
                    robot.getIntakeLatch().setPosition(0.8);
                }
            }else{
                if(robot.getRaisingArm1().getCurrentPosition() - armOffset < 2200 && robot.getRaisingArm1().getCurrentPosition() - armOffset > 100){
                    robot.getIntakeLatch().setPosition(0.2);
                }
                robot.getLid().setPosition(0.4);
            }
            if(armLockButton.isPressed()){
                armMod = ARM_HANG_OFFSET;
            }else if(!intakeTrigger.isPressed()) {
                armMod = 0;
            }
            if(!robot.getArmSwitch().getState()){
                armOffset = robot.getRaisingArm1().getCurrentPosition();
            }
            if(intakeReset.isPressed()){
                threadManager.submit(resetArm);
            }
            if (!raisingArm && !resetting) {
                if (robot.getRaisingArm1().getCurrentPosition() - armOffset < 100) {
                    if (gamepad2.right_stick_y <= 0) {
                        robot.getRaisingArm1().setPower((Math.pow(gamepad2.right_stick_y, 2) + armMod) * -1);
                        robot.getRaisingArm2().setPower((Math.pow(gamepad2.right_stick_y, 2) + armMod) * -1);
                    }
                } else {
                    robot.getRaisingArm1().setPower((gamepad2.right_stick_y + armMod) * -1);
                    robot.getRaisingArm2().setPower((gamepad2.right_stick_y + armMod) * -1);
                }
            }
            if (intakeKicker.isPressed() && !raisingArm && intakeKickerUpdated) {
                threadManager.submit(latchMove);
            }
            if(!robot.getArmSwitch().getState() && !raisingArm){
                robot.getIntakeLatch().setPosition(0.8);
            }
            telemetry.addData("RaisingArm1", robot.getRaisingArm1().getPower());
            telemetry.addData("RaisingArm2", robot.getRaisingArm2().getPower());
            telemetry.addData("ArmMod", armMod);
            telemetry.update();
        }
    }
}
