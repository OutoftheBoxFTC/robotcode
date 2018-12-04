package org.ftc7244.robotcontroller.sensor.vuforia;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.Initializable;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.PhoneCamInitializer;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.WebCamInitializer;

import java.util.concurrent.ExecutorService;

@Deprecated
public class CameraSystem implements Initializable {
    private CameraOrientationProvider phone, w1, w2;
    public CameraSystem(Robot robot){
        phone = new CameraOrientationProvider(true, new PhoneCamInitializer(VuforiaLocalizer.CameraDirection.BACK), robot);
        w1 = new CameraOrientationProvider(false, new WebCamInitializer(robot.getW1()), robot);
        w2 = new CameraOrientationProvider(false, new WebCamInitializer(robot.getW2()), robot);
    }

    @Override
    public void init(Robot robot) {
        if(!phone.init()){
            phone = null;
            robot.getOpMode().telemetry.addLine("Unable to initialize phone camera");
        }
        if(!w1.init()) {
            w1 = null;
            robot.getOpMode().telemetry.addLine("Unable to initialize webcam 1");
        }
        if(!w2.init()) {
            w2 = null;
            robot.getOpMode().telemetry.addLine("Unable to initialize webcam 2");
        }
    }

    public void run(ExecutorService threadManager){
        if(phone != null)threadManager.execute(phone);
        if(w1 != null)threadManager.execute(w1);
        if(w2 != null)threadManager.execute(w2);
    }

    public Orientation getRotation(InformationProvider provider){
        switch (provider){
            case W1:
                return w1==null?new Orientation():w1.getRotation();
            case W2:
                return w2==null?new Orientation():w2.getRotation();
            case PHONE:
                return phone==null?new Orientation():phone.getRotation();
        }
        return null;
    }

    public VectorF getTranslation(InformationProvider provider){
        switch (provider){
            case W1:
                return w1==null?new VectorF(0, 0, 0):w1.getTranslation();
            case W2:
                return w2==null?new VectorF(0, 0, 0):w2.getTranslation();
            case PHONE:
                return phone==null?new VectorF(0, 0, 0):phone.getTranslation();
        }
        return null;
    }

    public enum InformationProvider{
        PHONE,
        W1,
        W2,
    }
}