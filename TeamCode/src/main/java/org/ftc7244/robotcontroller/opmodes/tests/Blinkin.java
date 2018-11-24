package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
@TeleOp
public class Blinkin extends AutonomousProcedure {
    int[] modes = {69, 72, 77, 78};
    double timer = 0;
    int increment = 0;
    @Override
    protected void run() throws InterruptedException {
        timer = System.currentTimeMillis() + 5000;
        while(opModeIsActive()) {
            if (System.currentTimeMillis() >= timer) {
                if (increment <= modes.length - 1) {
                    increment++;
                } else {
                    increment = 0;
                }
                timer = System.currentTimeMillis() + 5000;
                robot.getBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.fromNumber(modes[increment]));
            }
        }
    }
}
