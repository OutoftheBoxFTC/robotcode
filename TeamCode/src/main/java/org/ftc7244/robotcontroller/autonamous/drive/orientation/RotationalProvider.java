package org.ftc7244.robotcontroller.autonamous.drive.orientation;

import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

public class RotationalProvider {
    private GyroscopeProvider gyroscope;
    private DriveProcedure currentDriveProcedure;

    private double gyroscopeOffset;

    private Orientation orientation;

    private boolean usingUltrasonic;

    /**
     * The wall which the active ultrasonic system side will face
     */

    public RotationalProvider(GyroscopeProvider gyroscope, Orientation orientation){
        this.gyroscope = gyroscope;
        this.orientation = orientation;
        gyroscopeOffset = -orientation.getR();
        usingUltrasonic = false;
    }

    /**
     * It is assumed that this method is called whenever rotation is subject to change (I.E. During Drive/Rotation Procedures)
     * @return the error between the current rotation and the target rotation in the shortest direction of rotation.
     */
    public double getRotationalError(){
        double rotationTarget = (currentDriveProcedure.getRotationTarget())%(Math.PI*2),
                currentRotation = retrieveAbsoluteRotation();

        if(rotationTarget > currentRotation){
            if(rotationTarget-currentRotation>Math.PI){
                return currentRotation-(rotationTarget-Math.PI*2);
            }
            return currentRotation-rotationTarget;
        }
        if(currentRotation-rotationTarget>Math.PI){
            return rotationTarget+Math.PI*2-currentRotation;
        }
        return currentRotation-rotationTarget;
    }

    public double retrieveAbsoluteRotation(){
        double rotation = retrieveGyroscopeReading();
        orientation.setR(rotation);
        return rotation;
    }

    public void linkDriveProcedure(DriveProcedure procedure){
        this.currentDriveProcedure = procedure;

    }

    private double retrieveGyroscopeReading(){
        return (gyroscope.getRotation(GyroscopeProvider.Axis.YAW)-gyroscopeOffset)%(Math.PI*2);
    }

    public void orientGyro(double r) {
        r %= (Math.PI*2);
        gyroscopeOffset = gyroscope.getRotation(GyroscopeProvider.Axis.YAW)-r;
    }

    public boolean isUsingUltrasonic() {
        return usingUltrasonic;
    }

    public void setUsingUltrasonic(boolean usingUltrasonic) {
        this.usingUltrasonic = usingUltrasonic;
    }
}
