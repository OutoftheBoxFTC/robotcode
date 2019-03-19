package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
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
import org.ftc7244.robotcontroller.sensor.ultrasonic.UltrasonicSystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider.Axis.PITCH;
import static org.ftc7244.robotcontroller.sensor.gyroscope.ExtendedGyroProvider.ExtendedGyroscopeProvider.Axis.YAW;

public abstract class DeadReckoningBase extends LinearOpMode {
    private PIDControl control;

    //protected GyroscopeProvider gyro;
    protected ExtendedGyroscopeProvider gyro;
    protected UltrasonicSystem ultrasonic;
    protected Robot robot;
    ExecutorService threadManager;
    private Pixycam2Provider samplePixyProvider;
    private PixycamSample pixycamSample;
    public PixycamSample.SampleTransform sample;
    private boolean gyroCalibrated, hanging;
    private AtomicBoolean armIsReset;
    private long startTime;
    //Servo on port 3
    public DeadReckoningBase(boolean hanging){
        this.hanging = hanging;
    }
    private Runnable armReset, intakeSample;
    @Override
    public void runOpMode() throws InterruptedException {
        armReset = () -> {
            armIsReset = new AtomicBoolean(false);
            double timer;
            robot.moveArm(1);
            while (opModeIsActive() && robot.getArmSwitch().getState()){}
            robot.moveArm(-0.5);
            while (opModeIsActive() && !robot.getArmSwitch().getState()){}
            timer = 100 + System.currentTimeMillis();
            while (opModeIsActive() && timer > System.currentTimeMillis()){}
            robot.moveArm(0.05);
            while (opModeIsActive() && robot.getArmSwitch().getState()){}
            robot.moveArm(0);
            armIsReset.set(true);
        };
        intakeSample = () -> {
            //double timer = -1;
            robot.intake(1);
            /*while((robot.getIntake().getPower() > 0.5 || robot.getIntake().getPower() < -0.5) && opModeIsActive()){
                if(robot.getIntake().getVelocity(AngleUnit.RADIANS) < 10 && timer == -1){
                    timer = System.currentTimeMillis() + 1000;
                }else if (robot.getIntake().getVelocity(AngleUnit.RADIANS) > 10){
                    timer = -1;
                }
                if(timer < System.currentTimeMillis() && timer != -1){
                    robot.intake(-1);
                }
            }
        */};
        gyro = new ExtendedRevIMUProvider();
        robot = new Robot(this);
        robot.init();
        samplePixyProvider = new Pixycam2Provider(Pixycam2Provider.Mineral.GOLD, robot.getSampleI2c());
        pixycamSample = new PixycamSample(samplePixyProvider);
        robot.getLeftDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getRightDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getLeftDrive2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getRightDrive2().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.initServos();
        robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.BLACK);
        ultrasonic = new UltrasonicSystem(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), robot.getLeadingRightUS(), robot.getTrailingRightUS());
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
                        robot.moveArm(0.31);
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
            //reorient
            robot.getRaisingArm1().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            if(hanging)
                unhang();
            robot.getSidePanelBlinkin().setPattern(RevBlinkinLedDriver.BlinkinPattern.CP2_SHOT);
            run();
            if(armIsReset != null){
                while (!armIsReset.get());
            }
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

    public void parralelize(UltrasonicSensor us1, UltrasonicSensor us2, double distance, double p, double i, double d){

        double error = getRotationalError(0, -getError(us2, us1, distance));
        PIDControl control = new PIDControl(p, i, d, Math.toRadians(15), true);
        ConditionalTerminator terminator = new ConditionalTerminator(new SensitivityTerminator(Math.toRadians(0.3), 100), new TimeTerminator((long) 3e9));
        while (opModeIsActive()&&!terminator.shouldTerminate(error)){
            double correction = control.correction(error);
            robot.drive(correction, -correction);
            telemetry.addData("error", error);
            telemetry.update();
            error = getRotationalError(0, -getError(us1, us2, distance));
        }
        robot.drive(0, 0);

    }

    public void parralelize(UltrasonicSensor us1, UltrasonicSensor us2, double distance, double p, double i, double d, double offset){

        double error = getRotationalError(offset, -getError(us2, us1, distance));
        PIDControl control = new PIDControl(p, i, d, Math.toRadians(15), true);
        ConditionalTerminator terminator = new ConditionalTerminator(new SensitivityTerminator(Math.toRadians(0.3), 100), new TimeTerminator((long) 3e9));
        while (opModeIsActive()&&!terminator.shouldTerminate(error)){
            double correction = control.correction(error);
            robot.drive(correction, -correction);
            telemetry.addData("error", error);
            telemetry.update();
            error = getRotationalError(offset, -getError(us1, us2, distance));
        }
        robot.drive(0, 0);

    }
    double angle = 0, us1, us2;

    public void orientGyroToWall(double wallAngle, UltrasonicSensor us1, UltrasonicSensor us2){
        this.us1 = us1.getUltrasonicLevel();
        this.us2 = us2.getUltrasonicLevel();
        double angle = wallAngle + Math.atan((this.us1-this.us2)/13.25);
        this.angle = angle;
        gyro.offsetAxisTo(ExtendedGyroscopeProvider.Axis.YAW, angle);
    }

    public double getError(UltrasonicSensor us1, UltrasonicSensor us2, double distance){
        double d1 = us1.getUltrasonicLevel(), d2 = us2.getUltrasonicLevel();

        double angle = Math.atan((d1-d2)/distance);

        if(angle < Math.toRadians(-15)){
            d2 = 0;
        } else if(angle > Math.toRadians(15)){
            d1 = 0;
        }
        angle = Math.atan((d1-d2)/distance);
        return angle;
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
            telemetry.addData("Gyro", gyro.getRotation(YAW));
            telemetry.addData("Angle", angle);
            telemetry.addData("us1", us1);
            telemetry.addData("us2", us2);
            double correction = control.correction(rotationError);
            robot.drive(speed+correction, speed-correction);
            //robot.drive(speed+correction, speed-correction);
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

    public void unhang(){
        robot.getLeftDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getRightDrive().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.moveArm(0.2);
        robot.drive(0.15, 0.15);
        while(opModeIsActive() && Math.abs(gyro.getRotation(PITCH)) > Math.toRadians(5)){
            robot.getLeftDrive().setPower(-0.15);
            robot.getRightDrive().setPower(-0.15);
            robot.getLeftDrive2().setPower(robot.getLeftDrive().getPower());
            robot.getRightDrive2().setPower(robot.getRightDrive().getPower());
        }
        robot.drive(0, 0);
        robot.moveArm(0);
        samplePixyProvider.setLamps(false, false);
        sleep(250);
        robot.getLatch().setPosition(0.7);
        sleep(1500);
        robot.moveArm(1);
        sleep(1000);
        robot.getLatch().setPosition(0.2);
        robot.moveArm(0);
        robot.getLeftDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.getRightDrive().setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        sleep(750);
        //drive(2, 0.5);
    }

    public Runnable getArmReset() {
        return armReset;
    }

    public Runnable getIntakeSample(){
        return intakeSample;
    }

    public void dumpArm(){
        samplePixyProvider.setLamps(true, true);
        robot.moveArm(-1);
        sleep(1200);
        robot.moveArm(0);
        sleep(200);
        robot.getLid().setPosition(.8);
        sleep(500);
        robot.getLid().setPosition(.4);
        sleep(250);
        threadManager.submit(armReset);
        if(armIsReset != null){
            while (!armIsReset.get() && opModeIsActive());
        }
        samplePixyProvider.setLamps(false, false);
    }

    public void burnJeClamelRetrograde(){
        robot.getJeClamelBurner().setPosition(0.2);
    }
}