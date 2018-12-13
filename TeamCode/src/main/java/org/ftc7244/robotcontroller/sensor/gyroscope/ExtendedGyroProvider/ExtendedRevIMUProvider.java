package org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

public class ExtendedRevIMUProvider extends ExtendedGyroscopeProvider{

    private BNO055IMU imu;

    public ExtendedRevIMUProvider(){
        super();
    }

    @Override
    protected double getReading(Axis axis) {
        Orientation orientation = imu.getAngularOrientation();
        switch (axis){
            case PITCH:
                return (orientation.secondAngle);
            case YAW:
                return (orientation.firstAngle);
            case ROLL:
                return (orientation.thirdAngle);
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public boolean isCalibrated() {
        return imu.isGyroCalibrated();
    }

    @Override
    public void init(Robot robot) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        imu = robot.getIMU();
        imu.initialize(parameters);
    }
}