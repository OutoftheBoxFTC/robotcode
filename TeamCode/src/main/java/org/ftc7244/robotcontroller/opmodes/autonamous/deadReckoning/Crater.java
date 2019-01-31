package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider;

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
        drive(4, 0.1);
        rotateGyro(rotation-Math.toDegrees(gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)), 1, 0.0000000025, 19000000, (long)1e9);
        robot.moveArm(0.15);
        robot.getIntakeLatch().setPosition(0.8);
        robot.intake(1);
        driveRange(24, 0.5);
        sleep(200);
        driveRange(5, -0.5);
        robot.intake(0);
        robot.getIntakeLatch().setPosition(0.2);
        robot.moveArm(0);
        rotateGyro(-90 - rotation, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        switch (sample) {
            case CENTER:
                driveRange(55, -0.5);
                break;
            case RIGHT:
                driveRange(61, -0.5);
                break;
            case LEFT:
                driveRange(52, -0.5);
                break;
            default:
                driveRange(55, -0.5);
        }
        rotateGyro(45, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        parralelize(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000);
        driveRange(30, -0.5);
        parralelize(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000);
        dumpArm();
        switch (sample){
            case LEFT:
                driveRange(55, 0.8);
                break;
            case RIGHT:
                driveRange(50, 0.8);
                break;
            case CENTER:
                driveRange(50, 0.8);
                break;
        }
    }

    private void dumpArm(){
        robot.moveArm(-1);
        sleep(1000);
        robot.getLid().setPosition(0.78);
        robot.moveArm(0);
        sleep(500);
        robot.moveArm(1);
        robot.getLid().setPosition(.4);
        sleep(750);
        robot.moveArm(0);
    }
}
