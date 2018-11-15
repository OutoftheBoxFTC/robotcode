package org.ftc7244.robotcontroller.autonamous.drive.orientation;

import org.ftc7244.robotcontroller.hardware.Robot;

public class DistanceProvider {
    private Robot robot;
    private double encoderOffset;

    public DistanceProvider(Robot robot){
        this.robot = robot;
    }

    public double getEncoderAverage(){
        return (robot.getDriveEncoderAverage()-encoderOffset)/robot.getCountsPerInch();
    }

    public void orient(double distance){
        encoderOffset = (robot.getDriveEncoderAverage()/robot.getCountsPerInch()-distance)*robot.getCountsPerInch();
    }
}
