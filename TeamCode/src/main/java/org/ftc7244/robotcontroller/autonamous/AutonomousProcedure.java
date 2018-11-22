package org.ftc7244.robotcontroller.autonamous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonamous.drive.DriveController;
import org.ftc7244.robotcontroller.autonamous.drive.orientation.Orientation;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamProvider;
import org.ftc7244.robotcontroller.sensor.ultrasonic.UltrasonicSystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AutonomousProcedure extends LinearOpMode {

    protected Robot robot;
    protected UltrasonicSystem ultrasonic;
    protected GyroscopeProvider gyroscope;
    private ExecutorService threadManager;
    protected DriveController driveController;
    protected Orientation orientation;

    private PixycamProvider pixycam;

    private long lastTime;

    @Override
    public void runOpMode() throws InterruptedException {
        lastTime = 0;

        robot = new Robot(this);
        robot.init();
        robot.getLeftDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getRightDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.initServos();
        threadManager = Executors.newCachedThreadPool();

        orientation = new Orientation(0, 0, 0);
        ultrasonic = new UltrasonicSystem(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), robot.getLeadingRightUS(), robot.getTrailingRightUS());
        gyroscope = new RevIMUProvider();
        //TODO initialize pixycam

        driveController = new DriveController(orientation, ultrasonic, gyroscope, robot);
        try {
            //init providers
            gyroscope.init(robot);
            while (!isStarted()){
                //cyclically calibrate
                telemetry.addData("Calibrated", gyroscope.isCalibrated()?"Y":"N");
                telemetry.update();
                idle();
            }
            //reorient
            lastTime = System.nanoTime();
            run();
        }
        catch (Throwable t){
            RobotLog.e(t.getMessage());
            telemetry.addData("ERROR", t.getMessage());
            telemetry.update();
            t.printStackTrace();
        }
        finally {
            //stop sensor providers
            threadManager.shutdownNow();
            threadManager.awaitTermination(100, TimeUnit.MILLISECONDS);
        }
    }

    public long getAutonamousDuriation(){
        return System.nanoTime()-lastTime;
    }

    protected abstract void run() throws InterruptedException;
}