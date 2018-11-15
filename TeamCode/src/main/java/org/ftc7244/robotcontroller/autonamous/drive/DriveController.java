package org.ftc7244.robotcontroller.autonamous.drive;

import org.ftc7244.robotcontroller.autonamous.drive.orientation.DistanceProvider;
import org.ftc7244.robotcontroller.autonamous.drive.orientation.RotationalProvider;
import org.ftc7244.robotcontroller.autonamous.drive.orientation.Orientation;
import org.ftc7244.robotcontroller.autonamous.drive.orientation.TranslationalProvider;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.ultrasonic.UltrasonicSystem;

public class DriveController {
    private Orientation orientation;
    private RotationalProvider rotation;
    private DistanceProvider distance;
    private TranslationalProvider translation;
    private Direction direction;

    private Robot robot;

    public DriveController(Orientation orientation, UltrasonicSystem ultrasonic, GyroscopeProvider gyroscope, Robot robot){
        this.orientation  = orientation;
        this.robot = robot;
        rotation = new RotationalProvider(ultrasonic, gyroscope, orientation);
        distance = new DistanceProvider(robot);
        translation = new TranslationalProvider(orientation, distance);
        direction = Direction.FOREWARD;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    //TODO replace with a driveController offset system based off of given curve
    public void drive(DriveProcedure procedure){
        rotation.linkDriveProcedure(procedure);
        translation.linkDriveProcedure(procedure);
        double rotationError = rotation.getRotationalError();

        while ((robot.getOpMode().opModeIsActive() && !procedure.getRotationalTerminator().shouldTerminate(rotationError))){
            rotationError = rotation.getRotationalError();
            double rotation = procedure.getControlSystem().correction(rotationError);
            robot.drive(rotation, -rotation);
        }
        distance.orient(0);
        double distanceTarget = procedure.getDistanceTarget()*direction.multiplier,
                translationalError = (distanceTarget-distance.getEncoderAverage());

        while (robot.getOpMode().opModeIsActive() && !procedure.getTranslationalTerminator().shouldTerminate(translationalError*direction.multiplier)){
            rotationError = rotation.getRotationalError();
            double rotation = procedure.getControlSystem().correction(rotationError),
                    power = procedure.getSpeed();
            robot.getOpMode().telemetry.addData("power", power* direction.multiplier);
            robot.getOpMode().telemetry.addData("error", direction.multiplier*translationalError);
            robot.getOpMode().telemetry.update();
            robot.drive(rotation+power*direction.multiplier, -rotation+power*direction.multiplier);
            translationalError = (distanceTarget-distance.getEncoderAverage());
        }
        robot.drive(0, 0);
        //TODO perform this dynamically based on image recognition or ultrasonic sensor input
        translation.applyDriveProcedure();
    }

    public void setDirection(Direction direction){
        this.direction = direction;
    }

    public void orient(double x, double y, double r) {
        orientation.setX(x);
        orientation.setY(y);
        orientation.setR(r);
        rotation.orientGyro(r);
    }

    public RotationalProvider getRotation() {
        return rotation;
    }

    public enum Direction{
        FOREWARD(1, 0),
        REVERSE(-1, Math.PI);
        private int multiplier;
        private double angle;
        Direction(int multiplier, double angle) {
            this.multiplier = multiplier;
            this.angle = angle;
        }

        public double getAngleOffset() {
            return angle;
        }
    }
}
