package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

public class Robot extends Hardware {
    //TODO tbd
    private static final double COUNTS_PER_INCH = 0;

    private DcMotorEx flDrive, frDrive, blDrive, brDrive;
    private I2cDeviceSynch sampleI2c;
    private BNO055IMU imu;

    public Robot(LinearOpMode opMode) {
        super(opMode, COUNTS_PER_INCH);
        //TODO determine counts per inch
    }

    public Robot(OpMode opMode){
        super((LinearOpMode)opMode, COUNTS_PER_INCH);
    }

    @Override
    public void init() {
        HardwareMap map = opMode.hardwareMap;
        imu = getOrNull(map, BNO055IMU.class, "imu");

        flDrive = (DcMotorEx) getOrNull(map.dcMotor, "flDrive");
        frDrive = (DcMotorEx) getOrNull(map.dcMotor, "frDrive");
        blDrive = (DcMotorEx) getOrNull(map.dcMotor, "blDrive");
        brDrive = (DcMotorEx) getOrNull(map.dcMotor, "brDrive");

        sampleI2c = getOrNull(map, I2cDeviceSynch.class, "sample");
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
        flDrive.setPower(-leftPower);
        frDrive.setPower(-leftPower);
        blDrive.setPower(-rightPower);
        brDrive.setPower(-rightPower);
    }

    @Override
    public void resetDriveMotors() {
        configureDriveMotors(DcMotor.ZeroPowerBehavior.FLOAT, null);
    }

    @Override
    public int getDriveEncoderAverage() {
        //TODO
        return 0;
    }

    /**
     *
     * @param zeroPowerBehavior null indicates keep as is
     * @param runMode null indicates keep as is
     */
    public void configureDriveMotors(DcMotor.ZeroPowerBehavior zeroPowerBehavior, DcMotor.RunMode runMode){
        if(zeroPowerBehavior != null) {
            flDrive.setMode(runMode);
            frDrive.setMode(runMode);
            blDrive.setMode(runMode);
            brDrive.setMode(runMode);
        }
        if(runMode != null) {
            flDrive.setZeroPowerBehavior(zeroPowerBehavior);
            frDrive.setZeroPowerBehavior(zeroPowerBehavior);
            blDrive.setZeroPowerBehavior(zeroPowerBehavior);
            brDrive.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    @Override
    public void resetDriveEncoders() {

    }

    public I2cDeviceSynch getSampleI2c() {
        return sampleI2c;
    }

    public DcMotorEx getBlDrive() {
        return blDrive;
    }

    public DcMotorEx getBrDrive() {
        return brDrive;
    }

    public DcMotorEx getFlDrive() {
        return flDrive;
    }

    public DcMotorEx getFrDrive() {
        return frDrive;
    }

    public BNO055IMU getIMU() {
        return imu;
    }
}