package org.ftc7244.robotcontroller.opmodes.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.opmodes.input.Button;
import org.ftc7244.robotcontroller.opmodes.input.ButtonType;
import org.ftc7244.robotcontroller.opmodes.input.PressButton;

@TeleOp
public class CountsPerInch extends LinearOpMode {
    Robot robot = new Robot(this);
    double countsPerInch = 0, target = 0, initial;
    Button a, b;
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        a = new Button(gamepad1, ButtonType.A);
        b = new PressButton(gamepad1, ButtonType.B);
        waitForStart();
        robot.configureDriveMotors(DcMotor.ZeroPowerBehavior.BRAKE, null);
        initial = robot.getDriveEncoderAverage();
        while (opModeIsActive()){
            telemetry.addLine("A to reset");
            telemetry.addLine("B to lock");
            if(!b.isPressed()){
                countsPerInch = ((robot.getDriveEncoderAverage() - initial) / 36);
                //countsPerInch = 21.8;
            }
            if(a.isPressed()){
                initial = robot.getDriveEncoderAverage();
            }
            if(gamepad1.dpad_up) {
                target = robot.getDriveEncoderAverage() + (12 * countsPerInch);
            }else if(gamepad1.dpad_down){
                target = robot.getDriveEncoderAverage() + (48 * countsPerInch);
            }
            if(target > robot.getDriveEncoderAverage() && target != 0){
                robot.drive(-0.5, -0.5);
            }else{
                robot.drive(0, 0);
                target = 0;
            }
            telemetry.addData("Counts Per Inch", countsPerInch);
            telemetry.update();
        }
    }
}
