package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.robotcore.hardware.Servo;

public class SmartServo {
    private Servo servo;
    private double previousPosition;
    public SmartServo(Servo servo){
        this.servo = servo;
        previousPosition = servo.getPosition();
    }

    public Servo getServo() {
        return servo;
    }

    public void setPosition(double position){
        if(position != previousPosition){
            servo.setPosition(position);
            previousPosition = position;
        }
    }

    public double getPosition(){
        return previousPosition;
    }
}
