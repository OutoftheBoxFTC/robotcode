package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Orient With Right US")
public class OrientWithRightUSTest extends DeadReckoningBase {
    public OrientWithRightUSTest() {
        super(false);
    }

    @Override
    protected void run() {
        orientGyroToWall(0, robot.getTrailingLeftUS(), robot.getLeadingLeftUS());
        drive(50, 0.5, 0);
    }
}
