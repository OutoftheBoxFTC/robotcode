package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonamousProcedure;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.vuforia.CameraOrientationProvider;
import org.ftc7244.robotcontroller.sensor.vuforia.CameraSystem;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.WebCamInitializer;

@TeleOp(name = "Vuforia Camera System Test")
public class WebcamSystemTest extends OpMode {
    CameraOrientationProvider vuforia;
    Robot robot = new Robot(this);
    @Override
    public void init() {
        robot.init();
        vuforia = new CameraOrientationProvider(true, new WebCamInitializer(robot.getW1()), robot);
        vuforia.init();
    }

    @Override
    public void loop() {
        telemetry.addData("Test", vuforia.getTranslation().get(1));
        telemetry.update();
    }
}
