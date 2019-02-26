package org.ftc7244.robotcontroller.opmodes.tests;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning.DeadReckoningBase;

import java.util.concurrent.atomic.AtomicBoolean;

public class StallTests extends DeadReckoningBase {
    public StallTests() {
        super(false);
    }

    AtomicBoolean stop;
    Runnable checkForStop;
    @Override
    protected void run() {
        checkForStop = () -> {
            sleep(750);
            double max = 0;
            while (opModeIsActive() && !stop.get()){
                if(!(robot.getLeftDrive2().getVelocity(AngleUnit.DEGREES) > max)){
                    stop.set(true);
                }
                max = robot.getLeftDrive2().getVelocity(AngleUnit.DEGREES);
            }
        };
        double offset = robot.getDriveEncoderAverage();
        double target = offset + (robot.getCountsPerInch() * 20);
        robot.drive(-0.25, -0.25);
        while(opModeIsActive() && ((robot.getDriveEncoderAverage() < target) || !stop.get())){}
        robot.drive(0, 0);
    }
}
