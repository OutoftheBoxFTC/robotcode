package org.ftc7244.robotcontroller.hardware;

import org.openftc.revextensions2.ExpansionHubServo;

public class SmartServo {
    private ExpansionHubServo servo;
    private double previousPosition;
    public SmartServo(ExpansionHubServo servo){
        this.servo = servo;
        previousPosition = servo.getPosition();
    }

    public ExpansionHubServo getServo() {
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
