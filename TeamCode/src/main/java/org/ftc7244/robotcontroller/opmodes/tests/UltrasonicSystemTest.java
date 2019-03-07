package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.sensor.ultrasonic.UltrasonicSystem;

@TeleOp(name = "Ultrasonic System Test", group = "Debug")
public class UltrasonicSystemTest extends AutonomousProcedure {
    @Override
    protected void run() {
        while (opModeIsActive()) {
            telemetry.addData("LeftFront", robot.getLeadingLeftUS().getUltrasonicLevel());
            telemetry.update();
        }
    }
}
