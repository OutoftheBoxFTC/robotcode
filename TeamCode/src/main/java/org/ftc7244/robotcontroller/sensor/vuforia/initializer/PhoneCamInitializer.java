package org.ftc7244.robotcontroller.sensor.vuforia.initializer;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


@Deprecated
public class PhoneCamInitializer extends CameraInitializer {
    private VuforiaLocalizer.CameraDirection direction;

    public PhoneCamInitializer(VuforiaLocalizer.CameraDirection direction){
        this.direction = direction;
    }

    @Override
    public boolean init(VuforiaLocalizer.Parameters parameters) {
        parameters.cameraDirection = direction;
        return true;
    }

    @Override
    public void linkTargets(VuforiaTrackables targets) {
        for (VuforiaTrackable trackable : targets) {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(OpenGLMatrix.translation(0, 0, 0), direction);
        }
    }
}