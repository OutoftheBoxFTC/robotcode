package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class SmartMotor {
    private DcMotorEx motor;

    private double previousPower;
    private DcMotor.RunMode previousRunMode;
    private DcMotor.ZeroPowerBehavior previousZeroPowerBehaviour;

    public SmartMotor(DcMotorEx motor){
        this.motor = motor;
        if(motor != null){
            previousRunMode = motor.getMode();
            previousZeroPowerBehaviour = motor.getZeroPowerBehavior();
        }
    }

    public void setPower(double power){
        if(power != previousPower){
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
}
