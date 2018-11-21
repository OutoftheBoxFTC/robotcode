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

    private Robot robot;

    public DriveController(Orientation orientation, UltrasonicSystem ultrasonic, GyroscopeProvider gyroscope, Robot robot){
        this.orientation  = orientation;
        this.robot = robot;
        rotation = new RotationalProvider(ultrasonic, gyroscope, orientation);
        distance = new DistanceProvider(robot);
        translation = new TranslationalProvider(orientation, distance);
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

            robot.getOpMode().telemetry.addData("Error", Math.toDegrees(rotationError));
            robot.getOpMode().telemetry.addData("Target", Math.toDegrees(procedure.getRotationTarget()));
            robot.getOpMode().telemetry.addData("Current", Math.toDegrees(orientation.getR()));

            double rotation = procedure.getControlSystem().correction(rotationError);
            robot.getOpMode().telemetry.addData("Correction", rotation);
            robot.getOpMode().telemetry.update();
            robot.drive(rotation, -rotation);
        }
        robot.drive(0, 0);
        distance.orient(0);
        double distanceTarget = procedure.getDistanceTarget(),
                translationalError = (distanceTarget-distance.getEncoderAverage());

        while (robot.getOpMode().opModeIsActive() && !procedure.getTranslationalTerminator().shouldTerminate(translationalError)){
            rotationError = rotation.getRotationalError();
            double rotation = procedure.getControlSystem().correction(rotationError),
                    power = procedure.getSpeed();

            robot.getOpMode().telemetry.addData("power", power);
            robot.getOpMode().telemetry.addData("error", translationalError);
            robot.getOpMode().telemetry.update();

            robot.drive(rotation+power, -rotation+power);
            translationalError = (distanceTarget-distance.getEncoderAverage());
        }
        robot.drive(0, 0);
        //TODO perform this dynamically based on image recognition or ultrasonic sensor input
        translation.applyDriveProcedure();
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
}
