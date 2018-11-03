package org.ftc7244.robotcontroller.sensor.gyroscope;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.ftc7244.robotcontroller.hardware.Robot;

public class RevIMUProvider extends GyroscopeProvider{

    private BNO055IMU imu;

    @Override
    protected double getReading(Axis axis) {
        Orientation orientation = imu.getAngularOrientation();
        switch (axis){
            case PITCH:
                return orientation.firstAngle;
            case YAW:
                return orientation.secondAngle;
            case ROLL:
                return orientation.thirdAngle;
        }
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public void init(Robot robot) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json";
        imu = robot.getIMU();
        imu.initialize(parameters);
    }
}