package org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider;

import org.ftc7244.robotcontroller.sensor.Initializable;

public abstract class ExtendedGyroscopeProvider implements Initializable{

    private double[] offsets;
    public ExtendedGyroscopeProvider(){
        offsets = new double[3];
    }

    protected abstract double getReading(Axis axis);

    public double getRotation(Axis axis){
        //return (getReading(axis)-offsets[axis.index])%(Math.PI*2);
        return Math.toRadians(offsetNumber(getReading(axis), offsets[axis.index]));
    }

    public abstract boolean isCalibrated();

    public enum Axis{
        PITCH(0),
        YAW(1),
        ROLL(2);
        private int index;
        Axis(int index){
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    public void offsetAxisTo(Axis axis, double value){
        offsets[axis.index] = getReading(axis)-value;
    }

    protected double offsetNumber(double orientation, double offset) {
        return ((orientation + 540 - offset) % 360) - 180;
    }
}
