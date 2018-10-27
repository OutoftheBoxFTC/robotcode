package org.ftc7244.robotcontroller.autonamous.drive.terminator;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonamous.drive.DriveProcedure;

/**
 * Once the control loop has reached a certain level of accuracy the sensitivity terminator will trigger
 * after a time period is passed to prevent early termination of the object passes the target value
 * and over compensated
 */
public class SensitivityTerminator extends DriveProcedureTerminator {

    private long timestamp, successDuration;
    private double maximumError, target, oldTime;
    private DriveProcedure context;

    /**
     * Requires the control system context to know if the values are in the target value and if they are within
     * a maximum amount of error kill the control loop.
     *
     * @param context         the Drive Procedure being used in the ${@link org.ftc7244.robotcontroller.autonamous.control.DriveController}
     * @param target          the target value of the rotation
     * @param maximumError    the absolute value of error the control system can have
     * @param successDuration how long after the target value must the target value retain before terminating
     */
    public SensitivityTerminator(DriveProcedure context, double target, double maximumError, long successDuration) {
        this.target = target;
        this.maximumError = maximumError;
        this.successDuration = successDuration;
        this.context = context;
        this.timestamp = -1;
    }

    @Override
    public boolean shouldTerminate(double error) {
        if (timestamp == -1 && Math.abs(error) < maximumError) {
            timestamp = System.currentTimeMillis();
            oldTime = System.currentTimeMillis();
        }

        else if (Math.abs(error) > maximumError) timestamp = -1;
        return Math.abs(System.currentTimeMillis() - timestamp) > successDuration && timestamp != -1;

    }
}