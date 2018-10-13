package org.ftc7244.robotcontroller.opmodes.tests;

import org.ftc7244.robotcontroller.autonamous.AutonamousProcedure;
import org.ftc7244.robotcontroller.sensor.ultrasonic.UltrasonicSystem;

public class UltrasonicSystemTest extends AutonamousProcedure {
    @Override
    protected void run() {
        while (opModeIsActive()) {
            telemetry.addData("Error", ultrasonic.getError(UltrasonicSystem.Side.LEFT));
            telemetry.update();
        }
    }
}