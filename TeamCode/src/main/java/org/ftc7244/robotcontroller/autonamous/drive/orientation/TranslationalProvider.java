package org.ftc7244.robotcontroller.autonamous.drive.orientation;


import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;

public class TranslationalProvider {
    private Orientation orientation;
    private DriveProcedure procedure;
    private DistanceProvider distance;
    private double initialX, initialY;

    public TranslationalProvider(Orientation orientation){
        this.orientation = orientation;
    }

    public void orient(double x, double y){

    }

    public void linkDriveProcedure(DriveProcedure procedure){
        this.procedure = procedure;
        initialX = orientation.getX();
        initialY = orientation.getY();
    }
}