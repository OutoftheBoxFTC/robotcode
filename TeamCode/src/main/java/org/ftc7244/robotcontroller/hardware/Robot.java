package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.ftc7244.robotcontroller.sensor.ultrasonic.SickUltrasonic;

public class Robot extends Hardware {
    private WebcamName w1, w2;
    private SickUltrasonic leadingLeftUS, leadingRightUS, trailingLeftUS, trailingRightUS;
    private DcMotor leftDrive, rightDrive;
    private BNO055IMU imu;

    public Robot(LinearOpMode opMode) {
        super(opMode, 1);
        //TODO determine counts per inch
    }

    @Override
    public void init() {
        HardwareMap map = opMode.hardwareMap;

        w1 = getOrNull(map, WebcamName.class, "w1");
        w2 = getOrNull(map, WebcamName.class, "w2");
        leadingLeftUS = new SickUltrasonic(getOrNull(map.analogInput, "leadingLeftUS"));
        leadingRightUS = new SickUltrasonic(getOrNull(map.analogInput, "leadingRightUS"));
        trailingLeftUS = new SickUltrasonic(getOrNull(map.analogInput, "trailingLeftUS"));
        trailingRightUS = new SickUltrasonic(getOrNull(map.analogInput, "trailingRightUS"));
        imu = getOrNull(map, BNO055IMU.class, "imu");

        leftDrive = getOrNull(map, DcMotor.class, "leftDrive");
        rightDrive = getOrNull(map, DcMotor.class, "rightDrive");
        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
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
        return (leftDrive.getCurrentPosition()+rightDrive.getCurrentPosition())/2;
    }

    @Override
    public void resetDriveEncoders() {

    }

    public WebcamName getW1() {
        return w1;
    }

    public WebcamName getW2() {
        return w2;
    }

    public SickUltrasonic getLeadingLeftUS() {
        return leadingLeftUS;
    }

    public SickUltrasonic getTrailingLeftUS() {
        return trailingLeftUS;
    }

    public SickUltrasonic getLeadingRightUS() {
        return leadingRightUS;
    }

    public SickUltrasonic getTrailingRightUS() {
        return trailingRightUS;
    }

    public BNO055IMU getIMU() {
        return imu;
    }
}
