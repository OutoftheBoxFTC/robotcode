package org.ftc7244.robotcontroller.autonamous.drive.orientation;

import org.ftc7244.robotcontroller.autonamous.drive.orientation.Orientation;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.ultrasonic.UltrasonicSystem;

public class RotationalProvider {
    private GyroscopeProvider gyroscope;
    private UltrasonicSystem ultrasonic;
    private DriveProcedure currentDriveProcedure;

    private double gyroscopeOffset;

    private Orientation orientation;

    /**
     * The wall which the active ultrasonic system side will face
     */
    private Wall wall;


    public RotationalProvider(UltrasonicSystem ultrasonic, GyroscopeProvider gyroscope, Orientation orientation){
        this.ultrasonic = ultrasonic;
        this.gyroscope = gyroscope;
        this.orientation = orientation;
        gyroscopeOffset = -orientation.getR();
        wall = null;
    }

    /**
     * It is assumed that this method is called whenever rotation is subject to change (I.E. During Drive/Rotation Procedures)
     * @return the error between the current rotation and the target rotation in the shortest direction of rotation.
     */
    public double getRotationalError(){
        double rotationTarget = currentDriveProcedure.getRotationTarget(),
                rotation = retrieveAbsoluteRotation();
        if(rotationTarget > rotation){
            if(rotationTarget-rotation>Math.PI){
                return rotation-(rotationTarget-Math.PI*2);
            }
            return rotation-rotationTarget;
        }
        if(rotation-rotationTarget>Math.PI){
            return rotationTarget+Math.PI*2-rotation;
        }
        return rotation-rotationTarget;
    }

    public double retrieveAbsoluteRotation(){
        double ultrasonicValue = retrieveUltrasonicValue();
        if(ultrasonicValue != Double.POSITIVE_INFINITY){
            orientGyro(ultrasonicValue);
        }
        double rotation = retrieveGyroscopeReading();
        orientation.setR(rotation);
        return rotation;
    }

    private double retrieveUltrasonicValue(){
        double ultrasonicValue = ultrasonic.getError(UltrasonicSystem.UltrasonicSide.RIGHT);
        UltrasonicSystem.UltrasonicSide side = UltrasonicSystem.UltrasonicSide.RIGHT;
        if(ultrasonicValue == Double.POSITIVE_INFINITY){
            side = UltrasonicSystem.UltrasonicSide.LEFT;
            ultrasonicValue = ultrasonic.getError(UltrasonicSystem.UltrasonicSide.LEFT);
            if(ultrasonicValue == Double.POSITIVE_INFINITY)
                return Double.POSITIVE_INFINITY;
        }

        if(wall != null){
            ultrasonicValue += wall.getRotation()+side.getRotation();
        }
        return ultrasonicValue%(Math.PI*2);
    }

    public void linkDriveProcedure(DriveProcedure procedure){
        this.currentDriveProcedure = procedure;

    }

    private double retrieveGyroscopeReading(){
        return (gyroscope.getRotation(GyroscopeProvider.Axis.YAW)-gyroscopeOffset)%(Math.PI*2);
    }


    public void setWall(Wall wall) {
        this.wall = wall;
    }

    public void orientGyro(double r) {
        r %= Math.PI*2;
        gyroscopeOffset = gyroscope.getRotation(GyroscopeProvider.Axis.YAW)-r;
    }

    public enum Wall{
        BACK(0),
        RED(Math.PI/2),
        AUDIENCE(Math.PI),
        BLUE(3*Math.PI/2);

        private double rotation;
        Wall(double rotation){
            this.rotation = rotation;
        }

        public double getRotation() {
            return rotation;
        }
    }
}
