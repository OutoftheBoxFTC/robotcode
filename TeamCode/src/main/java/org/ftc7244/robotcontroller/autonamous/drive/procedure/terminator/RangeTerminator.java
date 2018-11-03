package org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator;

public class RangeTerminator extends DriveTerminator{
    private final double min, max;

    public RangeTerminator(double bound1, double bound2){
        this.min = Math.min(bound1, bound2);
        this.max = Math.max(bound1, bound2);
    }

    @Override
    public boolean shouldTerminate(double error) {
        return error > min && error < max;
    }
}
