package org.ftc7244.robotcontroller.sensor.gyroscope;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.ftc7244.robotcontroller.hardware.Robot;

public class RevIMUProvider extends GyroscopeProvider{

    private static final double TAU = Math.PI*2;
    //this is for efficiency. otherwise, i genuinely don't care.

    private BNO055IMU imu;

    @Override
    protected double getReading(Axis axis) {
        Orientation orientation = imu.getAngularOrientation();
        double reading = Double.POSITIVE_INFINITY;
        switch (axis){
            case PITCH:
                reading =  orientation.secondAngle;
                break;
            case YAW:
                reading = orientation.firstAngle;
                break;
            case ROLL:
                reading = orientation.thirdAngle;
                break;
        }
        return ((reading%TAU)+TAU)%TAU;
    }

    @Override
    public boolean isCalibrated() {
        return imu.isGyroCalibrated();
    }

    public void init(Robot robot) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu = robot.getIMU();
        imu.initialize(parameters);
    }
}