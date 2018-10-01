package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.ftc7244.robotcontroller.hardware.Robot;

import java.util.ArrayList;

@TeleOp(name = "Ir Sensor Test")
public class IrSensorTest extends LinearOpMode {
    private static final int BUFFER_SIZE = 10;
    @Override
    public void runOpMode() {
        Robot robot = new Robot(this);
        robot.init();
        waitForStart();
        ArrayList<Double> dataBuffer = new ArrayList<>();
        while (opModeIsActive() && ! isStopRequested()){
            double distance = robot.getDistanceSensor().getDistance(DistanceUnit.INCH);
            dataBuffer.add(distance);

            if(dataBuffer.size()>BUFFER_SIZE){
                dataBuffer.remove(0);
            }
            double mean = 0;
            for (double data : dataBuffer){
                mean += data;
            }
            mean /= BUFFER_SIZE;
            telemetry.addData("Distance (IN)", mean);

            double mad = 0;
            for (double data : dataBuffer){
                mad += Math.abs(mean-data);
            }
            telemetry.addData("MAD (IN)", mad/BUFFER_SIZE);
            telemetry.update();
        }
    }
}
