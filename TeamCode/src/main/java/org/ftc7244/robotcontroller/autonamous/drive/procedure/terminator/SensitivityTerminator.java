package org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator;

import org.ftc7244.robotcontroller.autonamous.drive.DriveController;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;

/**
 * Once the control loop has reached a certain level of accuracy the sensitivity terminator will trigger
 * after a time period is passed to prevent early termination of the object passes the target value
 * and over compensated
 */
public class SensitivityTerminator extends DriveTerminator {

    private final long successDuration;
    private final double maximumError;

    private long timestamp;

    /**
     * Requires the control system context to know if the values are in the target value and if they are within
     * a maximum amount of error kill the control loop.
     *
     * @param maximumError    the absolute value of error the control system can have
     * @param successDuration how long after the target value must the target value retain before terminating
     */
    public SensitivityTerminator(double maximumError, long successDuration) {
        this.maximumError = maximumError;
        this.successDuration = successDuration;
        this.timestamp = -1;
    }

    @Override
    public boolean shouldTerminate(double error) {
        if (timestamp == -1 && Math.abs(error) < maximumError) {
            timestamp = System.currentTimeMillis();
        }

        else if (Math.abs(error) > maximumError) timestamp = -1;
        return Math.abs(System.currentTimeMillis() - timestamp) > successDuration && timestamp != -1;

    }
}