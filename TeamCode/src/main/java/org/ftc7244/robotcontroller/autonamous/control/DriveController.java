package org.ftc7244.robotcontroller.autonamous.control;

import org.ftc7244.robotcontroller.autonamous.drive.DriveProcedure;
import org.ftc7244.robotcontroller.autonamous.drive.terminator.DriveProcedureTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.Orientation;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.ultrasonic.UltrasonicSystem;

public class DriveController {
    private Orientation orientation;
    private RotationProvider rotation;
    private Robot robot;

    public DriveController(Orientation orientation, UltrasonicSystem ultrasonic, GyroscopeProvider gyroscope, Robot robot){
        this.orientation  = orientation;
        this.robot = robot;
        rotation = new RotationProvider(ultrasonic, gyroscope, orientation);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    //TODO replace with a drive offset system based off of given curve
    public void drive(DriveProcedure procedure){
        rotation.linkDriveProcedure(procedure);
        double rotationError = rotation.getRotationalError();
        while (robot.getOpMode().opModeIsActive() && !procedure.getRotationalTerminator().shouldTerminate(rotationError)){
            rotationError = rotation.getRotationalError();
            double rotation = procedure.getControlSystem().correction(rotationError);
            robot.drive(rotation, -rotation);
        }
        robot.resetDriveEncoders();
        double translationalError = procedure.getDistanceTarget()-robot.getDriveEncoderAverage();
        while (robot.getOpMode().opModeIsActive() && !procedure.getTranslationalTerminator().shouldTerminate(translationalError)){
            rotationError = rotation.getRotationalError();
            double rotation = procedure.getControlSystem().correction(rotationError),
                    power = procedure.getSpeed();
            robot.drive(rotation+power, -rotation+power);
        }
    }
}
