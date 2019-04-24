package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample;

import java.util.concurrent.atomic.AtomicBoolean;

@Autonomous
public class Depot extends DeadReckoningBase {
    public Depot() {
        super (true);
    }

    @Override
    protected void run() {
        double currentRot = gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW), mineralDistance = 18;
        double rotation = 0;
        switch (sample){
            case LEFT:
                rotation = 24;
                break;
            case RIGHT:
                mineralDistance = 18;
                rotation = -24;
                break;
        }
        drive(2, 0.3);
        rotateGyro(rotation-Math.toDegrees(gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)), 1, 0.0000000025, 19000000, (long)1e9);
        robot.moveArm(0.15);
        robot.getIntakeLatch().setPosition(latchOpen);
        robot.intake(1);
        drive(mineralDistance, 0.5);
        sleep(200);
        robot.getIntakeLatch().setPosition(latchClosed);
        final AtomicBoolean armMoved = new AtomicBoolean(false);
        switch(sample){
            case LEFT:
                drive(21, -0.5);
                break;
            case RIGHT:
                drive(21, -0.5);
                break;
            case CENTER:
                drive(21, -0.5);
                break;
            default:

        }
        threadManager.submit(()->{
            robot.moveArm(-1);
            while (robot.getRaisingArm1().getCurrentPosition()<2600 && opModeIsActive());
            robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            armMoved.set(true);
            robot.moveArm(0);
        });

        while (!armMoved.get());
        robot.intake(0);
        robot.getLid().setPosition(.4);
        robot.getIntakeLatch().setPosition(latchOpen);
        sleep(1000);
        robot.getRaisingArm1().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.getRaisingArm2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        threadManager.submit(getArmReset());
        robot.getIntakeLatch().setPosition(latchClosed);
        int distance = 6;
        if(sample != null && !sample.equals(PixycamSample.SampleTransform.CENTER))
            distance += 4;
        drive(distance, 0.5);
        robot.getLid().setPosition(.8);
        rotateGyro(82 - rotation, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        switch (sample) {
            case RIGHT:
                drive(43, 0.5);
                break;
            case LEFT:
                drive(34, 0.5);
                break;
            default:
                drive(40, 0.5);
        }
        rotateGyro(56, 0.8, 0.0000000025, 19000000, (long) 1.5e9);
        parralelize(robot.getLeadingRightUS(), robot.getTrailingRightUS(), 13.25, 0.8, 0.0000000025, 19000000, Math.toRadians(3));
        switch (sample) {
            case LEFT:
                drive(43, -0.5);
                break;
            case RIGHT:
                drive(41, -0.5);
                break;
            case CENTER:
                drive(47, -0.5);
                break;
        }
        burnJeClamelRetrograde();
        sleep(1000);
        parralelize(robot.getLeadingRightUS(), robot.getTrailingRightUS(), 13.25, 0.8, 0.0000000025, 19000000, Math.toRadians(-1.5));
        threadManager.submit(getArmReset());
        robot.getJeClamelBurner().setPosition(0.6);
        robot.setDriveZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.getLid().setPosition(.4);
        switch (sample){
            case LEFT:
                drive(35, 0.8);
                break;
            case RIGHT:
                drive(38, 0.8);
                break;
            case CENTER:
                drive(56, 0.8);
                break;
        }
    }
}
