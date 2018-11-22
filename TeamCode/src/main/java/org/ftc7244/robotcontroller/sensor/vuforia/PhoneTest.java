package org.ftc7244.robotcontroller.sensor.vuforia;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.PhoneCamInitializer;

public class PhoneTest {
    SynchronousCameraProvider phone;
    public PhoneTest(){}
    public boolean init(Robot robot){
        phone = new SynchronousCameraProvider(true, new PhoneCamInitializer(VuforiaLocalizer.CameraDirection.BACK), robot);
        return phone.init();
    }
    public VectorF getTranslation(){
        return phone.getTranslation();
    }
    public void start(){
        phone.start();
    }
    public void run(){
        phone.run();
    }
    public void stop(){
        phone.stop();
    }
}
