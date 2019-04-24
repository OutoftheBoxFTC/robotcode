package org.ftc7244.robotcontroller.TeleOp;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.opmodes.input.Button;
import org.ftc7244.robotcontroller.opmodes.input.ButtonType;
import org.ftc7244.robotcontroller.opmodes.input.PressButton;
import org.ftc7244.robotcontroller.sensor.pixycam.IntakePixyProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp(name="PTeleOp")
public class PixyTeleOp extends LinearOpMode {
    private static final double ARM_DOWN_PRESSURE = 0.05, ARM_HANG_OFFSET = 0.3, ANTI_TIP_TRIGGER_SPEED = 243, DRIVE_MODIFIER = 0.5, INTAKE_LATCH_OPEN = 0.05, INTAKE_LATCH_CLOSED = 0.95, ARM_RAISE_POSITION = 2550.0;
    private Robot robot;
    private double modifier = 1;
    private double armMod = 1;
    private double armOffset;
    private double antiTipTimeTarget = 0;
    private boolean slowingDown = false, test = false, intakeKickerUpdated = false, resetting = false, intakeResetUpdated = false, armUpButtonUpdated = false, lidOpened = true;
    private ExecutorService threadManager;
    private Runnable latchMove;
    private Runnable antiTip;
    private Runnable resetArm;
    private Runnable armRaise;
    I2cDeviceSynch pixy;
    IntakePixyProvider intakePixyProvider;
    AtomicBoolean raisingArm;
    long start, frames;
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
    Button intakeTrigger, outtakeTrigger, slowButton, lidButton, armLockButton, intakeKicker, intakeReset, armUpButton;
    PressButton Bbutton;

