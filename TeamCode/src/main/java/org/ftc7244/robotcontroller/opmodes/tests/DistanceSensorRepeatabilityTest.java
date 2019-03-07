package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning.DeadReckoningBase;
@Autonomous
public class DistanceSensorRepeatabilityTest extends DeadReckoningBase {
    public DistanceSensorRepeatabilityTest() {
        super(false);
    }

    @Override
    protected void run() {
        while (opModeIsActive()){
            driveToWall(300, -0.5, 100, robot.getBackDistanceSensor());
            while(opModeIsActive() && !gamepad1.a) {
                telemetry.addData("Distance", robot.getBackDistanceSensor().getDistance(DistanceUnit.INCH));
                telemetry.update();
            }
        }
    }
}
