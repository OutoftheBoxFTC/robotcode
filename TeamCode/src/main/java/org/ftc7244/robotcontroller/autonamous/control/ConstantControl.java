package org.ftc7244.robotcontroller.autonamous.control;


public class ConstantControl extends ControlSystem {

    @Override
    public double correction(double error) {
        return 0.3*(error<0?1:-1);
    }

    @Override
    public void reset() {

    }
}
