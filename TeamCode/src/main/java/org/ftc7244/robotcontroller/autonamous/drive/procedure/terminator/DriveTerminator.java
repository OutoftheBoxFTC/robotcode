package org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator;

public abstract class DriveTerminator {
    public abstract boolean shouldTerminate(double error);
}
