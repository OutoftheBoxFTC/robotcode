package org.ftc7244.robotcontroller.autonamous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.vuforia.CameraSystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AutonamousProcedure extends LinearOpMode {

    protected Robot robot;
    protected CameraSystem vuforia;
    private ExecutorService threadManager;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(this);
        robot.init();
        robot.initServos();
        threadManager = Executors.newCachedThreadPool();
        vuforia = new CameraSystem(robot);
        try {
            //init providers
            vuforia.init();
            while (!isStarted()){
                //send sensor calibration updates
                idle();
            }
            //reorient
            vuforia.run(threadManager);
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

    protected abstract void run();
}