package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.vuforia.PhoneTest;
import org.ftc7244.robotcontroller.sensor.vuforia.SynchronousCameraProvider;
import org.ftc7244.robotcontroller.sensor.vuforia.WebTest;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.PhoneCamInitializer;
import org.ftc7244.robotcontroller.sensor.vuforia.initializer.WebCamInitializer;
@TeleOp(name="WebCamSwitch")
public class WebCamSwitch extends LinearOpMode {
    PhoneTest phone;
    WebTest web = new WebTest();
    Robot robot;
    double currentTime;
    Boolean webInit = false;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        while(!web.init(robot) && opModeIsActive()){}
        telemetry.addLine("Hol up");
        telemetry.update();
        currentTime = 0;
        waitForStart();
        while(currentTime < 50000){
            telemetry.addData("Time", currentTime);
            telemetry.update();
            currentTime += 1;
        }
        web.stop();
        phone = new PhoneTest();
        while(!webInit){
            webInit = phone.init(robot);
        }
        while(opModeIsActive()){
            telemetry.addLine("YAY");
            telemetry.update();
        }
    }

}
