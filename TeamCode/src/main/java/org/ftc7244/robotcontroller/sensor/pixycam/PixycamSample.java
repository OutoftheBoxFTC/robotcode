package org.ftc7244.robotcontroller.sensor.pixycam;

public class PixycamSample {

    Pixycam2Provider pixy;
    SampleTransform[] filter;
    int index;
    public PixycamSample(Pixycam2Provider pixy){
        this.pixy = pixy;
        filter = new SampleTransform[10];
        index = 0;
        for(int i = 0; i < filter.length; i ++){
            filter[i] = SampleTransform.LEFT;
        }
    }

    public boolean start() {
        pixy.start();
        return pixy.isEngaged();
    }

    public SampleTransform run(){
        pixy.update();
        if(pixy.getX() != -1) {
            if (pixy.getX() < 100) {
                filter[index] = SampleTransform.LEFT;
                return SampleTransform.LEFT;
            } else if (pixy.getX() > 200) {
                filter[index] = SampleTransform.RIGHT;
            } else {
                filter[index] = SampleTransform.CENTER;
            }
            index++;
        }
        if(index == filter.length){
            index = 0;
        }
        return getAverage(filter);
    }

    public SampleTransform getAverage(SampleTransform[] arr){
        int left = 0, right = 0, center = 0, max = 0;
        for(int i = 0; i < arr.length; i ++){
            if(arr[i] == SampleTransform.LEFT){
                left ++;
            }else if(arr[i] == SampleTransform.RIGHT){
                right ++;
            }else {
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