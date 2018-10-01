package org.ftc7244.robotcontroller.sensor.vuforia;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.SensorProvider;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.PhoneCamInitializer;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.WebCamInitializer;

import java.util.concurrent.ExecutorService;

public class CameraSystem extends SensorProvider {
    private CameraOrientationProvider phone, w1, w2;
    public CameraSystem(Robot robot){
        phone = new CameraOrientationProvider(false, new PhoneCamInitializer(VuforiaLocalizer.CameraDirection.BACK), robot);
        w1 = new CameraOrientationProvider(false, new WebCamInitializer(robot.getW1()), robot);
        w2 = new CameraOrientationProvider(false, new WebCamInitializer(robot.getW2()), robot);
    }

    @Override
    public void init() {
        phone.init();
        w1.init();
        w2.init();
    }

    public void run(ExecutorService threadManager){
        threadManager.execute(phone);
        threadManager.execute(w1);
        threadManager.execute(w2);
    }

    public Orientation getRotation(InformationProvider provider){
        switch (provider){
            case W1:
                return w1.getRotation();
            case W2:
                return w2.getRotation();
            case PHONE:
                return phone.getRotation();
            case BEST:
                return getBestProvider().getRotation();
        }
        return null;
    }

    public VectorF getTranslation(InformationProvider provider){
        switch (provider){
            case W1:
                return w1.getTranslation();
            case W2:
                return w2.getTranslation();
            case PHONE:
                return phone.getTranslation();
            case BEST:
                return getBestProvider().getTranslation();
        }
        return null;
    }

    private CameraOrientationProvider getBestProvider(){
        return phone;
        //TODO
    }

    public enum InformationProvider{
        PHONE,
        W1,
        W2,
        BEST
    }
}