package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamProvider;
import org.ftc7244.robotcontroller.sensor.ultrasonic.SickUltrasonic;

public class Robot extends Hardware {
    private static final double COUNTS_PER_INCH = 39.5138889;

    private RevBlinkinLedDriver blinkinLedDriver;
    private WebcamName w1, w2;
    private SickUltrasonic leadingLeftUS, leadingRightUS, trailingLeftUS, trailingRightUS;
    private DcMotorEx leftDrive, rightDrive;
    private DcMotor intake, raisingArm1, raisingArm2;
    private BNO055IMU imu;
    private I2cDeviceSynch goldI2c, silverI2c, sampleI2c;
    private Servo latch, lid;

    public Robot(LinearOpMode opMode) {
        super(opMode, COUNTS_PER_INCH);
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
        raisingArm1 = getOrNull(map.dcMotor, "arm1");
        raisingArm2 = getOrNull(map.dcMotor, "arm2");
        goldI2c = getOrNull(map, I2cDeviceSynch.class, "intakePixy");
        silverI2c = getOrNull(map, I2cDeviceSynch.class, "intakePixy");
        sampleI2c = getOrNull(map, I2cDeviceSynch.class, "sample");
        imu = getOrNull(map, BNO055IMU.class, "imu");

        leftDrive = getOrNull(map, DcMotorEx.class, "leftDrive");
        rightDrive = getOrNull(map, DcMotorEx.class, "rightDrive");
        intake = getOrNull(map.dcMotor, "intake");
        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        latch = getOrNull(map.servo, "latch");
        lid = getOrNull(map.servo, "lid");
        blinkinLedDriver = getOrNull(map, RevBlinkinLedDriver.class, "blinkin");
        //sampleI2c = getOrNull(map, I2cDeviceSynch.class, "sample");
        resetMotors(raisingArm2);
    }

    @Override
    public void initServos() {
        getLid().setPosition(0.8);
    }

    public void moveArm(double power){
        power = -power;
        raisingArm1.setPower(power);
        raisingArm2.setPower(power);
    }

    public void intake(double power){
        intake.setPower(power);
    }

    public void intake(double power, PixycamProvider pixyGold, PixycamProvider pixySilver){
        if(pixyGold.getWidth() < 200 && pixySilver.getWidth() < 200){
            intake.setPower(power);
        }else{
            intake.setPower(0);
        }
    }

    public void intake(double power, PixycamProvider pixy){
        if(pixy.getWidth() < 250){
            intake.setPower(1);
        }else{
            intake.setPower(0);
        }
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
        double target = getDriveEncoderAverage() + (inches * countsPerInch);
        leftDrive.setPower(-1 * power);
        rightDrive.setPower(-1 * power);
        if(power > 0) {
            while (getDriveEncoderAverage() <= target) {
                opMode.telemetry.addData("encoder", getDriveEncoderAverage());
                opMode.telemetry.update();
            }
        }else{
            while (getDriveEncoderAverage() >= target) {
                opMode.telemetry.addData("encoder", getDriveEncoderAverage());
                opMode.telemetry.update();
            }
        }
        leftDrive.setPower(0);
        rightDrive.setPower(0);
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

    public DcMotorEx getLeftDrive() {
        return leftDrive;
    }

    public DcMotorEx getRightDrive() {
        return rightDrive;
    }

    public I2cDeviceSynch getGoldI2c() {
        return goldI2c;
    }

    public I2cDeviceSynch getSilverI2c() {
        return silverI2c;
    }

    public DcMotor getRaisingArm1() {
        return raisingArm1;
    }

    public DcMotor getRaisingArm2() {
        return raisingArm2;
    }

    public Servo getLatch() {
        return latch;
    }

    public I2cDeviceSynch getSampleI2c() {
        return sampleI2c;
    }

    public Servo getLid() {
        return lid;
    }

    public RevBlinkinLedDriver getBlinkinLedDriver() {
        return blinkinLedDriver;
    }
}
