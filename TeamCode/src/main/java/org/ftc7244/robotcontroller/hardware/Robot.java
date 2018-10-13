package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.ftc7244.robotcontroller.sensor.ultrasonic.SickUltrasonic;

public class Robot extends Hardware {
    private WebcamName w1, w2;
    private SickUltrasonic leadingLeftUS, leadingRightUS, trailingLeftUS, trailingRightUS;

    public Robot(OpMode opMode) {
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
}
