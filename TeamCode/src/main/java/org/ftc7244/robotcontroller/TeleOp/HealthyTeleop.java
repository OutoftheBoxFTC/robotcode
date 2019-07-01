package org.ftc7244.robotcontroller.TeleOp;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.ftc7244.robotcontroller.hardware.SmartRobot;
import org.ftc7244.robotcontroller.opmodes.input.Button;
import org.ftc7244.robotcontroller.opmodes.input.ButtonType;
import org.ftc7244.robotcontroller.opmodes.input.PressButton;
import org.ftc7244.robotcontroller.sensor.pixycam.IntakePixyProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TeleOp(name="Healthy Teleop")
public class HealthyTeleop extends LinearOpMode {
    private static final double ARM_DOWN_PRESSURE = 0.05,
            ARM_HANG_OFFSET = 0.3,
            ANTI_TIP_TRIGGER_SPEED = 243,
            DRIVE_COEFFICIENT = 0.5,
            INTAKE_LATCH_OPEN = 0.05,
            INTAKE_LATCH_CLOSED = 0.95,
            ARM_RAISE_POSITION = 2550.0,
            LATCH_OPEN = 0.7,
            LATCH_CLOSED = 0.2,
            LID_OPEN = 0.28,
            LID_CLOSED = 0.82;

    private SmartRobot robot;

    private double modifier, armOffset, armMod;

    private boolean lidOpened = true;

    private int armPosition;
    private boolean armSwitchPressed;


    private ExecutorService threadManager;

    private StateMachine latchMove, antiTip, resetArm, engageArm;

    private I2cDeviceSynch pixy;
    private IntakePixyProvider intakePixyProvider;
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
    PressButton latchButton;

