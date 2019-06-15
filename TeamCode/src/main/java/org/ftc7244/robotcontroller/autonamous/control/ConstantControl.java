package org.ftc7244.robotcontroller.autonamous.control;


public class ConstantControl extends ControlSystem {
    private double constant;
    public ConstantControl(double constant){
        this.constant = constant;
    }
    @Override
    public double correction(double error) {
        return constant*(error<0?1:-1);
    }

    @Override
    public void reset() {

    }
}