package org.ftc7244.robotcontroller.sensor.pixycam;

public class PixycamSample {

    Pixycam2Provider pixy;
    SampleTransform previous;
    public PixycamSample(Pixycam2Provider pixy){
        this.pixy = pixy;
    }

    public boolean start() {
        pixy.start();
        return pixy.isEngaged();
    }

    public SampleTransform run(){
        pixy.update();
        if(pixy.getX() != -1) {
            if (pixy.getX() < 100) {
                previous = SampleTransform.LEFT;
                return SampleTransform.LEFT;
            } else if (pixy.getX() > 250) {
                previous = SampleTransform.RIGHT;
                return SampleTransform.RIGHT;
            } else {
                previous = SampleTransform.CENTER;
                return SampleTransform.CENTER;
            }
        }else{
            return previous;
        }
    }

    public enum SampleTransform{
        LEFT,
        RIGHT,
        CENTER
    }
}
