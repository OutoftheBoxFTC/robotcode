package org.ftc7244.robotcontroller.sensor.pixycam;

public class PixycamSample {

    Pixycam2Provider pixy;
    SampleTransform previous;
    int index;
    int left = 0;
    int right = 0;
    int center = 0;
    int max = 0;
    int target = 11;
    public PixycamSample(Pixycam2Provider pixy){
        this.pixy = pixy;
        index = 0;
    }

    public boolean start() {
        pixy.start();
        return pixy.isEngaged();
    }

    public SampleTransform run(){
        pixy.update();
        if(pixy.getX() != -1) {
            index++;
            if(index == target){
                index = 0;
                left = 0;
                right = 0;
                center = 0;
                target = (int)((Math.round(Math.random() * 20)) + 10);
            }
            if (pixy.getX() < 100) {
                left ++;
            } else if (pixy.getX() > 210) {
                right ++;
            } else {
                center ++;
            }
        }
        max = Math.max(left, right);
        max = Math.max(max, center);
        if(max == left){
            return SampleTransform.LEFT;
        }else if(max == right){
            return SampleTransform.RIGHT;
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
