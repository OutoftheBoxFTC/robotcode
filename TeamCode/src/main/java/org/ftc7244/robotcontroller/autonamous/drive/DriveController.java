package org.ftc7244.robotcontroller.autonamous.drive;

import org.ftc7244.robotcontroller.autonamous.drive.orientation.DistanceProvider;
import org.ftc7244.robotcontroller.autonamous.drive.orientation.RotationalProvider;
import org.ftc7244.robotcontroller.autonamous.drive.orientation.Orientation;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.ultrasonic.UltrasonicSystem;

public class DriveController {
    private Orientation orientation;
    private RotationalProvider rotation;
    private DistanceProvider distance;
    private Robot robot;

    public DriveController(Orientation orientation, UltrasonicSystem ultrasonic, GyroscopeProvider gyroscope, Robot robot){
        this.orientation  = orientation;
        this.robot = robot;
        rotation = new RotationalProvider(ultrasonic, gyroscope, orientation);
        distance = new DistanceProvider(robot);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    //TODO replace with a driveController offset system based off of given curve
    public void drive(DriveProcedure procedure){
        rotation.linkDriveProcedure(procedure);
        double rotationError = rotation.getRotationalError();
        distance.orient();
        while (robot.getOpMode().opModeIsActive() && !procedure.getRotationalTerminator().shouldTerminate(rotationError)){
            rotationError = rotation.getRotationalError();
            double rotation = procedure.getControlSystem().correction(rotationError);
            robot.drive(rotation, -rotation);
        }
        distance.orient();
        double translationalError = procedure.getDistanceTarget()-distance.getEncoderAverage();
        while (robot.getOpMode().opModeIsActive() && !procedure.getTranslationalTerminator().shouldTerminate(translationalError)){
            rotationError = rotation.getRotationalError();
            double rotation = procedure.getControlSystem().correction(rotationError),
                    power = procedure.getSpeed();
            robot.drive(rotation+power, -rotation+power);
        }
    }

    public void orient(double x, double y, double r) {
        orientation.setX(x);
        orientation.setY(y);
        orientation.setR(r);
        rotation.orientGyro(r);
    }
}
