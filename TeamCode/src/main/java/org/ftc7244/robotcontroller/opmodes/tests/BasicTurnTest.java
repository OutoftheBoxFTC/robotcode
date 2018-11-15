package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.autonamous.control.ConstantControl;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.RangeTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.SensitivityTerminator;

@TeleOp(name = "Basic Turn Test")
public class BasicTurnTest extends AutonomousProcedure {

    @Override
    protected void run() {
        driveController.orient(0, 0, 0);
        DriveProcedure procedure = new DriveProcedure(Math.PI/2, 24, 0.5,
                new SensitivityTerminator(Math.PI/90, 10),
                new RangeTerminator(0, Double.NEGATIVE_INFINITY),
                new ConstantControl(), orientation);
        driveController.drive(procedure);
        sleep(1000);
    }
}
