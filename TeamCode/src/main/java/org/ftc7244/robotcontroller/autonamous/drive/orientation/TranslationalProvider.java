package org.ftc7244.robotcontroller.autonamous.drive.orientation;


import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;

public class TranslationalProvider {
    private Orientation orientation;
    private DriveProcedure procedure;
    private DistanceProvider distance;
    private double initialX, initialY;

    public TranslationalProvider(Orientation orientation, DistanceProvider distance){
        this.orientation = orientation;
        this.distance = distance;
    }

    public void linkDriveProcedure(DriveProcedure procedure){
        this.procedure = procedure;
        initialX = orientation.getX();
        initialY = orientation.getY();
    }

    public void applyDriveProcedure(){
        if(this.procedure!=null) {
            orientation.setX(initialX + Math.cos(this.procedure.getRotationTarget()) * distance.getEncoderAverage());
            orientation.setY(initialY + Math.sin(this.procedure.getRotationTarget()) * distance.getEncoderAverage());
        }
    }
}