package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonamousProcedure;

@TeleOp(name = "Ultrasonic Test")
public class UltrasonicTest extends AutonamousProcedure {

    @Override
    protected void run() {
        while (opModeIsActive()){
            telemetry.addData("Reading", robot.getTestUS().getUltrasonicLevel());
            telemetry.update();
        }
    }
}
