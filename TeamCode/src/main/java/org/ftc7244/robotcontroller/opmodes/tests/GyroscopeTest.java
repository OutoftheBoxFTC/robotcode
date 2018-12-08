package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning.DeadReckoningBase;
import org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

@TeleOp(name = "Gyroscope Test")
public class GyroscopeTest extends DeadReckoningBase {

    public GyroscopeTest() {
        super(false);
    }

    @Override
    protected void run() {
        while (opModeIsActive()){
            telemetry.addData("Reading", gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW));
            telemetry.update();
        }
    }
}