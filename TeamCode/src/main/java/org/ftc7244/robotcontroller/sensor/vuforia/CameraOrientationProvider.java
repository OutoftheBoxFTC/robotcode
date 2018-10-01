package org.ftc7244.robotcontroller.sensor.vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.RunnableSensorProvider;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.CameraInitializer;

public class CameraOrientationProvider extends RunnableSensorProvider {

    private boolean showCameraFeed;
    private static final String VUFORIA_LISCENCE = "AQ7YHUT/////AAAAGVOxmiN4SkHwqyEIEpsDKxo9Gpbkev2MCSd8RFB1jHcnH21ZDRyGXPK9hwVuWRRN4eiOU42jJhNeOiOlyh7yAdqsjfotKCW71TMFv7OiZr7uw6kS049r5LuvfMrxc9DyfDVCRh8aViWYNSuJVAGk6nF8D9dC9i5hy1FQFCRN3wxdQ49o/YqMfLeQNMgQIW/K3fqLi8ez+Ek9cF0mH1SGqBcv6dJrRavFqV/twq9F9fK+yW1rwcAQGunLKu2g6p0r1YXeSQe0qiMkfwumVwb2Sq0ZmEKQjHV4hwm14opyvtbXZzJwHppKOmBC0XXpkCBs7xLcYgoGbEiiGwEQv+N1xVnRha3NZXCmHH44JweTvmbh";

    private VuforiaLocalizer vuforia;
    private CameraInitializer camera;
    private Robot robot;
    private VuforiaTrackables targets;
    private OpenGLMatrix orientationMatrix;
    private Orientation orientation;
    private VectorF translation;

    public CameraOrientationProvider(boolean showCameraFeed, CameraInitializer camera, Robot robot){
        this.showCameraFeed = showCameraFeed;
        this.camera = camera;
        this.robot = robot;
    }

    @Override
    public void init() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_LISCENCE;
        camera.init(parameters);
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
        vuforia.enableConvertFrameToBitmap();
        targets = vuforia.loadTrackablesFromAsset("RoverRuckus");
        targets.get(0).setName("blue_rover");
        targets.get(1).setName("red_footprint");
        targets.get(2).setName("front_craters");
        targets.get(4).setName("back_space");

    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            for (int i = 0; i < targets.size(); i++) {
                VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener)targets.get(i).getListener();
                if(listener.isVisible()){
                    i=targets.size();
                    orientationMatrix = listener.getUpdatedRobotLocation();
                    translation = orientationMatrix.getTranslation();
                    orientation = Orientation.getOrientation(orientationMatrix, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
                }
            }
        }
    }

    public Orientation getRotation(){
        return orientation;
    }

    public VectorF getTranslation(){
        return translation;
    }
}