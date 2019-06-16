package org.ftc7244.robotcontroller.autonamous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.hardware.Robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class AutonomousProcedure extends LinearOpMode {

    protected Robot robot;
    private ExecutorService threadManager;

    private long lastTime;

    @Override
    public void runOpMode() throws InterruptedException {
        lastTime = 0;

        robot = new Robot(this);
        robot.init();
        robot.initServos();
        threadManager = Executors.newCachedThreadPool();
        try {
            while (!isStarted()){
                for (String error : robot.getErrors()) {
                    telemetry.addData("ERROR", error);
                }
                telemetry.update();
                idle();
            }
            lastTime = System.nanoTime();
            run();
        }
        catch (Throwable t){
            t.printStackTrace();
            RobotLog.e(t.getMessage());
        }
        finally {
            robot.disableDriveMotors();
            threadManager.shutdown();
            threadManager.awaitTermination(100, TimeUnit.MILLISECONDS);
        }
    }

    public long getAutonamousDuriation(){
        return System.nanoTime()-lastTime;
    }

    protected abstract void run() throws InterruptedException;
}