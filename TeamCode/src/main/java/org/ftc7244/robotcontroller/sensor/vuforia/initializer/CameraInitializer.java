package org.ftc7244.robotcontroller.sensor.vuforia.initializer;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public abstract class CameraInitializer {
    /**
     * I WANT LAMBDAS IN ANDROID
     */
    public abstract boolean init(VuforiaLocalizer.Parameters parameters);
}