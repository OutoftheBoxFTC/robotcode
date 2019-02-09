package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample;

@Autonomous(name = "Double Mineral Crater")
public class DoubleCrater extends DeadReckoningBase {
    public DoubleCrater() {
        super(true);
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
        drive(24, 0.5);
        sleep(200);
        drive(5, -0.5);
        robot.moveArm(0);
        rotateGyro(90 - rotation, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        robot.intake(0);
        robot.getIntakeLatch().setPosition(0.2);
        switch (sample) {
            case CENTER:
                drive(40, 0.5);
                break;
            case RIGHT:
                drive(53, 0.5);
                break;
            case LEFT:
                drive(36.5, 0.5);
                break;
            default:
                drive(55, 0.5);
        }
        rotateGyro(45, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        parralelize(robot.getLeadingRightUS(), robot.getTrailingRightUS(), 13.25, 0.8, 0.0000000025, 19000000, (long)2e9);

        double alignment = 27.75, distance = 10;
        switch (sample) {
            case LEFT:
                alignment = 36;
                distance = 21;
                break;
            case RIGHT:
                alignment = 6;
                distance = 2;
                break;
        }

        drive(alignment, 0.5);
        rotateGyro(90, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        robot.moveArm(0.15);
        robot.getIntakeLatch().setPosition(0.8);
        robot.intake(1);
        drive(distance+3, 0.5);
        sleep(200);
        robot.getIntakeLatch().setPosition(0.2);
        robot.moveArm(0);
        if(sample != null && sample.equals(PixycamSample.SampleTransform.LEFT)){
            rotateGyro(20, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
            drive(6, -0.5);
            robot.intake(0);
            dumpArm();
            rotateGyro(-25, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
            drive(59, 0.8);
        }
        else {
            drive(distance+6, -0.5);
            rotateGyro(97, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
            robot.intake(0);
            if (sample.equals(PixycamSample.SampleTransform.RIGHT)) {
                drive(26, -0.5);
            }else{
                drive(6, -0.5);
            }
            parralelize(robot.getLeadingRightUS(), robot.getTrailingRightUS(), 13.25, 0.8, 0.0000000025, 19000000);
            dumpArm();
            drive(55, 0.8);
        }
        threadManager.submit(getArmReset());
    }
}
