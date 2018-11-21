package org.ftc7244.robotcontroller.opmodes.autonamous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.autonamous.control.ConstantControl;
import org.ftc7244.robotcontroller.autonamous.control.ControlSystem;
import org.ftc7244.robotcontroller.autonamous.control.PIDControl;
import org.ftc7244.robotcontroller.autonamous.drive.orientation.Orientation;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.DriveTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.RangeTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.SensitivityTerminator;

@Autonomous(name = "Crater Auto")
public class CraterAuto extends AutonomousProcedure {
    @Override
    protected void run() throws InterruptedException{
        int mineral = 0;
        robot.getLeftDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getRightDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        switch(mineral){
            case 1:
                rotate(23, 0.75);
                break;
            case 2:
                rotate(-35, 0.75);
                break;
            default:
                break;
        }
        robot.intake(-1);
        robot.driveToInch(0.3, 20.5);
        sleep(500);
        robot.intake(0);
        robot.driveToInch(-0.3, -10.5);
        sleep(500);
        rotate(45, 0.5);
        sleep(500);
        robot.driveToInch(0.3, 40);
        sleep(500);
        rotate(-45, 0.75);
        sleep(500);
        robot.driveToInch(-0.75, -60);
        //drop off team marker
        robot.driveToInch(0.75, 84);
        robot.getLeftDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.getRightDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    private void rotate(double target, double speed){
        driveController.orient(0, 0, 0);
        DriveProcedure procedure = new DriveProcedure(Math.toRadians(target), 0, speed,
                new SensitivityTerminator(Math.PI/90, 100),
                new RangeTerminator(0, Double.NEGATIVE_INFINITY),
                new ConstantControl(), orientation);
        driveController.drive(procedure);
    }
}