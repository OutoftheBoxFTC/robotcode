package org.ftc7244.robotcontroller.sensor.ultrasonic;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class UltrasonicError {
    SickUltrasonic leftFront, leftBack, rightFront, rightBack;
    UltrasonicSide side;
    public UltrasonicError(SickUltrasonic frontLeftSensor, SickUltrasonic backLeftSensor, SickUltrasonic frontRightSensor, SickUltrasonic backRightSensor){
        this.side  = side;
        leftFront = frontLeftSensor;
        leftBack = backLeftSensor;
        rightFront = frontRightSensor;
        rightBack = backRightSensor;

    }
    public void start(SickUltrasonic.Mode mode){
        leftFront.setMode(mode);
        leftBack.setMode(mode);
        rightBack.setMode(mode);
        rightFront.setMode(mode);
    }
    public double getError(UltrasonicSide side){
        if(side == UltrasonicSide.LEFT){
            return leftFront.getUltrasonicLevel() - leftBack.getUltrasonicLevel();
        }else if(side == UltrasonicSide.RIGHT){
            return rightFront.getUltrasonicLevel() - rightBack.getUltrasonicLevel();
        }else{
            return -1;
        }
    }
    public enum UltrasonicSide{
        LEFT,
        RIGHT
    }
}
