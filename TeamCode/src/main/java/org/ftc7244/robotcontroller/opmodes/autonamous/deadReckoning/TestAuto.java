package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Test Auto")
public class TestAuto extends DeadReckoningBase {

    public TestAuto() {
        super(false);
    }

    @Override
    protected void run() {
        drive(22, 0.5);
        while (opModeIsActive()){
            telemetry.addData("Sample", sample);
            telemetry.update();
        }
    }
}