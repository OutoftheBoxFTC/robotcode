package org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator;

import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;

public abstract class DriveTerminator {
    public abstract boolean shouldTerminate(double error);
}
