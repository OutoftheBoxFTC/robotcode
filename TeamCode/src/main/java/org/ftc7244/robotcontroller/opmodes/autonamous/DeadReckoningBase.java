package org.ftc7244.robotcontroller.opmodes.autonamous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonamous.control.PIDControl;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.InequalityTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.TimeTerminator;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedRevIMUProvider;
import org.ftc7244.robotcontroller.sensor.pixycam.Pixycam2Provider;
import org.ftc7244.robotcontroller.sensor.pixycam.PixycamSample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider.Axis.PITCH;
import static org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider.Axis.YAW;

public abstract class DeadReckoningBase extends LinearOpMode {
    private PIDControl control;

    //protected GyroscopeProvider gyro;
    protected ExtendedGyroscopeProvider gyro;
    protected Robot robot;
    ExecutorService threadManager;
    private Pixycam2Provider samplePixyProvider;
    private PixycamSample pixycamSample;
    public PixycamSample.SampleTransform sample;
    private boolean hanging;
    private long startTime;
    double latchOpen = 0.05, latchClosed = 0.8;

    public DeadReckoningBase(boolean hanging){
        this.hanging = hanging;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        gyro = new ExtendedRevIMUProvider();
        robot = new Robot(this);
        robot.init();
        samplePixyProvider = new Pixycam2Provider(Pixycam2Provider.Mineral.GOLD, robot.getSampleI2c());
        robot.initServos();
        threadManager = Executors.newCachedThreadPool();

        //control = new PIDControl(Math.toRadians(15), true, "default_pid", hardwareMap.appContext);
        control = new PIDControl(0.45, 0.0000000025, 19000000, Math.toRadians(15), true);
        startTime = System.nanoTime();
        try {
            //init providers
            gyro.init(robot);
            samplePixyProvider.start();
            pixycamSample.start();
            samplePixyProvider.setLamps(true, true);
            while (!isStarted()){
                //cyclically calibrate
                //Set the sample to what the pixy sample sees
                telemetry.addData("Calibrated", gyro.isCalibrated()?"Y":"N");
                robot.logErrors();
                telemetry.update();
                sample = pixycamSample.run();
                //Keep the robot hanging
                if(gyro.isCalibrated()){
                    gyro.offsetAxisTo(YAW, 0);
                    gyro.offsetAxisTo(PITCH, 0);
                    while (!isStarted() && hanging) {
                        sample = pixycamSample.run();
                        telemetry.addData("Gyro", Math.toDegrees(gyro.getRotation(YAW)));
                        telemetry.addData("Pixy", sample);
                        telemetry.addData("X", samplePixyProvider.getX());
                        robot.logErrors();
                        telemetry.update();
                    }
                }
                idle();
            }
            run();
        }
        catch (Throwable t){
            t.printStackTrace();
            RobotLog.e(t.getMessage());
            telemetry.addData("EXECPTION THROWN", t.getMessage());
            telemetry.update();
            sleep(5000);
        }
        finally {
            //stop sensor providers and other threads
            threadManager.shutdownNow();
            threadManager.awaitTermination(100, TimeUnit.MILLISECONDS);
        }
    }

    protected abstract void run();

    public void rotateGyro(double rotation){

        rotation = Math.toRadians(rotation);
        ConditionalTerminator terminator = new ConditionalTerminator(new SensitivityTerminator(Math.toRadians(0.0872665), 100), new TimeTerminator((long) 3e9));
        double gyroOffset = gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW),
        error = getRotationalError(rotation, gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)-gyroOffset);
        double target = getRotationalError(rotation, gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)-gyroOffset);
        while (opModeIsActive()&&!terminator.shouldTerminate(error)){
            telemetry.addData("Error", Math.toDegrees(error));
            telemetry.addData("Target", Math.toDegrees(target));
            telemetry.update();
            error = getRotationalError(rotation, gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)-gyroOffset);
            double correction = control.correction(error);
            robot.drive(correction, -correction);
        }
    }

    public void rotateGyro(double rotation, double p, double i, double d, long timeout){
        rotation = Math.toRadians(rotation);
        PIDControl control = new PIDControl(p, i, d, Math.toRadians(15), true);
        ConditionalTerminator terminator = new ConditionalTerminator(new SensitivityTerminator(Math.toRadians(0.5), 100), new TimeTerminator((long) timeout));
        double gyroOffset = gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW),
                error = getRotationalError(rotation, gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)-gyroOffset);

        while (opModeIsActive()&&!terminator.shouldTerminate(error)){
            error = getRotationalError(rotation, gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)-gyroOffset);
            double correction = control.correction(error);
            robot.drive(correction, -correction);
        }
        robot.drive(0, 0);
    }

    public void drive(double inches, double speed){
        drive(inches, speed, gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW));
    }

    public void drive(double inches, double speed, double angle){
        InequalityTerminator terminator = new InequalityTerminator();
        speed *= -1;
        double direction = speed<0?-1:1,
                encoderOffset = robot.getDriveEncoderAverage(),
                distanceTarget = inches*robot.getCountsPerInch()*direction,
                distanceError = -(distanceTarget+(robot.getDriveEncoderAverage()-encoderOffset)),
                rotationError = getRotationalError(0, gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)-angle);
        while (opModeIsActive() && !terminator.shouldTerminate(distanceError)){
            double correction = control.correction(rotationError);
            robot.drive(speed+correction, speed-correction);
            telemetry.addData("rotational error", rotationError);
            telemetry.addData("distance error", distanceError);
            telemetry.update();
            rotationError = getRotationalError(0, gyro.getRotation(ExtendedGyroscopeProvider.Axis.YAW)-angle);
            distanceError = -(distanceTarget+(robot.getDriveEncoderAverage()-encoderOffset));
        }
        robot.drive(0, 0);
    }

    public double getRotationalError(double rotationTarget, double currentRotation) {
        if (rotationTarget > currentRotation) {
            if (rotationTarget - currentRotation > Math.PI) {
                return currentRotation - (rotationTarget - Math.PI * 2);
            }
            return currentRotation - rotationTarget;
        }
        if (currentRotation - rotationTarget > Math.PI) {
            return rotationTarget + Math.PI * 2 - currentRotation;
        }
        return currentRotation - rotationTarget;
    }
}