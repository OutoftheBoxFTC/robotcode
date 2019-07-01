package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.ftc7244.robotcontroller.sensor.ultrasonic.SickUltrasonic;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.ExpansionHubServo;
import org.openftc.revextensions2.RevBulkData;
import org.openftc.revextensions2.RevExtensions2;

public class SmartRobot extends Hardware {
    private static final double COUNTS_PER_INCH = 342.5; //342.5, 317.25

    private WebcamName w1, w2;
    private SickUltrasonic leadingLeftUS, leadingRightUS, trailingLeftUS, trailingRightUS;
    private SmartMotor leftDrive, rightDrive, leftDrive2, rightDrive2, intake;
    private SmartMotor raisingArm1, raisingArm2;
    private BNO055IMU imu;
    private I2cDeviceSynch intakeI2c, sampleI2c;
    private SmartServo latch, lid, intakeLatch, jeClamelBurner;
    private DigitalChannel armSwitch;
    private RevBlinkinLedDriver backBlinkin, sidePanelBlinkin;
    private ExpansionHubEx expansionHub;

    private RevBulkData bulkData;


    public SmartRobot(LinearOpMode opMode) {
        super(opMode, COUNTS_PER_INCH);
    }

    @Override
    public void init() {
        RevExtensions2.init();

        HardwareMap map = opMode.hardwareMap;
        expansionHub = getOrNull(map, ExpansionHubEx.class, "Expansion Hub 2");

        w1 = getOrNull(map, WebcamName.class, "w1");
        w2 = getOrNull(map, WebcamName.class, "w2");
        leadingLeftUS = new SickUltrasonic(getOrNull(map.analogInput, "leadingLeftUS"));
        leadingRightUS = new SickUltrasonic(getOrNull(map.analogInput, "leadingRightUS"));
        trailingLeftUS = new SickUltrasonic(getOrNull(map.analogInput, "trailingLeftUS"));
        trailingRightUS = new SickUltrasonic(getOrNull(map.analogInput, "trailingRightUS"));
        armSwitch = getOrNull(map.digitalChannel, "intakeSwitch");
        intakeI2c = getOrNull(map, I2cDeviceSynch.class, "intakePixy");
        sampleI2c = getOrNull(map, I2cDeviceSynch.class, "sample");
        imu = getOrNull(map, BNO055IMU.class, "imu");


        raisingArm1 = new SmartMotor((ExpansionHubMotor) getOrNull(map.dcMotor, "arm1"), this);
        raisingArm2 = new SmartMotor((ExpansionHubMotor) getOrNull(map.dcMotor, "arm2"), this);
        leftDrive = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "leftDrive"), this);
        rightDrive = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "rightDrive"), this);
        leftDrive2 = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "leftDrive2"), this);
        rightDrive2 = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "rightDrive2"), this);
        intake = new SmartMotor((ExpansionHubMotor)getOrNull(map.dcMotor, "intake"), this);


        latch = new SmartServo((ExpansionHubServo)getOrNull(map.servo, "latch"));
        lid = new SmartServo((ExpansionHubServo)getOrNull(map.servo, "lid"));
        jeClamelBurner = new SmartServo((ExpansionHubServo)getOrNull(map.servo, "jeClamelBurner"));
        intakeLatch = new SmartServo((ExpansionHubServo)getOrNull(map.servo, "intakeKicker"));

        sidePanelBlinkin = getOrNull(map, RevBlinkinLedDriver.class, "blinkin1");
        backBlinkin = getOrNull(map, RevBlinkinLedDriver.class, "blinkin2");

        leftDrive.getMotor().setDirection(DcMotorSimple.Direction.REVERSE);
        leftDrive2.getMotor().setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void initServos() {
        RobotLog.e("1: " + lid + ", 2: " + latch + ", 3: " + jeClamelBurner);
        getLid().setPosition(0.8);
        latch.setPosition(0.2);
        jeClamelBurner.setPosition(0.45);
        getLid().setPosition(0.85);
        latch.setPosition(0.2);
    }

    public void moveArm(double power){
        power = -power;
        raisingArm1.setPower(power);
        raisingArm2.setPower(power);
    }

    public void intake(double power){
        intake.setPower(power);
    }

    @Override
    public void drive(double leftPower, double rightPower, long timeMillis) throws InterruptedException {
        drive(leftPower, rightPower);
        Thread.sleep(timeMillis);
        drive(0, 0);
    }

    @Override
    public void drive(double leftPower, double rightPower) {
        leftDrive.setPower(-leftPower);
        leftDrive2.setPower(-leftPower);
        rightDrive.setPower(-rightPower);
        rightDrive2.setPower(-rightPower);
    }

    @Override
    public void driveToInch(double power, double inches) {
        double target = getDriveEncoderAverage() + (inches * countsPerInch);
        leftDrive.setPower(-1 * power);
        rightDrive.setPower(-1 * power);
        leftDrive2.setPower(-1 * power);
        rightDrive2.setPower(-1 * power);
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
        leftDrive2.setPower(0);
        rightDrive2.setPower(0);
    }

    public void brakeDriveMotors(double direction){
        drive(direction, direction);
    }

    @Override
    public void resetDriveMotors() {
        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public int getDriveEncoderAverage() {
        return (-1 * leftDrive2.getMotor().getCurrentPosition());
    }

    public void setDriveZeroPowerBehaviour(DcMotor.ZeroPowerBehavior zeroPowerBehavior){
        leftDrive.setZeroPowerBehavior(zeroPowerBehavior);
        rightDrive.setZeroPowerBehavior(zeroPowerBehavior);
        leftDrive2.setZeroPowerBehavior(zeroPowerBehavior);
        rightDrive2.setZeroPowerBehavior(zeroPowerBehavior);
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

    public SmartMotor getLeftDrive() {
        return leftDrive;
    }

    public SmartMotor getRightDrive() {
        return rightDrive;
    }

    public I2cDeviceSynch getIntakeI2c() {
        return intakeI2c;
    }

    public SmartMotor getRaisingArm1() {
        return raisingArm1;
    }

    public SmartMotor getRaisingArm2() {
        return raisingArm2;
    }

    public SmartMotor getIntake(){return intake;}

    public SmartServo getLatch() {
        return latch;
    }

    public I2cDeviceSynch getSampleI2c() {
        return sampleI2c;
    }

    public SmartServo getLid() {
        return lid;
    }

    public SmartServo getIntakeLatch(){
        return intakeLatch;
    }

    public DigitalChannel getArmSwitch(){
        return armSwitch;
    }

    public SmartMotor getLeftDrive2() {
        return leftDrive2;
    }

    public SmartMotor getRightDrive2() {
        return rightDrive2;
    }

    public SmartServo getJeClamelBurner() {
        return jeClamelBurner;
    }

    public RevBlinkinLedDriver getSidePanelBlinkin() {
        return sidePanelBlinkin;
    }

    public RevBlinkinLedDriver getBackBlinkin() {
        return backBlinkin;
    }

    public RevBulkData getBulkData() {
        return bulkData;
    }

    public boolean armSwitchState(){
        return bulkData.getDigitalInputState(armSwitch);
    }

    public void bulkRead(){
        bulkData = expansionHub.getBulkInputData();
    }
}