package org.ftc7244.robotcontroller.sensor.ultrasonic;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class UltrasonicError {
    UltrasonicSide side;
    SickUltrasonic ultrasonicFront;
    SickUltrasonic ultrasonicBack;
    public UltrasonicError(UltrasonicSide side){
        this.side = side;
    }
    public void start(HardwareMap map){
        if(side == UltrasonicSide.LEFT){

        }
    }
    public double getError(){
        return 0;
    }
}
