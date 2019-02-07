package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
@Autonomous(name="Turn45Test")
public class Turn45Test extends DeadReckoningBase {

    public Turn45Test() {
        super (false);
    }

    @Override
    protected void run() {
        rotateGyro(45);
    }
}
