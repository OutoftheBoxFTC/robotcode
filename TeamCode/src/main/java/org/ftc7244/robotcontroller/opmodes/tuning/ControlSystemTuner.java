package org.ftc7244.robotcontroller.opmodes.tuning;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.opmodes.input.Button;
import org.ftc7244.robotcontroller.opmodes.input.ButtonType;
import org.ftc7244.robotcontroller.opmodes.tuning.parameter.TunableDecimal;

public abstract class ControlSystemTuner extends AutonomousProcedure {

    private TunableDecimal[] parameters;

    private Button aButton, dUp, dDown, dLeft, dRight;

    public ControlSystemTuner(TunableDecimal... parameters){
        this.parameters = parameters;
    }

    @Override
    protected void run() {
        aButton = new Button(gamepad1, ButtonType.A);
        dUp = new Button(gamepad1, ButtonType.D_PAD_UP);
        dDown = new Button(gamepad1, ButtonType.D_PAD_DOWN);
        dLeft = new Button(gamepad1, ButtonType.D_PAD_LEFT);
        dRight = new Button(gamepad1, ButtonType.D_PAD_RIGHT);

        int currentParameter = 0;
        while (opModeIsActive() && currentParameter < parameters.length){
            if(dUp.isUpdated()&&dUp.isPressed()){
                parameters[currentParameter].changeDigit(true);
            }
            if(dDown.isUpdated() && dDown.isPressed()){
                parameters[currentParameter].changeDigit(false);
            }
            if(dLeft.isUpdated() && dLeft.isPressed()){
                parameters[currentParameter].advanceDigit(true);
            }
            if(dRight.isUpdated() && dRight.isPressed()){
                parameters[currentParameter].advanceDigit(false);
            }

            Telemetry.Line line = telemetry.addLine(parameters[currentParameter].getName());
            line.addData("", parameters[currentParameter].toString());
            line.addData("", parameters[currentParameter].getSelectedDigit());
            telemetry.update();
            if(aButton.isUpdated() && aButton.isPressed()){
                currentParameter++;
            }
        }

        double[] parameters = new double[this.parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = this.parameters[i].getValue();
        }
        driveController.drive(getDriveProcedure(parameters));
    }

    protected abstract DriveProcedure getDriveProcedure(double[] parameters);
}
