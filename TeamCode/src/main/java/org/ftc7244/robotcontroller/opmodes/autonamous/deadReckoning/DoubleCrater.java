package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample;

import java.util.concurrent.atomic.AtomicBoolean;

@Autonomous(name = "Double Crater")
public class DoubleCrater extends DeadReckoningBase {
    public DoubleCrater() {
        super(true);
    }

    @Override
    protected void run() {
        double rotation = 0, mineralDistance = 18;
        switch (sample){
            case LEFT:
                rotation = 22;
                break;
            case RIGHT:
                mineralDistance = 16;
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
        switch(sample){
            case LEFT:
                drive(4, -0.5);
                break;
            case RIGHT:
                drive(3, -0.5);
                break;
            case CENTER:
                drive(3, -0.5);
                break;
            default:

        }
        robot.getIntakeLatch().setPosition(0.05);
        robot.moveArm(0);
        robot.getLid().setPosition(.8);
        rotateGyro(90 - rotation, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        robot.intake(0);
        switch (sample) {
            case CENTER:
                drive(31, 0.5);
                break;
            case RIGHT:
                drive(37, 0.5);
                break;
            case LEFT:
                drive(29, 0.5);
                break;
            default:
                drive(34, 0.5);
        }
        rotateGyro(45, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        parralelize(robot.getLeadingRightUS(), robot.getTrailingRightUS(), 13.25, 0.8, 0.0000000025, 19000000, Math.toRadians(-3));

        switch (sample) {
            case LEFT:
                drive(32, 0.5);
                break;
            case RIGHT:
                drive(14, 0.5);
                break;
            default:
                drive(28, 0.5);
                break;
        }
        double craterDistance = 43, distance = 8;
        switch (sample) {
            case LEFT:
                craterDistance = 60;
                distance = 18;
                break;
            case RIGHT:
                distance = -2.9;
                break;
        }
        rotateGyro(93, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        drive(distance+3, 0.5);
        sleep(200);

        if(sample != null && sample.equals(PixycamSample.SampleTransform.LEFT)){
            drive(15, -0.5);
            rotateGyro(45, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
            burnJeClamelRetrograde();
            dumpArm();
            rotateGyro(-50, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        }
        else {
            if(!sample.equals(PixycamSample.SampleTransform.RIGHT)){
                drive(distance+6, -0.5);
            }
            rotateGyro(90, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
            drive(9, -0.5);
            parralelize(robot.getLeadingRightUS(), robot.getTrailingRightUS(), 13.25, 0.8, 0.0000000025, 19000000, Math.toRadians(1.5));
            burnJeClamelRetrograde();
            dumpArm();
        }
        threadManager.submit(getArmReset());
        robot.setDriveZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.FLOAT);
        drive(43, 0.8);
    }
}
