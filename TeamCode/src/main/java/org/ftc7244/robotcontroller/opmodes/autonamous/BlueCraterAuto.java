package org.ftc7244.robotcontroller.opmodes.autonamous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.autonamous.control.ConstantControl;
import org.ftc7244.robotcontroller.autonamous.drive.DriveController;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;

@Autonomous(name = "Blue Crater Auto")
public class BlueCraterAuto extends AutonomousProcedure {
    @Override
    protected void run() {
        driveController.orient(-7.5, 7.5, 3*Math.PI/4);
        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, -24, 24).setSpeed(0.5).
                setControlSystem(new ConstantControl()).getDriveProcedure());
        driveController.setDirection(DriveController.Direction.REVERSE);
        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, -7.5, 7.5).setSpeed(0.5).
                setControlSystem(new ConstantControl()).getDriveProcedure());
        driveController.setDirection(DriveController.Direction.FOREWORD);
        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, -48, 7.5).setSpeed(0.5).
                setControlSystem(new ConstantControl()).getDriveProcedure());
        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, -48, -36).setSpeed(0.5).
                setControlSystem(new ConstantControl()).getDriveProcedure());
        driveController.setDirection(DriveController.Direction.REVERSE);
        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, -48, 30).setSpeed(0.5).
                setControlSystem(new ConstantControl()).getDriveProcedure());
    }
}