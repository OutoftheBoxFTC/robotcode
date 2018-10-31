package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class Robot extends Hardware {
    private DistanceSensor distanceSensor;
    private WebcamName w1, w2;
    private DcMotor leftDrive, rightDrive;
    public Robot(OpMode opMode) {
        super(opMode, 0);
        //TODO determine counts per inch
    }

    @Override
    public void init() {
        HardwareMap map = opMode.hardwareMap;

        distanceSensor = getOrNull(map, DistanceSensor.class, "distanceSensor");
        w1 = getOrNull(map, WebcamName.class, "w1");
        w2 = getOrNull(map, WebcamName.class, "w2");
        leftDrive = getOrNull(map, DcMotor.class, "leftDrive");
        rightDrive  = getOrNull(map, DcMotor.class, "rightDrive");
        rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void initServos() {

    }

    @Override
    public void drive(double leftPower, double rightPower, long timeMillis) throws InterruptedException {
        drive(leftPower, rightPower);
        Thread.sleep(timeMillis);
        drive(0, 0);
    }

    @Override
    public void drive(double leftPower, double rightPower) {
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }

    @Override
    public void driveToInch(double power, double inches) {

    }

    @Override
    public void resetDriveMotors() {

    }

    @Override
    public int getDriveEncoderAverage() {
        return 0;
    }

    @Override
    public void resetDriveEncoders() {

    }

    public DistanceSensor getDistanceSensor() {
        return distanceSensor;
    }

    public WebcamName getW1() {
        return w1;
    }

    public WebcamName getW2() {
        return w2;
    }
}
