package org.ftc7244.robotcontroller.opmodes.autonamous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.autonamous.control.ConstantControl;
import org.ftc7244.robotcontroller.autonamous.drive.DriveController;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;

@Autonomous(name = "Blue Crater Auto")
public class BlueCraterAuto extends AutonomousProcedure {
    @Override
    protected void run() throws InterruptedException{
        driveController.orient(7.5, 7.5, Math.PI/4);
        double x=24, y=24;
        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, x, y).setSpeed(0.5).
                setControlSystem(new ConstantControl()).getDriveProcedure());
        sleep(1000);
        driveController.setDirection(DriveController.Direction.REVERSE);
        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, 7.5, 7.5).setSpeed(0.5).
                setControlSystem(new ConstantControl()).getDriveProcedure());
        sleep(1000);
    }
}
