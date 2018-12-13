package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
@Autonomous(name="Turn45Test")
public class turn45test extends DeadReckoningBase {

    public turn45test() {
        super (false);
    }

    @Override
    protected void run() {
        rotateGyro(45);
    }
}
