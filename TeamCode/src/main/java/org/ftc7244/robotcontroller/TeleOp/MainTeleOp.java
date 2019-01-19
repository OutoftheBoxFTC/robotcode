package org.ftc7244.robotcontroller.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.opmodes.input.Button;
import org.ftc7244.robotcontroller.opmodes.input.ButtonType;
import org.ftc7244.robotcontroller.opmodes.input.PressButton;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUProvider;

@TeleOp(name="TeleOp")
public class MainTeleOp extends LinearOpMode {
    private static final double ARM_DOWN_PRESSURE = 0, ARM_HANG_OFFSET = 0.3, ANTI_TIP_TRIGGER_SPEED = 343;

    private Robot robot = new Robot(this);
    private double timer = 0, modifier = 1, armMod = 1, armOffset, timeTarget = 0, antiTipTimeTarget = 0;
    private boolean slowingDown = false, raisingArm = false, test = false;
    private GyroscopeProvider gyro;
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
    Button intakeTrigger, outtakeTrigger, flipButton, slowButton, leftBumper, lidButton, armLockButton, intakeKicker;
    PressButton Bbutton, intakeReset;
    @Override
    public void runOpMode() throws InterruptedException {
        gyro = new RevIMUProvider();
        robot.init();
        robot.initServos();
        gyro.init(robot);
        intakeTrigger = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        outtakeTrigger = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        lidButton = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        Bbutton = new PressButton(gamepad2, ButtonType.B);
        armLockButton = new PressButton(gamepad2, ButtonType.D_PAD_UP);
        intakeKicker = new Button (gamepad2, ButtonType.Y); //New servo made refer to notes below, ask builders about this.
        intakeReset = new PressButton(gamepad2, ButtonType.X);
        armOffset = robot.getRaisingArm1().getCurrentPosition();
        waitForStart();
        while(opModeIsActive()) {
            if(!slowingDown) {
                robot.drive(gamepad1.left_stick_y, gamepad1.right_stick_y); //Uses the left and right sticks to drive the robot
                antiTipTimeTarget = System.currentTimeMillis() + 750;
            }else{
                //robot.drive(Math.abs(gamepad1.left_stick_y) * modifier, Math.abs(gamepad1.right_stick_y) * modifier); //Uses the left and right sticks to drive the robot
                robot.brakeDriveMotors(modifier);
                if(antiTipTimeTarget - 720 < System.currentTimeMillis()){
                    modifier = 0;
                }
                if(antiTipTimeTarget < System.currentTimeMillis()){
                    slowingDown = false;
                }
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
                    slowingDown = true;
                }
            }else if(robot.getLeftDrive().getVelocity(AngleUnit.DEGREES) > ANTI_TIP_TRIGGER_SPEED && robot.getRightDrive().getVelocity(AngleUnit.DEGREES) > ANTI_TIP_TRIGGER_SPEED){
                if(gamepad1.left_stick_y > 0.5 && gamepad1.right_stick_y > 0.5){
                    modifier = 1;
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
            }if(intakeKicker.isPressed()){ //Please get this fixed, for new server put in.
            }
            if(robot.getRaisingArm1().getCurrentPosition() - armOffset < 60 && !raisingArm){
                robot.getIntakeLatch().setPosition(0.8);
            }else if(!raisingArm && !lidButton.isPressed()){
                //robot.getIntakeLatch().setPosition(0.2);
            }
            if(!raisingArm && !intakeReset.isPressed()){
                if(robot.getRaisingArm1().getCurrentPosition() - armOffset < 100){
                    if(gamepad1.right_stick_y < -0.1){
                        robot.getRaisingArm1().setPower((gamepad2.right_stick_y + armMod) * -1);
                        robot.getRaisingArm2().setPower((gamepad2.right_stick_y + armMod) * -1);
                    }
                }else {
                    robot.getRaisingArm1().setPower((gamepad2.right_stick_y + armMod) * -1);
                    robot.getRaisingArm2().setPower((gamepad2.right_stick_y + armMod) * -1);
                }
            }
            if(intakeKicker.isPressed() && !raisingArm){
                raisingArm = true;
                timeTarget = System.currentTimeMillis() + 500;
            }
            if(raisingArm){
                robot.getIntakeLatch().setPosition(0.2);
                if(timeTarget < System.currentTimeMillis()){
                    if(robot.getRaisingArm1().getCurrentPosition() - armOffset < 836){
                        robot.moveArm(-1);
                    }else{
                        robot.moveArm(0);
                        raisingArm = false;
                    }
                }
            }
            if(!robot.getArmSwitch().getState()){
                armOffset = robot.getRaisingArm1().getCurrentPosition();
            }
            if(intakeReset.isPressed()){
                robot.getIntakeLatch().setPosition(0.2);
                robot.moveArm(0.75);
                if(!robot.getArmSwitch().getState()){
                    armOffset = robot.getRaisingArm1().getCurrentPosition();
                    robot.moveArm(0);
                    intakeReset.release();
                }
            }
            telemetry.addData("Pressed", robot.getArmSwitch().getState());
            telemetry.addData("Target", timeTarget);
            telemetry.addData("Time", System.currentTimeMillis());
            telemetry.addData("Current Position", robot.getRaisingArm1().getCurrentPosition() - armOffset);
            telemetry.addData("State", !robot.getArmSwitch().getState());
            telemetry.addData("ControllerLeft", gamepad1.left_stick_y);
            telemetry.addData("ControllerRight", gamepad1.right_stick_y);
            telemetry.addData("Velocity", robot.getLeftDrive().getVelocity(AngleUnit.DEGREES));
            telemetry.addData("Testing", test);
            telemetry.addData("Bool 1", robot.getLeftDrive().getVelocity(AngleUnit.DEGREES));
            telemetry.addData("Bool 2",  robot.getLeftDrive2().getVelocity(AngleUnit.DEGREES));
            telemetry.addData("Bool 3", robot.getRightDrive().getVelocity(AngleUnit.DEGREES));
            telemetry.addData("Bool 4", robot.getRightDrive2().getVelocity(AngleUnit.DEGREES));
            telemetry.update();
        }
    }
}
