package org.ftc7244.robotcontroller.sensor.pixycam;

public class PixycamSample {

    PixycamProvider pixy;
    PixyCam2Provider pixy2;
    public PixycamSample(PixycamProvider pixy){
        this.pixy = pixy;
    }
    public PixycamSample(PixyCam2Provider pixy){
        this.pixy2 = pixy;
    }
    public boolean start() {
        if(pixy2 == null) {
            pixy.start();
            return pixy.isEngaged();
        }else{
            pixy2.start();
            return pixy2.isEngaged();
        }
    }

    public SampleTransform run(){
        if(pixy2 == null) {
            pixy.update();
            if (pixy.getX() < 62) {
                return SampleTransform.LEFT;
            } else if (pixy.getX() > 189) {
                return SampleTransform.RIGHT;
            } else {
                return SampleTransform.CENTER;
            }
        }else{
            pixy2.update();
            if (pixy2.getX() < 62) {
                return SampleTransform.LEFT;
            } else if (pixy2.getX() > 189) {
                return SampleTransform.RIGHT;
            } else {
                return SampleTransform.CENTER;
            }
        }
    }

    public enum SampleTransform{
        LEFT,
        RIGHT,
        CENTER
    }
}
