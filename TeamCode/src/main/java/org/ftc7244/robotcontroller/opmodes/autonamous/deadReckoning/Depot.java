package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider;

import java.util.concurrent.atomic.AtomicBoolean;

@Autonomous
public class Depot extends DeadReckoningBase {
    public Depot() {
        super (true);
    }

    @Override
    protected void run() {
        double currentRot = gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW);
        double rotation = 0;
        switch (sample){
            case LEFT:
                rotation = 22;
                break;
            case RIGHT:
                rotation = -22;
                break;
        }
        drive(4, .1);
        rotateGyro(rotation-Math.toDegrees(gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)), 1, 0.0000000025, 19000000, (long)1e9);
        robot.moveArm(0.15);
        robot.getIntakeLatch().setPosition(0.8);
        robot.intake(1);
        drive(24, 0.5);
        sleep(200);
        robot.intake(0);
        robot.getIntakeLatch().setPosition(0.2);
        final AtomicBoolean armMoved = new AtomicBoolean(false);
        drive(23, -0.5);
        threadManager.submit(()->{
            robot.moveArm(-1);
            while (robot.getRaisingArm1().getCurrentPosition()<2050 && opModeIsActive());
            armMoved.set(true);
            robot.moveArm(0);
        });

        while (!armMoved.get());
        robot.getLid().setPosition(.4);
        sleep(1000);
        threadManager.submit(getArmReset());
        drive(14, 0.5);
        robot.getLid().setPosition(.8);
        rotateGyro(82 - rotation, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        switch (sample) {
            case CENTER:
                drive(38, 0.5);
                break;
            case RIGHT:
                drive(42, 0.5);
                break;
            case LEFT:
                drive(36.5, 0.5);
                break;
            default:
                drive( 40, 0.5);
        }
        rotateGyro(56, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        //parralelize(robot.getLeadingRightUS(), robot.getTrailingRightUS(), 13.25, 0.8, 0.0000000025, 19000000);
        switch (sample) {
            case LEFT:
                drive(51, -0.5);
                break;
            case RIGHT:
                drive(48, -0.5);
                break;
            case CENTER:
                drive(53, -0.5);
                break;
        }
        parralelize(robot.getLeadingRightUS(), robot.getTrailingRightUS(), 13.25, 0.8, 0.0000000025, 19000000);
        burnJeClamelRetrograde();
        sleep(1500);
        threadManager.submit(getArmReset());
        switch (sample){
            case LEFT:
                drive(59, 0.8);
                break;
            case RIGHT:
                drive(62, 0.8);
                break;
            case CENTER:
                drive(62, 0.8);
                break;
        }
    }
}
