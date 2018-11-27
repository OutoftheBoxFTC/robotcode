package org.ftc7244.robotcontroller.sensor.pixycam;

public class PixycamSample {

    PixycamProvider pixy;
    public PixycamSample(PixycamProvider pixy){
        this.pixy = pixy;
    }

    public boolean start() {
        pixy.start();
        return pixy.isEngaged();
    }

    public SampleTransform run(){
        pixy.update();
        if(pixy.getX() < 62){
            return SampleTransform.RIGHT;
        }else if(pixy.getX() > 189){
            return SampleTransform.LEFT;
        }else{
            return SampleTransform.CENTER;
        }
    }

    public enum SampleTransform{
        LEFT,
        RIGHT,
        CENTER
    }
}
