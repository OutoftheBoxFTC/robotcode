package org.ftc7244.robotcontroller.opmodes.autonamous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.autonamous.control.ConstantControl;
import org.ftc7244.robotcontroller.autonamous.drive.orientation.RotationalProvider;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;

@Autonomous(name = "Blue Crater Auto")
public class BlueCraterAuto extends AutonomousProcedure {
    @Override
    protected void run() {
        System.out.println(0);
        driveController.orient(-7.5, 7.5, 3*Math.PI/4);
        System.out.println(0);
        double x = -24, y = 24;
        int pixyReading = 2;
        robot.intake(-1);
        switch (pixyReading){
            case 0:
                x = -20;
                y = 40;
                break;
            case 2:
                x = -40;
                y = 20;
                break;
        }

        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, x, y, hardwareMap.appContext).setSpeed(0.5).getDriveProcedure());
        robot.intake(0);
        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, -7.5, 7.5,hardwareMap.appContext).
                setDirection(DriveProcedure.Direction.REVERSE).setSpeed(0.5).getDriveProcedure());

        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, -50, 7.5, hardwareMap.appContext).setSpeed(0.5).getDriveProcedure());
        //driveController.getRotation().setUltrasonicUsage(RotationalProvider.UltrasonicUsage.LEFT);
        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, -50, -52, hardwareMap.appContext).
                setDirection(DriveProcedure.Direction.REVERSE).setSpeed(0.5).getDriveProcedure());
        driveController.drive(new DriveProcedure.DriveProcedureBuilder(orientation, -52, 30, hardwareMap.appContext).setSpeed(0.5).getDriveProcedure());
    }
}