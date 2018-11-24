package org.ftc7244.robotcontroller.opmodes.autonamous.dead_reckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous
public class TestAuto extends DeadReckoningBase {
    @Override
    protected void run() {
        drive(22, 0.5);
        while (opModeIsActive()){
            telemetry.addData("Sample", sample);
            telemetry.update();
        }
    }
}
