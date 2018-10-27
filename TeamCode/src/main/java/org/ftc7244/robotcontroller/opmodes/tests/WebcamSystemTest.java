package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.sensor.vuforia.CameraSystem;

@TeleOp(name = "Vuforia Camera System Test")
public class WebcamSystemTest extends AutonomousProcedure {

    @Override
    protected void run() {
        while (opModeIsActive()){
            telemetry.addData("X", vuforia.getTranslation(CameraSystem.InformationProvider.PHONE).get(0));
            telemetry.addData("Y", vuforia.getTranslation(CameraSystem.InformationProvider.PHONE).get(1));
            telemetry.addData("Z", vuforia.getTranslation(CameraSystem.InformationProvider.PHONE).get(2));
            telemetry.addData("R0", vuforia.getRotation(CameraSystem.InformationProvider.PHONE).firstAngle);
            telemetry.addData("R1", vuforia.getRotation(CameraSystem.InformationProvider.PHONE).secondAngle);
            telemetry.addData("R2", vuforia.getRotation(CameraSystem.InformationProvider.PHONE).thirdAngle);
            telemetry.update();
        }
    }
}
