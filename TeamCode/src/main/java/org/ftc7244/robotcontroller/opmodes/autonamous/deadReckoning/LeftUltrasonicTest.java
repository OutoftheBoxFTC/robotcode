package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Left Ultrasonic Test")
public class LeftUltrasonicTest extends DeadReckoningBase {
    public LeftUltrasonicTest() {
        super(false);
    }
    @Override
    protected void run() {
        while (opModeIsActive()){
            double leading = robot.getLeadingRightUS().getUltrasonicLevel(), trailing = robot.getTrailingRightUS().getUltrasonicLevel();
            double error = getRotationalError(0, -getError(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25));
            telemetry.addData("leading", leading);
            telemetry.addData("trailing", trailing);
            telemetry.addData("angular offset", error);
            telemetry.update();
        }
    }
}