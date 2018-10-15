package org.ftc7244.robotcontroller.autonamous.drive;

/**
 * This class will refer to the previous {@link Orientation.PrecisionBenchmark} value of the orientation and define a relative translation and rotation procedure
 * Acts in the order of rotation then driving
 */
public class DriveProcedure {
    private double d;
    private double r;

    public DriveProcedure(double x, double y, Orientation orientation){
        double a = x-orientation.getX(Orientation.PrecisionBenchmark.KNOWN),
                o = y-orientation.getY(Orientation.PrecisionBenchmark.KNOWN),
                t = o/a;
        r = Math.atan(t);
        r=a<0?r>0?Math.PI-r:-Math.PI-r:r;
        d=Math.sqrt(a*a+o*o);

    }

    public double getRotationTarget(){
        return r;
    }

    public double getDistanceTarget(){
        return d;
    }
}
