package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.sensor.ultrasonic.UltrasonicSystem;

@TeleOp(name = "Ultrasonic System Test", group = "Debug")
public class UltrasonicSystemTest extends AutonomousProcedure {
    @Override
    protected void run() {
        double target = 500 + robot.getDriveEncoderAverage();
        double error = 0;
        ultrasonic.setWall(UltrasonicSystem.Wall.BLUE);
        while (opModeIsActive() && robot.getDriveEncoderAverage() < target) {
            telemetry.addData("Error", ultrasonic.getRotationalOffset(UltrasonicSystem.Side.LEFT));
            telemetry.update();
            error = ultrasonic.getRotationalOffset(UltrasonicSystem.Side.LEFT);
            robot.drive(0.3 - error, 0.3 + error);
        }
    }
}
