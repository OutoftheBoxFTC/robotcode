package org.ftc7244.robotcontroller.autonamous.control;

import org.ftc7244.robotcontroller.autonamous.drive.DriveProcedure;
import org.ftc7244.robotcontroller.autonamous.drive.Orientation;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.ultrasonic.UltrasonicSystem;

public class RotationProvider {
    private GyroscopeProvider gyroscope;
    private UltrasonicSystem ultrasonic;
    private DriveProcedure currentDriveProcedure;

    private double gyroscopeOffset;

    private Orientation orientation;

    /**
     * The wall which the active ultrasonic system side will face
     */
    private Wall wall;


    public RotationProvider(UltrasonicSystem ultrasonic, GyroscopeProvider gyroscope, Orientation orientation){
        this.ultrasonic = ultrasonic;
        this.gyroscope = gyroscope;
        this.orientation = orientation;
        gyroscopeOffset = -orientation.getR();
        wall = null;
    }

    /**
     * It is assumed that this method is called whenever rotation is subject to change (I.E. During Drive/Rotation Procedures)
     * @return the error between the current rotation and the target rotation in the desired direction of travel.
     */
    public double getRotationalError(){
        double rotationTarget = currentDriveProcedure.getRotationTarget(),
                rotation = retrieveAbsoluteRotation(),
                error = Math.abs(rotationTarget-rotation);
        return error > Math.PI ? Math.PI*2-error:error;
    }

    public double retrieveAbsoluteRotation(){
        double ultrasonicValue = retrieveUltrasonicValue();
        if(ultrasonicValue != Double.POSITIVE_INFINITY){
            gyroscopeOffset = gyroscope.getRotation(GyroscopeProvider.Axis.YAW)-ultrasonicValue;
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
