package org.ftc7244.robotcontroller.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot extends Hardware {
    private BNO055IMU imu;
    private SmartMotor a, b, c, d;

    public Robot(LinearOpMode opMode) {
        super(opMode);
    }

    @Override
    public void init() {
        HardwareMap map = opMode.hardwareMap;
        a = new SmartMotor((DcMotorEx)getOrNull(map.dcMotor, "a"));
        b = new SmartMotor((DcMotorEx)getOrNull(map.dcMotor, "b"));
        c = new SmartMotor((DcMotorEx)getOrNull(map.dcMotor, "c"));
        d = new SmartMotor((DcMotorEx)getOrNull(map.dcMotor, "d"));
        b.getMotor().setDirection(DcMotorSimple.Direction.REVERSE);
        d.getMotor().setDirection(DcMotorSimple.Direction.REVERSE);
        imu = getOrNull(map, BNO055IMU.class, "imu");
    }

    public void drive(double left, double right){
        a.setPower(left);
        c.setPower(left);
        b.setPower(right);
        d.setPower(right);
    }

    public void drive(double a, double b, double c, double d){
        this.a.setPower(a);
        this.b.setPower(b);
        this.c.setPower(c);
        this.d.setPower(d);
    }

    @Override
    public void initServos() {

    }

    @Override
    public void disableDriveMotors() {
        a.disable();
        b.disable();
        c.disable();
        d.disable();
    }

    public BNO055IMU getIMU() {
        return imu;
    }
}