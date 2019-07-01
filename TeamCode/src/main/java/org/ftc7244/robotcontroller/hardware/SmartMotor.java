package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.openftc.revextensions2.ExpansionHubMotor;

public class SmartMotor {
    private ExpansionHubMotor motor;

    private double previousPower;
    private DcMotor.RunMode previousRunMode;
    private DcMotor.ZeroPowerBehavior previousZeroPowerBehaviour;

    private SmartRobot robot;

    public SmartMotor(ExpansionHubMotor motor, SmartRobot robot){
        this.robot = robot;
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
        return robot.getBulkData().getMotorCurrentPosition(motor);
    }

    //TODO support velocity units (IE angular, absolute)
    public double getMotorVelocity(){
        return robot.getBulkData().getMotorVelocity(motor);
    }

    public ExpansionHubMotor getMotor() {
        return motor;
    }
}
