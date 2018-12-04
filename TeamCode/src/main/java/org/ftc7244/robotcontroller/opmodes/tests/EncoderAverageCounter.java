package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;

@TeleOp(name = "Encoder Count")
public class EncoderAverageCounter extends AutonomousProcedure {

    @Override
    protected void run() {
        driveController.orient(0, 0, 0);
        while (opModeIsActive()){
            telemetry.addData("Avg", robot.getDriveEncoderAverage());
            telemetry.addData("Left", robot.getLeftDrive().getCurrentPosition());
            telemetry.addData("Right", robot.getRightDrive().getCurrentPosition());
            telemetry.update();
        }
    }
}