package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Autonomous(name = "Arc Test")
public class ArcTest extends DeadReckoningBase {

    public ArcTest() {
        super(false);
    }

    @Override
    protected void run() {
        driveArc(36, 90, 0.5);
    }
}
