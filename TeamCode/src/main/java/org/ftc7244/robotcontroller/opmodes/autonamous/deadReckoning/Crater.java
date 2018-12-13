package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample;

@Autonomous(name = "Crater")
public class Crater extends DeadReckoningBase {

    public Crater() {
        super (true);
    }

    @Override
    protected void run() {
        double rotation = 0;
        switch (sample){
            case LEFT:
                rotation = 23;
                break;
            case RIGHT:
                rotation = -25;
                break;
        }
        rotateGyro(rotation-Math.toDegrees(gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)), 1, 0.0000000025, 19000000, (long)1e9);
        robot.moveArm(0.15);
        robot.intake(1);
        drive(24, 0.5);
        sleep(200);
        drive(5, -0.5);
        robot.intake(0);
        robot.moveArm(0);
        rotateGyro(-90 - rotation, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        switch (sample) {
            case CENTER:
                drive(55, -0.5);
                break;
            case RIGHT:
                drive(61, -0.5);
                break;
            case LEFT:
                drive(52, -0.5);
                break;
            default:
                drive(55, -0.5);
        }
        rotateGyro(45, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        parralelize(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000);
        drive(30, -0.5);
        parralelize(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000);
        dumpArm();
        switch (sample){
            case LEFT:
                drive(55, 0.8);
                break;
            case RIGHT:
                drive(50, 0.8);
                break;
            case CENTER:
                drive(50, 0.8);
                break;
        }
    }

    private void dumpArm(){
        robot.moveArm(-1);
        sleep(1000);
        robot.getLid().setPosition(.1);
        robot.moveArm(0);
        sleep(500);
        robot.moveArm(1);
        robot.getLid().setPosition(.8);
        sleep(750);
        robot.moveArm(0);
    }
}
