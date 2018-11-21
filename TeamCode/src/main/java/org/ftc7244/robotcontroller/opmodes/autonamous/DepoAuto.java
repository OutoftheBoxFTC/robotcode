package org.ftc7244.robotcontroller.opmodes.autonamous;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;

public class DepoAuto extends AutonomousProcedure {
    @Override
    protected void run() throws InterruptedException{
        int mineral = 0;
        robot.getLeftDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getRightDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        switch(mineral){
            case 1:
                //robot.rotate(gyroscope, -23);
                break;
            case 2:
                //robot.rotate(gyroscope, 35);
                break;
            default:
                break;
        }
        robot.intake(-1);
        robot.driveToInch(1, 20.5);
        sleep(500);
        robot.intake(0);
        robot.driveToInch(-1, -20.5);
        sleep(500);
        //robot.rotate(gyroscope, -40);
        sleep(500);
        robot.driveToInch(1, 55);
        sleep(1000);
        robot.getLeftDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.getRightDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
}
