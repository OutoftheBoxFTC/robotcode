package org.ftc7244.robotcontroller.autonamous.control;

public abstract class ControlSystem {

    /**
     *
     * @param error the error value for which the control system is correcting
     * @return the correction power offset
     */
    public abstract double correction(double error);

}