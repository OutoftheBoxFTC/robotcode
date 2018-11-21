package org.ftc7244.robotcontroller.opmodes.tuning;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.opmodes.input.Button;
import org.ftc7244.robotcontroller.opmodes.input.ButtonType;
import org.ftc7244.robotcontroller.opmodes.tuning.parameter.TunableDecimal;

public abstract class ControlSystemTuner extends AutonomousProcedure {

    private TunableDecimal[] parameters;

    private Button aButton, bButton, dUp, dDown, dLeft, dRight;

    /**
     * D up/down: increase/decrease digit
     * D left/right: scroll through numbers
     * A: advance/run
     * B: save
     */
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
        String raw = FileSystem.loadFromFile(filePath, hardwareMap.appContext);
        if(raw==null){
            formatFile(filePath);
        }
        else {
            String[] numbers = raw.split(",");
            if(numbers.length == parameters.length) {
                for (int i = 0; i < parameters.length; i++) {
                    try {
                        parameters[i].setValue(numbers[i]);
                    }
                    catch (NumberFormatException e){
                        e.getStackTrace();
                    }
                }
            }
            else {
                formatFile(filePath);
            }
        }
        while (opModeIsActive()) {
            while (opModeIsActive() && currentParameter < parameters.length) {
                if(bButton.isUpdated() && bButton.isPressed()){
                    StringBuilder save = new StringBuilder();
                    for(TunableDecimal param : parameters){
                        save.append(param.toString()).append(",");
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
                telemetry.addData("", parameters[currentParameter].toDisplayString());
                telemetry.addData("", parameters[currentParameter].getSelectedDigit());
                telemetry.update();
                if (aButton.isUpdated() && aButton.isPressed()) {
                    currentParameter++;
                }
            }
            currentParameter = 0;

            double[] parameters = new double[this.parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                parameters[i] = this.parameters[i].getValue();
            }
            driveController.orient(0, 0, 0);
            driveController.drive(getDriveProcedure(parameters));
        }
    }

    private void formatFile(String filePath){
        StringBuilder lines = new StringBuilder();
        for(TunableDecimal decimal : parameters){
            lines.append(decimal.toString()).append(",");
        }
        FileSystem.saveToFile(lines.toString(), filePath, hardwareMap.appContext);
    }

    protected abstract TunableDecimal[] getParameters();

    protected abstract DriveProcedure getDriveProcedure(double[] parameters);

    protected abstract String getFilePath();
}
