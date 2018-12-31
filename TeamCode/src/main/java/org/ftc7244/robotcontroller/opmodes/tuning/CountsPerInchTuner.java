package org.ftc7244.robotcontroller.opmodes.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.opmodes.input.Button;
import org.ftc7244.robotcontroller.opmodes.input.ButtonType;
import org.ftc7244.robotcontroller.opmodes.tuning.parameter.TunableDecimal;

public class CountsPerInchTuner extends LinearOpMode {
    TunableDecimal count;
    private Button aButton, dUp, dDown, dLeft, dRight;
    double offset = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(this);
        robot.init();
        count = new TunableDecimal(0, "Counts");
        aButton = new Button(gamepad1, ButtonType.A);
        dUp = new Button(gamepad1, ButtonType.D_PAD_UP);
        dDown = new Button(gamepad1, ButtonType.D_PAD_DOWN);
        dLeft = new Button(gamepad1, ButtonType.D_PAD_LEFT);
        dRight = new Button(gamepad1, ButtonType.D_PAD_RIGHT);
        while(opModeIsActive()){
            if(!aButton.isPressed()) {
                if (dUp.isUpdated() && dUp.isPressed()) {
                    count.changeDigit(true);
                }
                if (dDown.isUpdated() && dDown.isPressed()) {
                    count.changeDigit(false);
                }
                if (dLeft.isUpdated() && dLeft.isPressed()) {
                    count.advanceDigit(true);
                }
                if (dRight.isUpdated() && dRight.isPressed()) {
                    count.advanceDigit(false);
                }
                telemetry.addData("", count.getName());
                telemetry.addData("", count.toDisplayString());
                telemetry.addData("", count.getSelectedDigit());
                telemetry.update();
                offset = robot.getDriveEncoderAverage();
            }
            if(aButton.isPressed()){
                if(robot.getDriveEncoderAverage() - offset < (15 * count.getValue())){
                    robot.drive(0.5, 0.5);
                }
            }
        }
    }
}
