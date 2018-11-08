package org.ftc7244.robotcontroller.opmodes.autonamous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;

@Autonomous(name = "Crater Auto")
public class CraterAuto extends AutonomousProcedure {
    @Override
    protected void run() throws InterruptedException{
        robot.drive(1, 1, 500);
    }
}
