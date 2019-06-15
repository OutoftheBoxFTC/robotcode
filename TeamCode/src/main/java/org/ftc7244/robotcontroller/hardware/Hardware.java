package org.ftc7244.robotcontroller.hardware;

import android.support.annotation.NonNull;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import java.util.ArrayList;
import java.util.Map;

public abstract class Hardware {
    protected LinearOpMode opMode;
    private ArrayList<String> errors;

    public Hardware(LinearOpMode opMode) {
        this.opMode = opMode;
        errors = new ArrayList<>();
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
        errors.add(name + " not found!");
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
            errors.add(name + " not found!");
            RobotLog.e("ERROR: " + name + " not found!");
        }
        return null;
    }

    /**
     * Loads all needed hardware into code
     */
    public abstract void init();

    /**
     * Initializes all servo positions
     */
    public abstract void initServos();

    public abstract void disableDriveMotors();

    public LinearOpMode getOpMode() {
        return opMode;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }
}
