package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.pixycam.Pixycam2Provider;

@TeleOp
public class IntakePixyProviderTests extends LinearOpMode {
    Robot robot;
    Pixycam2Provider pixyGold, pixySilver;
    int counter = 0, max = 0;
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
            if(pixyGold.getWidth() > 290 || pixySilver.getWidth() > 215                                                                                                                                                  || (pixyGold.getWidth() > 1 && pixySilver.getWidth() > 1)){
                telemetry.addData("Two Minerals", true);
                //robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.GREEN);
                counter ++;
            }else{
                telemetry.addData("Two Minerals", false);
                if(pixyGold.getWidth() == -1 && pixySilver.getWidth() == -1) {
                    //robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.CP1_SHOT);
                }else{
                    //robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED);
                }
                max = Math.max(counter, max);
                counter = 0;
            }
            telemetry.addData("Max", max);
            telemetry.update();
        }
    }
}