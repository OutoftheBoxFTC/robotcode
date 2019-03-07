package org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class DistanceSensorTerminator extends DriveTerminator {
    DistanceSensor sensor;
    double terminationDistance;
    public DistanceSensorTerminator(DistanceSensor sensor, double terminationDistance){
        this.sensor = sensor;
        this.terminationDistance = terminationDistance;
    }
    @Override
    public boolean shouldTerminate(double error) {
        return sensor.getDistance(DistanceUnit.INCH) <= terminationDistance;
    }
}
