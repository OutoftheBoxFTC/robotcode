package org.ftc7244.robotcontroller.hardware;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.sensor.Initializable;

import java.util.ArrayList;
import java.util.Map;

public abstract class Hardware {
    protected LinearOpMode opMode;
    protected double countsPerInch;
    private ArrayList<String> errorMessages;

    public Hardware(LinearOpMode opMode, double countsPerInch) {
        this.opMode = opMode;
        this.countsPerInch = countsPerInch;
        errorMessages = new ArrayList<>();
    }

    /**
     * Get the value associated with an id and instead of raising an error return null and log it
     *
     * @param map  the hardware map from the HardwareMap
     * @param name The ID in the hardware map
     * @param <T>  the type of hardware map
     * @return the hardware device associated with the name
     */
    protected <T extends HardwareDevice> T getOrNull(@NonNull HardwareMap.DeviceMapping<T> map, String name) {
        for (Map.Entry<String, T> item : map.entrySet()) {
            if (!item.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            return item.getValue();
        }
        errorMessages.add(name + " not found!");
        RobotLog.e("ERROR: " + name + " not found!");
        return null;
    }

    protected <T> T getOrNull(@NonNull HardwareMap map, Class<T> type, String name) {
        try {
            T device = map.get(type, name);
            opMode.telemetry.addData("Device", device);
            return map.get(type, name);
        }
        catch (IllegalArgumentException e){
            errorMessages.add(name + " not found!");
            RobotLog.e("ERROR: " + name + " not found!");
        }
        return null;
    }

    public void logErrors(){
        for(String error : errorMessages){
            opMode.telemetry.addData("Config Error", error);
        }
    }



    /**
     * Waits for all the motors to have offsetReadingTo position and if it is not offsetReadingTo tell it to orient
     *
     * @param motors all the motors to orient
     */
    public static void resetMotors(@NonNull DcMotor... motors) {
        boolean notReset = true;
        while (notReset) {
            boolean allReset = true;
            for (DcMotor motor : motors) {
                if (motor.getCurrentPosition() == 0) {
                    continue;
                }
                allReset = false;
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }
            notReset = !allReset;
        }
        for (DcMotor motor : motors) motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    /**
     * Loads all needed hardware into code
     */
    public abstract void init();

    /**
     * Initializes all servo positions
     */
    public abstract void initServos();

    /**
     * Drives robot at a given power for a given amount of milliseconds
     * @param leftPower Power to driveController left side of robot
     * @param rightPower Power to driveController right side of robot
     * @param timeMillis Time to driveController robot for
     * @throws InterruptedException of code fails to terminate on stop requested
     */
    public abstract void drive(double leftPower, double rightPower, long timeMillis) throws InterruptedException;

    /**
     * Drives robot at a certain power for an indefinite amount of time
     * @param leftPower Power to driveController left side of robot
     * @param rightPower Power to driveController right side of robot
     */
    public abstract void drive(double leftPower, double rightPower);

    /**
     * Resets the direction and encoder offsets of all the driveController motors
     */
    public abstract void resetDriveMotors();

    public double getCountsPerInch() {
        return countsPerInch;
    }

    /**
     *
     * @return returns the average value of all driveController motor encoders
     */
    public abstract int getDriveEncoderAverage();

    public LinearOpMode getOpMode() {
        return opMode;
    }

    public abstract void resetDriveEncoders();
}
