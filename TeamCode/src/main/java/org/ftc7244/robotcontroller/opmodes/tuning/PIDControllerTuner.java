package org.ftc7244.robotcontroller.opmodes.tuning;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.control.PIDControl;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.RangeTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.TimeTerminator;
import org.ftc7244.robotcontroller.opmodes.tuning.parameter.TunableDecimal;
@Disabled
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
        return new DriveProcedure(Math.toRadians(7), 0, 0,
                new ConditionalTerminator(new TimeTerminator((long) 6e9), new SensitivityTerminator(Math.toRadians(0.3), 100))
                ,new RangeTerminator(-1, 1),
                new PIDControl(parameters[0], parameters[1], parameters[2], Math.toRadians(15), true), orientation);
    }

    @Override
    protected String getFilePath() {
        return "default_pid";
    }
}