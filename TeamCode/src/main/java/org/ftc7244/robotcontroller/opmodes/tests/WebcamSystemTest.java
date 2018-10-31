package org.ftc7244.robotcontroller.opmodes.tests;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.ftc7244.robotcontroller.autonamous.AutonamousProcedure;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.vuforia.CameraOrientationProvider;
import org.ftc7244.robotcontroller.sensor.vuforia.CameraSystem;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.PhoneCamInitializer;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.WebCamInitializer;

@TeleOp(name = "Vuforia Camera System Test")
public class WebcamSystemTest extends AutonamousProcedure {
    Robot robot = new Robot(this);

    @Override
    protected void run() {
        while (opModeIsActive()) {
            telemetry.addData("Test", vuforia.getTranslation(CameraSystem.InformationProvider.W1).getData()[0]);
            telemetry.update();
        }
    }
}
