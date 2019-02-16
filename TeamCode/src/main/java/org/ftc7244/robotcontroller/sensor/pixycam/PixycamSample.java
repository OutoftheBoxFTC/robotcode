package org.ftc7244.robotcontroller.sensor.pixycam;

public class PixycamSample {

    Pixycam2Provider pixy;
    SampleTransform previous;
    int[] filter;
    int index;
    public PixycamSample(Pixycam2Provider pixy){
        this.pixy = pixy;
        filter = new int[10];
        index = 0;
        for(int i = 0; i < filter.length; i ++){
            filter[i] = 0;
        }
    }

    public boolean start() {
        pixy.start();
        return pixy.isEngaged();
    }

    public SampleTransform run(){
        pixy.update();
        if(pixy.getX() != -1) {
            filter[index] = pixy.getX();
            index++;
        }
        if(index == filter.length){
            index = 0;
        }
        if (getAverage(filter) < 100) {
            previous = SampleTransform.LEFT;
            return SampleTransform.LEFT;
        } else if (getAverage(filter) > 250) {
            previous = SampleTransform.RIGHT;
            return SampleTransform.RIGHT;
        } else {
            previous = SampleTransform.CENTER;
            return SampleTransform.CENTER;
        }
    }

    public int getAverage(int[] arr){
        int sum = 0;
        for(int i = 0; i < arr.length; i ++){
            sum += arr[i];
        }
        sum /= arr.length;
        return sum;
    }

    public enum SampleTransform{
        LEFT,
        RIGHT,
        CENTER
    }
}
