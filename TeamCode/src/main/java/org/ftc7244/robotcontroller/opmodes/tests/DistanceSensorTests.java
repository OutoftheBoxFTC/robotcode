package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning.DeadReckoningBase;
@Autonomous
public class DistanceSensorTests extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this);
        robot.init();
        waitForStart();
        while (opModeIsActive()){
            telemetry.addData("Distance", robot.getBackDistanceSensor().getDistance(DistanceUnit.INCH));
            telemetry.update();
        }
    }
}
