package org.ftc7244.robotcontroller.opmodes.tests;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;

public class EncoderAverageCounter extends AutonomousProcedure {

    @Override
    protected void run() {
        driveController.orient(0, 0, 0);
        while (opModeIsActive()){
            telemetry.addData("Ticks", robot.getDriveEncoderAverage());
            telemetry.addData("Inches", robot.getDriveEncoderAverage()/robot.getCountsPerInch());
            telemetry.update();
        }
    }
}