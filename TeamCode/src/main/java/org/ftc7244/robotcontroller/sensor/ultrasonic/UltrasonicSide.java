package org.ftc7244.robotcontroller.sensor.ultrasonic;

public class UltrasonicSide {

    private double usDistance;
    private SickUltrasonic us1, us2;
    public UltrasonicSide(SickUltrasonic us1, SickUltrasonic us2, double usDistance){
        this.us1 = us1;
        this.us2 = us2;
        this.usDistance = usDistance;
    }

    public double getError(){
        if(us1.getUltrasonicLevel()==us1.getMode().getCap()||us2.getUltrasonicLevel()==us2.getMode().getCap()){
            return Double.POSITIVE_INFINITY;
        }
        return Math.atan((us1.getUltrasonicLevel()-us2.getUltrasonicLevel())/usDistance);
    }

    public void setMode(SickUltrasonic.Mode mode) {
        us1.setMode(mode);
        us2.setMode(mode);
    }

    public double getAverage() {
        if(us1.getUltrasonicLevel()==us1.getMode().getCap()||us2.getUltrasonicLevel()==us2.getMode().getCap()){
            return Double.POSITIVE_INFINITY;
        }
        return (us1.getUltrasonicLevel()+us2.getUltrasonicLevel())/2;
    }
}
