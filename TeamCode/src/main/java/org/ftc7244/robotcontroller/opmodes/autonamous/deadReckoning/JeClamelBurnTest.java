package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Burn JeClamel Retrograde")
public class JeClamelBurnTest extends DeadReckoningBase {

    public JeClamelBurnTest() {
        super(false);
    }

    @Override
    protected void run() {
        burnJeClamelRetrograde();
        sleep(1500);
    }
}
