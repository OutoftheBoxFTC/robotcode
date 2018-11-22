package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;

@TeleOp(name = "Encoder Count")
public class EncoderAverageCounter extends AutonomousProcedure {

    @Override
    protected void run() {
        driveController.orient(0, 0, 0);
        double leftOffset = robot.getLeftDrive().getCurrentPosition(),
                rightOffset = robot.getRightDrive().getCurrentPosition(),
                avgOffset = robot.getDriveEncoderAverage();
        while (opModeIsActive()){
            telemetry.addData("Avg", robot.getDriveEncoderAverage()-avgOffset);
            telemetry.addData("Left", robot.getLeftDrive().getCurrentPosition()-leftOffset);
            telemetry.addData("Right", robot.getRightDrive().getCurrentPosition()-rightOffset);
            telemetry.update();
        }
    }
}