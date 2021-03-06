package org.ftc7244.robotcontroller.sensor.gyroscope;

public abstract class GyroscopeProvider {

    protected abstract double getReading(Axis axis);

    public double getRotation(Axis axis){
        return getReading(axis)%(Math.PI*2);
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
}
