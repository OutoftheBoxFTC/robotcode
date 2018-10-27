package org.ftc7244.robotcontroller.sensor.vuforia;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.RunnableSensorProvider;
import org.ftc7244.robotcontroller.sensor.VariablyInitializable;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.CameraInitializer;

@Deprecated
public class CameraOrientationProvider extends RunnableSensorProvider implements VariablyInitializable {

    private boolean showCameraFeed;
    private static final String VUFORIA_LISCENCE = "AUmZqRb/////AAABmVovv5V7wU+Yq0AT/j0llpg7JLpGJ/i/kVtxTuIjI43ZnFjmxAZJGY+UBD/e8pYFsPKm2hMyh1aZeCIgE7qZ30oUyVfzlF1PlXHTkeZ7uirb9SEC6xWtxGYzGy5uIbFQtCxF7kNZ9WroVI4CSLf6mlJvYe+hXdJxf3MUnYLzyU1hRuZjK5JRSxc+wvCviSnfwzsCRU+qgZ1kyUbnaxqskAiFSjyOLdvc03IGEgkFpz903ZZNSmTod/OHyPlhq1qVzcU7IJgrSheYGnoJYhiLELwIblrlxvrEq9fB3My/xcGMBjmfCJiAakb9NMSWEPhvbxMBkt3vT8zXMsIEf1XHO1G8Yddy9cQfJmVeS+V/2sbV";

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
    public boolean init() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        if(showCameraFeed) {
            parameters.fillCameraMonitorViewParent = false;
            HardwareMap hardwareMap = robot.getOpMode().hardwareMap;
            parameters.cameraMonitorViewIdParent=hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        }
        parameters.vuforiaLicenseKey = VUFORIA_LISCENCE;
        if(camera.init(parameters)){
            vuforia = ClassFactory.getInstance().createVuforia(parameters);
            vuforia.enableConvertFrameToBitmap();
            targets = vuforia.loadTrackablesFromAsset("RoverRuckus");
            targets.get(0).setName("blue_rover");
            targets.get(1).setName("red_footprint");
            targets.get(2).setName("front_craters");
            targets.get(3).setName("back_space");
            camera.linkTargets(targets);
            robot.getOpMode().telemetry.addLine("Initialized Phone");
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        targets.activate();
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
        targets.deactivate();
    }

    public Orientation getRotation(){
        return orientation==null?new Orientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, Float.NaN, Float.NaN, Float.NaN, 0):orientation;
    }

    public double getX(){
        return translation==null?Double.POSITIVE_INFINITY:translation.get(0);
    }

    public double getY(){
        return translation==null?Double.POSITIVE_INFINITY:translation.get(0);
    }

    public double getR(){
        return 0;
        //TODO FINISH ME
    }

    public VectorF getTranslation(){
        return translation==null?new VectorF(Float.NaN, Float.NaN, Float.NaN):translation;
    }
}