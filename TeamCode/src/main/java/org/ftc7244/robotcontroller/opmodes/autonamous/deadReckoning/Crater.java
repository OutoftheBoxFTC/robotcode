package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample;

import java.util.concurrent.atomic.AtomicBoolean;

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
                rotation = 22;
                break;
            case RIGHT:
                rotation = -22;
                break;
        }
        drive(2, 0.3);
        rotateGyro(rotation-Math.toDegrees(gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)), 1, 0.0000000025, 19000000, (long)1e9);
        robot.moveArm(0.15);
        sleep(500);
        robot.getIntakeLatch().setPosition(0.8);
        final AtomicBoolean armMoved = new AtomicBoolean(false);
        threadManager.submit(getIntakeSample());
        //robot.intake(1);
        drive(18, 0.5);
        sleep(200);
        drive(3, -0.5);
        robot.getIntakeLatch().setPosition(0.05);
        robot.moveArm(0);
        robot.getLid().setPosition(.8);
        rotateGyro(-90 - rotation, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        robot.intake(0);
        switch (sample) {
            case CENTER:
                drive(43, -0.5);
                break;
            case RIGHT:
                drive(49, -0.5);
                break;
            case LEFT:
                drive(40, -0.5);
                break;
            default:
                drive(43, -0.5);
        }
        rotateGyro(45, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        parralelize(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000);
        switch (sample) {
            case LEFT:
                drive(26, -0.5);
                break;
            case RIGHT:
                drive(30, -0.5);
                break;
            default:
                drive(26, -0.5);
                break;
        }
        parralelize(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000);
        burnJeClamelRetrograde();
        dumpArm();
        threadManager.submit(getArmReset());
        switch (sample){
            case LEFT:
                drive(57, 0.8);
                break;
            case RIGHT:
                drive(56, 0.8);
                break;
            case CENTER:
                drive(54, 0.8);
                break;
        }
    }
}
