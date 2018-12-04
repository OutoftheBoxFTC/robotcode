package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

@TeleOp(name = "Gyroscope Test")
public class GyroscopeTest extends AutonomousProcedure {

    @Override
    protected void run() {
        driveController.orient(0, 0, 0);
        while (opModeIsActive()){
            telemetry.addData("Reading", gyroscope.getRotation(GyroscopeProvider.Axis.YAW));
            telemetry.update();
        }
    }
}