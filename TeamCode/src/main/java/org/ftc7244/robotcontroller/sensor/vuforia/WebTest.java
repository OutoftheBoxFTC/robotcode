package org.ftc7244.robotcontroller.sensor.vuforia;

import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.PhoneCamInitializer;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.WebCamInitializer;

public class WebTest {
    SynchronousCameraProvider web;
    public WebTest(){}
    public boolean init(Robot robot){
        web = new SynchronousCameraProvider(true, new WebCamInitializer(robot.getW1()), robot);
        return web.init();
    }
    public VectorF getTranslation(){
        return web.getTranslation();
    }
    public void start(){
        web.start();
    }
    public void run(){
        web.run();
    }
    public void stop(){
        web.stop();
    }
}
