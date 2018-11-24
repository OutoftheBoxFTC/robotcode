package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

import static org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider.Axis.PITCH;

@TeleOp(name = "Gyroscope Test")
public class GyroscopeTest extends AutonomousProcedure {

    @Override
    protected void run() {
        double offset = 0;
        driveController.orient(0, 0, 0);
        offset = gyroscope.getRotation(PITCH);
        while (opModeIsActive()){
            telemetry.addData("Reading",  Math.toDegrees(Math.abs(gyroscope.getRotation(PITCH) - offset)));
            telemetry.update();
        }
    }
}