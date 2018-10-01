package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonamousProcedure;

@TeleOp(name = "Vuforia Camera System Test")
public class WebcamSystemTest extends AutonamousProcedure {

    @Override
    protected void run() {

        while (opModeIsActive()){
            telemetry.addData("X", vuforia.getRotation())
        }
    }
}
