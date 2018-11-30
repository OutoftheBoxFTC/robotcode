package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Right Parallelize Test")
public class ParallelizeTestRight extends DeadReckoningBase {

    public ParallelizeTestRight() {
        super(false);
    }

    @Override
    protected void run() {
        parralelize(robot.getTrailingRightUS(), robot.getLeadingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000);
    }
}