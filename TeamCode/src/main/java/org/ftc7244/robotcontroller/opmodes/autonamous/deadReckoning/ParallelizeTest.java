package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Parallelize Test")
public class ParallelizeTest extends DeadReckoningBase {

    public ParallelizeTest() {
        super(false);
    }

    @Override
    protected void run() {
        parralelize(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000);
    }
}