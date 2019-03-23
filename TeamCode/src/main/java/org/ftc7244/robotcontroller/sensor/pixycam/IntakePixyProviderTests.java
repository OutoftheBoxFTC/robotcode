package org.ftc7244.robotcontroller.sensor.pixycam;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Robot;
@TeleOp
public class IntakePixyProviderTests extends LinearOpMode {
    Robot robot;
    Pixycam2Provider pixyGold, pixySilver;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        robot.init();
        pixyGold = new Pixycam2Provider(Pixycam2Provider.Mineral.GOLD, robot.getIntakeI2c());
        pixySilver = new Pixycam2Provider(Pixycam2Provider.Mineral.SILVER, robot.getIntakeI2c());
        pixyGold.start();
        pixySilver.start();
        pixySilver.setLamps(true, true);
        waitForStart();
        while (opModeIsActive()){
            pixyGold.update();
            pixySilver.update();
            telemetry.addData("Gold Width", pixyGold.getWidth());
            telemetry.addData("Silver Width", pixySilver.getWidth());
            if(pixyGold.getWidth() > 290 || pixySilver.getWidth() > 290 || (pixyGold.getWidth() > 1 && pixySilver.getWidth() > 1)){
                telemetry.addData("Two Minerals", true);
            }else{
                telemetry.addData("Two Minerals", false);
            }
            telemetry.update();
        }
    }
}
