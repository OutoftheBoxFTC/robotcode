package org.ftc7244.robotcontroller.opmodes.tuning;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;

public abstract class ControlSystemTuner extends AutonomousProcedure {

    private String[] parameterNames;

    public ControlSystemTuner(String... parameters){

    }

    @Override
    protected void run() {
        telemetry.addLine();
    }
}
