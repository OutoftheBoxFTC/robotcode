package org.ftc7244.robotcontroller.sensor.vuforia.initializer;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class WebCamInitializer extends CameraInitializer {
    private WebcamName webcam;

    public WebCamInitializer(WebcamName webcam){
        this.webcam = webcam;
    }

    @Override
    public boolean init(VuforiaLocalizer.Parameters parameters) {
        if(webcam == null)
            return false;
        parameters.cameraName = webcam;
        return true;
    }
}