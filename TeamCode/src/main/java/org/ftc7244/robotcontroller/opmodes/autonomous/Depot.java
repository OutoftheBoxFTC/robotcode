package org.ftc7244.robotcontroller.opmodes.autonomous;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample;

public class Depot extends AutonomousProcedure {
    PixycamSample pixy = new PixycamSample();
    PixycamSample.Sample sample = null;
    @Override
    protected void run() {
        driveController.orient(8, 8, Math.PI / 4);
        switch (sample) {
            case LEFT:
                driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation,13, 27 ).setSpeed(0.5).getDriveProcedure());
                break;
            case RIGHT:
                driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, 36, 36).setSpeed(0.5).getDriveProcedure());
                break;
            default:
                driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, 24, 48).setSpeed(0.5).getDriveProcedure());
                break;
        }
    }
}
