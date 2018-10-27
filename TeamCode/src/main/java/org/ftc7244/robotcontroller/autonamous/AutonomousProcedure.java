package org.ftc7244.robotcontroller.autonamous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.vuforia.CameraSystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AutonomousProcedure extends LinearOpMode {

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
            vuforia.init(robot);

            while (!isStarted()){
                //send sensor calibration updates
                idle();
            }
            //reorient
            vuforia.run(threadManager);
            run();
        }
        catch (Throwable t){
            t.printStackTrace();
            RobotLog.e(t.getMessage());
        }
        finally {
            //stop sensor providers
            threadManager.shutdownNow();
            threadManager.awaitTermination(100, TimeUnit.MILLISECONDS);
        }
    }

    protected abstract void run();
}