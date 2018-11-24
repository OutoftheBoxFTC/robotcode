package org.ftc7244.robotcontroller.opmodes.autonamous.dead_reckoning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.RobotLog;

import org.ftc7244.robotcontroller.autonamous.control.PIDControl;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.RangeTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.TimeTerminator;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;
import org.ftc7244.robotcontroller.sensor.gyroscope.RevIMUProvider;
import org.ftc7244.robotcontroller.sensor.ultrasonic.UltrasonicSystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class DeadReckoningBase extends LinearOpMode {
    private PIDControl control;

    private GyroscopeProvider gyro;
    protected UltrasonicSystem ultrasonic;
    protected Robot robot;
    private ExecutorService threadManager;

    @Override
    public void runOpMode() throws InterruptedException {
        gyro = new RevIMUProvider();

        robot = new Robot(this);
        robot.init();
        robot.getLeftDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getRightDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.initServos();

        ultrasonic = new UltrasonicSystem(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), robot.getLeadingRightUS(), robot.getTrailingRightUS());
        threadManager = Executors.newCachedThreadPool();

        control = new PIDControl(Math.toRadians(15), true, "default_pid", hardwareMap.appContext);

        try {
            //init providers
            gyro.init(robot);
            while (!isStarted()){
                //cyclically calibrate
                telemetry.addData("Calibrated", gyro.isCalibrated()?"Y":"N");
                telemetry.update();
                idle();
            }
            //reorient
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

    public void rotateGyro(double rotation){
        rotation = Math.toRadians(rotation);
        ConditionalTerminator terminator = new ConditionalTerminator(new SensitivityTerminator(Math.toRadians(0.5), 100), new TimeTerminator((long) 1.5e9));
        double gyroOffset = gyro.getRotation(GyroscopeProvider.Axis.YAW),
        error = getRotationalError(rotation, gyro.getRotation(GyroscopeProvider.Axis.YAW)-gyroOffset);

        while (opModeIsActive()&&!terminator.shouldTerminate(error)){
            error = getRotationalError(rotation, gyro.getRotation(GyroscopeProvider.Axis.YAW)-gyroOffset);
            double correction = control.correction(error);
            robot.drive(correction, -correction);
        }
    }

    public void rotateGyro(double rotation, double p, double i, double d){
        rotation = Math.toRadians(rotation);
        PIDControl control = new PIDControl(p, i, d, Math.toRadians(15), true);
        ConditionalTerminator terminator = new ConditionalTerminator(new SensitivityTerminator(Math.toRadians(0.5), 100), new TimeTerminator((long) 1.5e9));
        double gyroOffset = gyro.getRotation(GyroscopeProvider.Axis.YAW),
                error = getRotationalError(rotation, gyro.getRotation(GyroscopeProvider.Axis.YAW)-gyroOffset);

        while (opModeIsActive()&&!terminator.shouldTerminate(error)){
            error = getRotationalError(rotation, gyro.getRotation(GyroscopeProvider.Axis.YAW)-gyroOffset);
            double correction = control.correction(error);
            robot.drive(correction, -correction);
        }
        robot.drive(0, 0);
    }

    public void parralelize(UltrasonicSensor us1, UltrasonicSensor us2, double distance, double p, double i, double d){
        double error = getRotationalError(0, -getError(us1, us2, distance));
        PIDControl control = new PIDControl(p, i, d, Math.toRadians(15), true);
        ConditionalTerminator terminator = new ConditionalTerminator(new SensitivityTerminator(Math.toRadians(0.5), 100), new TimeTerminator(3000));
        while (true){
            double correction = control.correction(error);
            //robot.drive(correction, -correction);
            telemetry.addData("error", error);
            telemetry.update();
            error = getRotationalError(0, -getError(us1, us2, distance));
        }
        //robot.drive(0, 0);

    }

    private double getError(UltrasonicSensor us1, UltrasonicSensor us2, double distance){
        return Math.atan((us1.getUltrasonicLevel()-us2.getUltrasonicLevel())/distance);
    }

    public void drive(double inches, double speed){
        RangeTerminator terminator = new RangeTerminator(-robot.getCountsPerInch(), robot.getCountsPerInch());
        speed *= -1;
        double direction = speed<0?-1:1,
                encoderOffset = robot.getDriveEncoderAverage(),
                distanceTarget = inches*robot.getCountsPerInch()*direction,
                distanceError = -(distanceTarget-(robot.getDriveEncoderAverage()-encoderOffset)),
                gyroOffset = gyro.getRotation(GyroscopeProvider.Axis.YAW),
                rotationError = getRotationalError(0, gyro.getRotation(GyroscopeProvider.Axis.YAW)-gyroOffset);

        while (opModeIsActive() && !terminator.shouldTerminate(distanceError)){
            double correction = control.correction(rotationError);
            robot.drive(speed+correction, speed-correction);
            telemetry.addData("rotational error", rotationError);
            telemetry.addData("distance error", distanceError);
            telemetry.update();
            rotationError = getRotationalError(0, gyro.getRotation(GyroscopeProvider.Axis.YAW)-gyroOffset);
            distanceError = -(distanceTarget-(robot.getDriveEncoderAverage()-encoderOffset));
        }
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