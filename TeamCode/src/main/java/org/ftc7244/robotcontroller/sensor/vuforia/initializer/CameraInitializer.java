package org.ftc7244.robotcontroller.sensor.vuforia.initializer;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.ftc7244.robotcontroller.hardware.Robot;

public abstract class CameraInitializer {
    /**
     * I WANT LAMBDAS IN ANDROID
     */
    public abstract void init(VuforiaLocalizer.Parameters parameters);
}