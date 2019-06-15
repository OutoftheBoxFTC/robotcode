package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class SmartMotor {
    private DcMotorEx motor;

    private double previousPower;
    private DcMotor.RunMode previousRunMode;
    private DcMotor.ZeroPowerBehavior previousZeroPowerBehaviour;
    private boolean enabled;

    public SmartMotor(DcMotorEx motor){
        this.motor = motor;
        enabled = true;
        if(motor != null){
            previousRunMode = motor.getMode();
            previousZeroPowerBehaviour = motor.getZeroPowerBehavior();
        }
    }

    public void setPower(double power){
        if(power != previousPower&&enabled){
            previousPower = power;
            motor.setPower(power);
        }
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behaviour){
        if(!previousZeroPowerBehaviour.equals(behaviour)){
            previousZeroPowerBehaviour = behaviour;
            motor.setZeroPowerBehavior(behaviour);
        }
    }

    public void setMode(DcMotor.RunMode mode){
        if(!mode.equals(previousRunMode)){
            previousRunMode = mode;
            motor.setMode(mode);
        }
    }

    public int getCurrentPosition(){
        return motor.getCurrentPosition();
    }

    public DcMotorEx getMotor() {
        return motor;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void disable(){
        setPower(0);
        enabled = false;
    }

    public void enable(){
        enabled = true;
    }
}
