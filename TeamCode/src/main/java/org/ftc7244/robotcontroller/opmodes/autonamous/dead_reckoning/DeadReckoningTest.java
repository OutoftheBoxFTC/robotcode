package org.ftc7244.robotcontroller.opmodes.autonamous.dead_reckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Dead Reckoning Test")
public class DeadReckoningTest extends DeadReckoningBase {

    @Override
    protected void run() {
        parralelize(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000.0);
    }
}
