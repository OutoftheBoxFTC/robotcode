package org.ftc7244.robotcontroller.sensor.vuforia.initializer;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Deprecated
public abstract class CameraInitializer {
    /**
     * I WANT LAMBDAS IN ANDROID
     */
    public abstract boolean init(VuforiaLocalizer.Parameters parameters);

    public abstract void linkTargets(VuforiaTrackables targets);
}