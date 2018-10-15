package org.ftc7244.robotcontroller.autonamous.drive;

public class Orientation {
    private double x, y, r, extrudedX, extrudedY, extrudedR;
    private DriveProcedure procedure;

    public Orientation(double x, double y, double r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public double getX(PrecisionBenchmark type) {
        return type==PrecisionBenchmark.KNOWN?x:extrudedX;
    }

    public double getY(PrecisionBenchmark type) {
        return type==PrecisionBenchmark.KNOWN?y:extrudedY;
    }

    public double getR(PrecisionBenchmark type) {
        return type==PrecisionBenchmark.KNOWN?r:extrudedR;
    }

    public void reCalibrate(double x, double y, double r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void queryDriveProcedure(DriveProcedure procedure){
        reCalibrate(x +this.procedure.getDistanceTarget()*Math.cos(this.procedure.getRotationTarget()),
                y +this.procedure.getDistanceTarget()*Math.sin(this.procedure.getRotationTarget()),
                this.procedure.getRotationTarget());
        this.procedure = procedure;

    }

    public enum PrecisionBenchmark {
        KNOWN,
        EXTRUDED
    }
}