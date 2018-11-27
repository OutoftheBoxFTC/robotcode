package org.ftc7244.robotcontroller.opmodes.autonamous.dead_reckoning;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

@Autonomous(name = "Crater Autonomous")
public class Crater extends DeadReckoningBase {

    public Crater() {
        super (true);
    }

    @Override
    protected void run() {
        int pixyCase = 1;
        double rotation = 0;
        switch (pixyCase){
            case 0:
                rotation = -23;
                break;
            case 2:
                rotation = 23;
                break;
        }
        robot.getRightDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getRightDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rotateGyro(rotation-Math.toDegrees(gyro.getRotation(GyroscopeProvider.Axis.YAW)));
        robot.intake(-1);
        drive(22, 0.5);
        sleep(200);
        robot.intake(0);
        drive(20, -0.5);
        rotateGyro(45-rotation);
        drive(40, 0.5);
        rotateGyro(-90);
        parralelize(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000);
        drive(20, -0.5);
        rotateGyro(-15);
        drive(5, -0.5);

        //Arm Dumping
        dumpArm();


        rotateGyro(5);
        //parralelize(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000);
        drive(20, 0.5);
        rotateGyro(7);
        drive(30, 0.5);
    }

    private void dumpArm(){
        robot.moveArm(-1);
        sleep(1000);
        robot.getLid().setPosition(0.8);
        sleep(500);
        robot.moveArm(0);
        sleep(500);
        robot.moveArm(1);
        robot.getLid().setPosition(0);
        sleep(1000);
        robot.moveArm(0);
    }
}