    @Override
    public void runOpMode() {
        raisingArm = new AtomicBoolean(false);
        threadManager = Executors.newCachedThreadPool();
        robot = new Robot(this);
        robot.init();
        robot.initServos();
        pixy = robot.getIntakeI2c();
        intakePixyProvider = new IntakePixyProvider(pixy, robot);
        slowButton = new PressButton(gamepad1, ButtonType.RIGHT_TRIGGER);
        intakeTrigger = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        outtakeTrigger = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        lidButton = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        Bbutton = new PressButton(gamepad2, ButtonType.B);
        armLockButton = new PressButton(gamepad2, ButtonType.D_PAD_UP);
        intakeKicker = new Button(gamepad2, ButtonType.Y); //New servo made refer to notes below, ask builders about this.
        intakeReset = new Button(gamepad2, ButtonType.X);
        armOffset = robot.getRaisingArm1().getCurrentPosition();
        armUpButton = new Button(gamepad2, ButtonType.A);
        resetArm = () -> {
            resetting = true;
            robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
            robot.moveArm(1);
            double timer = System.currentTimeMillis();
            double armResetNum = 1;
            while (opModeIsActive() && robot.getArmSwitch().getState()) {
                robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
                robot.moveArm(armResetNum);
                if((System.currentTimeMillis() - timer) / 1000 > 0.1){
                    armResetNum = ((System.currentTimeMillis() - timer) / 1000);
                }
            }
            robot.moveArm(0);
            armOffset = robot.getRaisingArm1().getCurrentPosition();
            resetting = false;
        };
        latchMove = () -> {
            raisingArm.set(true);
            robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
            robot.getLid().setPosition(0.8);
            sleep(500);
            while (robot.getRaisingArm1().getCurrentPosition() - armOffset < 300 && opModeIsActive()) {
                robot.getLid().setPosition(0.8);
                robot.moveArm(-1);
            }
            robot.moveArm(0);
            raisingArm.set(false);
        };
        antiTip = () -> {
            sleep(30);
            modifier = 0;
            sleep(720);
            slowingDown = false;
        };
        armRaise = () -> {
            raisingArm.set(true);
            robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
            sleep(500);
            robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            while (opModeIsActive() && robot.getRaisingArm1().getCurrentPosition() - armOffset < 500){
                robot.moveArm(-1);
            }
            robot.getLid().setPosition(0.85);
            while (opModeIsActive() && robot.getRaisingArm1().getCurrentPosition() - armOffset < ARM_RAISE_POSITION) {
                robot.moveArm(-1);
            }
            robot.moveArm(0);
            sleep(150);
            robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            raisingArm.set(false);
        };
        waitForStart();
        frames = 0;
        start = System.currentTimeMillis();
        threadManager.execute(intakePixyProvider);
        while (opModeIsActive()) {
            intakeKickerUpdated = intakeKicker.isUpdated();
            intakeResetUpdated = intakeReset.isUpdated();
            armUpButtonUpdated = armUpButton.isUpdated();
            if (!slowingDown) {
                if (!slowButton.isPressed()) {
                    robot.drive(gamepad1.left_stick_y, gamepad1.right_stick_y); //Uses the left and right sticks to driveRange the robot
                } else {
                    robot.drive(gamepad1.left_stick_y * DRIVE_MODIFIER, gamepad1.right_stick_y * DRIVE_MODIFIER);
                }
                antiTipTimeTarget = System.currentTimeMillis() + 750;
            } else {
                robot.brakeDriveMotors(modifier);
            }
            if (intakeTrigger.isPressed()) {
                armMod = ARM_DOWN_PRESSURE;
                robot.intake(1);
            } else if (outtakeTrigger.isPressed()) { //Else if the right trigger is pressed
                robot.intake(-1); //Outtake
            } else {
                robot.intake(0);
            }
            if (robot.getLeftDrive().getVelocity(AngleUnit.DEGREES) < (-1 * ANTI_TIP_TRIGGER_SPEED) && robot.getRightDrive().getVelocity(AngleUnit.DEGREES) < (-1 * ANTI_TIP_TRIGGER_SPEED)) { //If both wheels are going full speed backwards
                if (gamepad1.left_stick_y < -0.5 && gamepad1.right_stick_y < -0.5) { //And both sticks are pushed forwards
                    modifier = -1;
                    threadManager.submit(antiTip);
                    slowingDown = true;
                }
            } else if (robot.getLeftDrive().getVelocity(AngleUnit.DEGREES) > ANTI_TIP_TRIGGER_SPEED && robot.getRightDrive().getVelocity(AngleUnit.DEGREES) > ANTI_TIP_TRIGGER_SPEED) {
                if (gamepad1.left_stick_y > 0.5 && gamepad1.right_stick_y > 0.5) {
                    modifier = 1;
                    threadManager.submit(antiTip);
                    slowingDown = true;
                }
            }
            if (Bbutton.isPressed()) {
                robot.getLatch().setPosition(0.7);
            } else {
                robot.getLatch().setPosition(0.2);
            }
            if (lidButton.isPressed()) {
                if (robot.getRaisingArm1().getCurrentPosition() - armOffset > 2200) {
                    robot.getIntakeLatch().setPosition(INTAKE_LATCH_OPEN);
                    robot.getLid().setPosition(0.28);
                    lidOpened = true;
                } else {
                    robot.getLid().setPosition(0.28);
                }
            } else if(!robot.getArmSwitch().getState()){
                if (robot.getRaisingArm1().getCurrentPosition() - armOffset > 2200 && robot.getRaisingArm1().getCurrentPosition() - armOffset > 100) {
                    robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
                }
                robot.getLid().setPosition(0.82);
            }else if(!raisingArm.get()){
                if(robot.getRaisingArm1().getCurrentPosition() - armOffset > 2200 && lidOpened){
                    robot.getLid().setPosition(0.28);
                }else {
                    lidOpened = false;
                    robot.getLid().setPosition(0.82);
                }
            }
            if (armLockButton.isPressed()) {
                armMod = ARM_HANG_OFFSET;
            } else if (!intakeTrigger.isPressed()) {
                armMod = 0;
            }
            if (!robot.getArmSwitch().getState()) {
                armOffset = robot.getRaisingArm1().getCurrentPosition();
            }
            if (intakeReset.isPressed() && intakeResetUpdated) {
                threadManager.submit(resetArm);
            }
            if (!raisingArm.get() && !resetting && !armUpButton.isPressed()) {
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
            if (intakeKicker.isPressed() && !raisingArm.get() && intakeKickerUpdated) {
                threadManager.submit(latchMove);
            }
            if (!robot.getArmSwitch().getState() && !raisingArm.get()) {
                robot.getIntakeLatch().setPosition(INTAKE_LATCH_OPEN);
            }
            if (armUpButton.isPressed() && armUpButtonUpdated) {
                threadManager.submit(armRaise);
            }
            if(intakePixyProvider.getStatus() == 0){
                robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_SHOT);
            }else if(intakePixyProvider.getStatus() == 1){
                robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED);
            }else if(intakePixyProvider.getStatus() == 2){
                robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            }else if(intakePixyProvider.getStatus() == 3){
                robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            }
            if(robot.getArmSwitch().getState()){
                robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }else{
                robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }
            frames ++;
            telemetry.addData("Status", intakePixyProvider.getStatus());
            telemetry.addData("Main loop FPS", (double)frames / ((System.currentTimeMillis() - start) * 1000));
            telemetry.addData("Filter loop FPS", intakePixyProvider.getFps());

            telemetry.update();
        }
    }
}
