package org.ftc7244.robotcontroller.opmodes.tuning;

import android.os.Environment;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.opmodes.input.Button;
import org.ftc7244.robotcontroller.opmodes.input.ButtonType;
import org.ftc7244.robotcontroller.opmodes.tuning.parameter.TunableDecimal;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ControlSystemTuner extends AutonomousProcedure {

    private TunableDecimal[] parameters;

    private Button aButton, bButton, dUp, dDown, dLeft, dRight;

    @Override
    protected void run() {
        aButton = new Button(gamepad1, ButtonType.A);
        dUp = new Button(gamepad1, ButtonType.D_PAD_UP);
        dDown = new Button(gamepad1, ButtonType.D_PAD_DOWN);
        dLeft = new Button(gamepad1, ButtonType.D_PAD_LEFT);
        dRight = new Button(gamepad1, ButtonType.D_PAD_RIGHT);
        bButton = new Button(gamepad1, ButtonType.B);

        parameters = getParameters();
        int currentParameter = 0;

        String filePath = getFilePath();
        String lines = FileSystem.loadFromFile(filePath, hardwareMap.appContext);
        if(lines==null){
            lines = "";
            FileSystem.saveToFile(lines, filePath, hardwareMap.appContext);
        }
        else {
            String[] numbers = lines.split(",");
            for (int i = 0; i < parameters.length; i++) {
                parameters[i].setValue(Double.parseDouble(numbers[i]));
            }
        }

        while (opModeIsActive()) {
            while (opModeIsActive() && currentParameter < parameters.length) {
                if(bButton.isUpdated() && bButton.isPressed()){
                    StringBuilder save = new StringBuilder();
                    for(TunableDecimal param : parameters){
                        save.append(param).append(",");
                    }
                    FileSystem.saveToFile(save.toString(), filePath, hardwareMap.appContext);
                }

                if (dUp.isUpdated() && dUp.isPressed()) {
                    parameters[currentParameter].changeDigit(true);
                }
                if (dDown.isUpdated() && dDown.isPressed()) {
                    parameters[currentParameter].changeDigit(false);
                }
                if (dLeft.isUpdated() && dLeft.isPressed()) {
                    parameters[currentParameter].advanceDigit(true);
                }
                if (dRight.isUpdated() && dRight.isPressed()) {
                    parameters[currentParameter].advanceDigit(false);
                }
                telemetry.addData("", parameters[currentParameter].getName());
                telemetry.addData("", parameters[currentParameter].toString());
                telemetry.addData("", parameters[currentParameter].getSelectedDigit());
                telemetry.update();
                if (aButton.isUpdated() && aButton.isPressed()) {
                    currentParameter++;
                }
            }
            currentParameter = 0;

            /*double[] parameters = new double[this.parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = this.parameters[i].getValue();
            }
            driveController.drive(getDriveProcedure(parameters));*/
        }
    }

    protected abstract TunableDecimal[] getParameters();

    protected abstract DriveProcedure getDriveProcedure(double[] parameters);

    protected abstract String getFilePath();
}
