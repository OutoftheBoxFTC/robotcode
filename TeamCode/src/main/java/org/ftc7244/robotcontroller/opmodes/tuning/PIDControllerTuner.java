package org.ftc7244.robotcontroller.opmodes.tuning;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.control.PIDControl;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.RangeTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.SensitivityTerminator;
import org.ftc7244.robotcontroller.opmodes.tuning.parameter.TunableDecimal;

@TeleOp(name = "PID Controller Tuner")
public class PIDControllerTuner extends ControlSystemTuner {

    @Override
    protected TunableDecimal[] getParameters() {
        return new TunableDecimal[]{
                new TunableDecimal(0, "KP"), new TunableDecimal(0, "KI"), new TunableDecimal(0, "KD")
        };
    }

    @Override
    protected DriveProcedure getDriveProcedure(double[] parameters) {
        return new DriveProcedure(orientation.getR()+Math.PI/2, 0, 0,
                new SensitivityTerminator(Math.toRadians(2), 10), new RangeTerminator(0, Double.POSITIVE_INFINITY),
                new PIDControl(parameters[0], parameters[1], parameters[2], true), orientation);
    }

    @Override
    protected String getFilePath() {
        return "default_pid";
    }
}