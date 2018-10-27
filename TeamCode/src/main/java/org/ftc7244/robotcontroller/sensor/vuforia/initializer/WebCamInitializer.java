package org.ftc7244.robotcontroller.sensor.vuforia.initializer;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

@Deprecated
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

    @Override
    public void linkTargets(VuforiaTrackables targets) {
        for (VuforiaTrackable trackable : targets) {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setCameraLocationOnRobot(webcam, OpenGLMatrix.translation(0, 0, 0));
        }
    }
}