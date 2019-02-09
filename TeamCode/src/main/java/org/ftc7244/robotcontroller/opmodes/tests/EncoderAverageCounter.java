package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;

@TeleOp(name = "Encoder Count")
@Disabled
public class EncoderAverageCounter extends AutonomousProcedure {

    @Override
    protected void run() {
        while (opModeIsActive()){
            telemetry.addData("Test", robot.getRaisingArm1().getCurrentPosition());
            telemetry.addData("Test", robot.getRaisingArm2().getCurrentPosition());
            telemetry.update();
        }
    }
}