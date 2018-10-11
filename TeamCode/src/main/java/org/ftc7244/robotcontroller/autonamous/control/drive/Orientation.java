package org.ftc7244.robotcontroller.autonamous.control.drive;

public class Orientation {
    private double x, y, r;
    private DriveProcedure procedure;

    public Orientation(double x, double y, double r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public void reCalibrate(double x, double y, double r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void queryDriveProcedure(DriveProcedure procedure){
        this.procedure = procedure;
        reCalibrate(x+procedure.getDistanceTarget()*Math.cos(procedure.getRotationTarget()),
                y+procedure.getDistanceTarget()*Math.sin(procedure.getRotationTarget()),
                procedure.getRotationTarget());

    }
}
