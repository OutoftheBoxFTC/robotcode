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

@TeleOp(name="Everything is going horribly wrong")
public class ProblemSolvingTeleOp extends LinearOpMode {
    private static final double ARM_DOWN_PRESSURE = 0.05, ARM_HANG_OFFSET = 0.3, ANTI_TIP_TRIGGER_SPEED = 243, DRIVE_MODIFIER = 0.5, INTAKE_LATCH_OPEN = 0.05, INTAKE_LATCH_CLOSED = 0.95, ARM_RAISE_POSITION = 2500.0;
    private Robot robot;
    private double timer = 0, modifier = 1, armMod = 1, armOffset, timeTarget = 0, antiTipTimeTarget = 0;
    private boolean slowingDown = false, raisingArm = false, test = false, intakeKickerUpdated = false, resetting = false, intakeResetUpdated = false, armUpButtonUpdated = false;
    private ExecutorService threadManager;
    private Runnable latchMove, antiTip, resetArm, armRaise, pixyThread;
    I2cDeviceSynch pixy;
    IntakePixyProvider intakePixyProvider;
    int max = 0;
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
    public void runOpMode() throws InterruptedException {
        threadManager = Executors.newCachedThreadPool();
        robot = new Robot(this);
        robot.init();
        robot.initServos();
        pixy = robot.getIntakeI2c();
        intakePixyProvider = new IntakePixyProvider(pixy);
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
        intakePixyProvider.start();
        resetArm = () -> {
            resetting = true;
            robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
            robot.moveArm(0.75);
            while (opModeIsActive() && robot.getArmSwitch().getState()) {
                robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
            }
            robot.moveArm(0);
            armOffset = robot.getRaisingArm1().getCurrentPosition();
            resetting = false;
        };
        latchMove = () -> {
            raisingArm = true;
            robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
            sleep(500);
            while (robot.getRaisingArm1().getCurrentPosition() - armOffset < 836 && opModeIsActive()) {
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
        armRaise = () -> {
            raisingArm = true;
            robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
            robot.getLid().setPosition(0.95);
            sleep(500);
            robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            while (opModeIsActive() && robot.getRaisingArm1().getCurrentPosition() - armOffset < ARM_RAISE_POSITION) {
                robot.moveArm(-1);
            }
            robot.moveArm(0);
            sleep(150);
            robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            raisingArm = false;
        };
        intakePixyProvider.start();
        waitForStart();
        while (opModeIsActive()) {
            intakeKickerUpdated = intakeKicker.isUpdated();
            intakeResetUpdated = intakeReset.isUpdated();
            armUpButtonUpdated = armUpButton.isUpdated();
            if (!slowingDown) {
                if (!slowButton.isPressed()) {
                    if(gamepad1.right_stick_y > 0.1 || gamepad1.right_stick_y < -0.1){
                        robot.drive((-1 * gamepad1.right_stick_y) + gamepad1.right_stick_y, (-1 * gamepad1.right_stick_y) - gamepad1.right_stick_x);
                    }else{
                        robot.drive(gamepad1.left_stick_y - gamepad1.left_stick_x, gamepad1.left_stick_y + gamepad1.left_stick_x); //Uses the left and right sticks to driveRange the robot
                    }
                } else {
                    if(gamepad1.right_stick_y > 0.1 || gamepad1.right_stick_y < -0.1){
                        robot.drive(((-1 * gamepad1.right_stick_y) + gamepad1.right_stick_y) * DRIVE_MODIFIER, ((-1 * gamepad1.right_stick_y) - gamepad1.right_stick_x) * DRIVE_MODIFIER);
                    }else{
                        robot.drive((gamepad1.left_stick_y - gamepad1.left_stick_x) * DRIVE_MODIFIER, (gamepad1.left_stick_y + gamepad1.left_stick_x) * DRIVE_MODIFIER); //Uses the left and right sticks to driveRange the robot
                    }
                }
                antiTipTimeTarget = System.currentTimeMillis() + 750;
            } else {
                robot.brakeDriveMotors(modifier);
            }
            if (intakeTrigger.isPressed()) {
                armMod = ARM_DOWN_PRESSURE;
                robot.intake(gamepad2.left_trigger);
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
                } else {
                    robot.getLid().setPosition(0.28);
                }
            } else {
                if (robot.getRaisingArm1().getCurrentPosition() - armOffset > 2200 && robot.getRaisingArm1().getCurrentPosition() - armOffset > 100) {
                    robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
                }
                robot.getLid().setPosition(0.85);
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
            if (!raisingArm && !resetting && !armUpButton.isPressed()) {
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
            if (!robot.getArmSwitch().getState() && !raisingArm) {
                robot.getIntakeLatch().setPosition(INTAKE_LATCH_OPEN);
            }
            if (armUpButton.isPressed() && armUpButtonUpdated) {
                threadManager.submit(armRaise);
            }
            if(intakePixyProvider.getStatus() == 0){
                //robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.CP2_SHOT);
            }else if(intakePixyProvider.getStatus() == 1){
                //robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED);
            }else if(intakePixyProvider.getStatus() == 2){
                //robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
            }
            max = Math.max(max, intakePixyProvider.silverAverage);
            telemetry.addData("Gild", intakePixyProvider.goldAverage);
            telemetry.addData("Bilver", intakePixyProvider.silverAverage);
            telemetry.addData("Max", max);
            telemetry.update();
        }
    }
}
