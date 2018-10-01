package org.ftc7244.robotcontroller.sensor.vuforia.initializer;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class PhoneCamInitializer extends CameraInitializer {
    private VuforiaLocalizer.CameraDirection direction;

    public PhoneCamInitializer(VuforiaLocalizer.CameraDirection direction){
        this.direction = direction;
    }

    @Override
    public void init(VuforiaLocalizer.Parameters parameters) {
        parameters.cameraDirection = direction;
    }
}