    @Override
    public void runOpMode() {
        threadManager = Executors.newCachedThreadPool();
        robot = new SmartRobot(this);
        robot.init();
        robot.initServos();
        pixy = robot.getIntakeI2c();
        intakePixyProvider = new IntakePixyProvider(pixy, robot);

        slowButton = new PressButton(gamepad1, ButtonType.RIGHT_TRIGGER);
        intakeTrigger = new Button(gamepad2, ButtonType.LEFT_TRIGGER);
        outtakeTrigger = new Button(gamepad2, ButtonType.RIGHT_TRIGGER);
        lidButton = new Button(gamepad2, ButtonType.RIGHT_BUMPER);
        latchButton = new PressButton(gamepad2, ButtonType.B);
        armLockButton = new PressButton(gamepad2, ButtonType.D_PAD_UP);
        intakeKicker = new Button(gamepad2, ButtonType.Y); //New servo made refer to notes below, ask builders about this.
        intakeReset = new Button(gamepad2, ButtonType.X);
        armUpButton = new Button(gamepad2, ButtonType.A);

        modifier = 1;
        armMod = 1;

        resetArm = new StateMachine(() -> {
            robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
            robot.moveArm(1);
            robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
            resetArm.advance();
        }, () -> {
            if(armSwitchPressed){
                robot.moveArm(0);
                resetArm.advance();
            } else {
                double power = resetArm.getElapsedTime() / 1000.0;
                if (power > 0.1) {
                    robot.moveArm(power);
                }
            }
        });

        latchMove = new StateMachine(() -> {
            robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
            robot.getLid().setPosition(LID_CLOSED);
            latchMove.advance(500);
        }, () -> {
            robot.moveArm(-1);
            latchMove.advance();
        }, () -> {
            if(armPosition < 300){
                robot.moveArm(0);
                latchMove.advance();
            }
        });

        antiTip = new StateMachine(() -> antiTip.advance(30),
                () -> {
            modifier = 0;
            antiTip.advance(720);
        });

        engageArm = new StateMachine(() -> {
            robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
            engageArm.advance(500);
        }, () -> {
            robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.moveArm(-1);
            engageArm.advance();
        }, () -> {
            if(armPosition >= 500){
                robot.getLid().setPosition(0.85);
                engageArm.advance();
            }
        }, () ->{
            if(armPosition >= ARM_RAISE_POSITION){
                robot.moveArm(0);
                engageArm.advance(150);
            }
        }, () -> {
            robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            engageArm.advance();
        });

        StateMachine[] machines = new StateMachine[]{resetArm, engageArm, antiTip, latchMove};

        waitForStart();
        threadManager.execute(intakePixyProvider);
        long start = System.currentTimeMillis();

        while (opModeIsActive()) {
            boolean intakeKickerUpdated = intakeKicker.isUpdated(),
                    intakeResetUpdated = intakeReset.isUpdated(),
                    armUpButtonUpdated = armUpButton.isUpdated();

            robot.bulkRead();
            //TODO do via bulk read
            double vLeft = robot.getLeftDrive().getMotor().getVelocity(AngleUnit.DEGREES);
            double vRight = robot.getRightDrive().getMotor().getVelocity(AngleUnit.DEGREES);

            armPosition = robot.getRaisingArm1().getCurrentPosition();
            armSwitchPressed = !robot.armSwitchState();
            if (armSwitchPressed) {
                armOffset = armPosition;
            }
            armPosition -= armOffset;

            for(StateMachine machine : machines){
                machine.update();
            }

            double left = gamepad1.left_stick_y,
                    right = gamepad1.right_stick_y,
                    intake = 0,
                    latch = LATCH_CLOSED;

            if(antiTip.isRunning()){
                left = modifier;
                right = modifier;
            } else{
                int leftPolarity = vLeft <0?-1:1, rightPolarity = vRight <0?-1:1;
                if(leftPolarity==rightPolarity){
                    if(vLeft *leftPolarity>ANTI_TIP_TRIGGER_SPEED&& vRight *rightPolarity>ANTI_TIP_TRIGGER_SPEED){
                        if(gamepad1.left_stick_y*leftPolarity<-0.5&&gamepad1.right_stick_y*rightPolarity<-0.5){
                            modifier = -leftPolarity;
                            antiTip.advance();
                        }
                    }
                }
                if(slowButton.isPressed()){
                    left *= DRIVE_COEFFICIENT;
                    right *= DRIVE_COEFFICIENT;
                }
            }
            robot.drive(left, right);

            if (intakeTrigger.isPressed()) {
                armMod = ARM_DOWN_PRESSURE;
                intake = 1;
            } else if (outtakeTrigger.isPressed()) {
                armMod = 0;
                intake = -1;
            }
            robot.intake(intake);

            if (latchButton.isPressed()) {
                latch = LATCH_OPEN;
            }
            robot.getLatch().setPosition(latch);

            if (lidButton.isPressed()) {
                robot.getLid().setPosition(LID_OPEN);
                if (armPosition > 2200) {
                    robot.getIntakeLatch().setPosition(INTAKE_LATCH_OPEN);
                    lidOpened = true;
                }
            } else if(armSwitchPressed){
                if (armPosition > 2200) {
                    robot.getIntakeLatch().setPosition(INTAKE_LATCH_CLOSED);
                }
                robot.getLid().setPosition(LID_CLOSED);
            }else if(!raisingArm()){
                if(armPosition > 2200 && lidOpened){
                    robot.getLid().setPosition(LID_OPEN);
                }else {
                    lidOpened = false;
                    robot.getLid().setPosition(LID_CLOSED);
                }
            }
            if (armLockButton.isPressed()) {
                armMod = ARM_HANG_OFFSET;
            } else if (!intakeTrigger.isPressed()) {
                armMod = 0;
            }
            if (intakeReset.isPressed() && intakeResetUpdated) {
                resetArm.advance();
            }
            if (!raisingArm() && !resetArm.isRunning() && !armUpButton.isPressed()) {
                if (armPosition < 100) {
                    if (gamepad2.right_stick_y <= 0) {
                        robot.getRaisingArm1().setPower((Math.pow(gamepad2.right_stick_y, 2) + armMod) * -1);
                        robot.getRaisingArm2().setPower((Math.pow(gamepad2.right_stick_y, 2) + armMod) * -1);
                    }
                } else {
                    robot.getRaisingArm1().setPower((gamepad2.right_stick_y + armMod) * -1);
                    robot.getRaisingArm2().setPower((gamepad2.right_stick_y + armMod) * -1);
                }
            }
            if (intakeKicker.isPressed() && !raisingArm() && intakeKickerUpdated) {
                latchMove.advance();
            }
            if (armSwitchPressed && !raisingArm()) {
                robot.getIntakeLatch().setPosition(INTAKE_LATCH_OPEN);
            }
            if (armUpButton.isPressed() && armUpButtonUpdated) {
                engageArm.advance();
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
            if(armSwitchPressed){
                robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            }else{
                robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
            long now = System.currentTimeMillis();
            double fps = 1000.0/(now- start);
            start = now;
            telemetry.addData("Status", intakePixyProvider.getStatus());
            telemetry.addData("Main loop FPS", fps);
            telemetry.addData("Filter loop FPS", intakePixyProvider.getFps());

            telemetry.update();
        }
    }

    private boolean raisingArm(){
        return latchMove.isRunning()||engageArm.isRunning();
    }
}
