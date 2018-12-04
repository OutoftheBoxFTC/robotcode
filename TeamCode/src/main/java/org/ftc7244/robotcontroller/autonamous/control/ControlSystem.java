package org.ftc7244.robotcontroller.autonamous.control;

import android.content.Context;

import org.ftc7244.robotcontroller.opmodes.tuning.FileSystem;

import java.math.BigDecimal;

public abstract class ControlSystem {

    /**
     *
     * @param error the error value for which the control system is correcting
     * @return the correction power offset
     */
    public abstract double correction(double error);

    public abstract void reset();

    public static double[] configFromFile(String path, Context context){
        String raw = FileSystem.loadFromFile(path, context);
        String[] split = raw.split(",");
        BigDecimal[] parameters = new BigDecimal[split.length];

        for (int i = 0; i < split.length; i++) {
            try {
                parameters[i] = new BigDecimal(split[i]);
            }
            catch (NumberFormatException e){
                e.getStackTrace();
            }
        }
        double[] decimalParams = new double[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            decimalParams[i] = parameters[i].doubleValue();
        }
        return decimalParams;
    }

}