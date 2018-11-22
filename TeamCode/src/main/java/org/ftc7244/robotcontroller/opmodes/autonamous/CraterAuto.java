package org.ftc7244.robotcontroller.opmodes.autonamous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.autonamous.AutonomousProcedure;
import org.ftc7244.robotcontroller.autonamous.control.ConstantControl;
import org.ftc7244.robotcontroller.autonamous.control.ControlSystem;
import org.ftc7244.robotcontroller.autonamous.control.PIDControl;
import org.ftc7244.robotcontroller.autonamous.drive.orientation.Orientation;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.DriveProcedure;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.DriveTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.RangeTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.SensitivityTerminator;
import org.ftc7244.robotcontroller.sensor.gyroscope.GyroscopeProvider;

@Autonomous(name = "Crater Auto")
public class CraterAuto extends AutonomousProcedure {
    private PIDControl pid = new PIDControl(0.45, 0.0000000025, 19000000, Math.toRadians(15), true);
    @Override
    protected void run() throws InterruptedException{
        int mineral = 0;
        robot.getLeftDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.getRightDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        switch(mineral){
            case 1:
                rotate(23);
                break;
            case 2:
                rotate(-35);
                break;
            default:
                break;
        }
        robot.intake(1);
        robot.driveToInch(0.3, 50.5);
        sleep(500);
        robot.intake(0);
        robot.driveToInch(-0.3, -20.5);
        sleep(500);
        rotate(-55);
        sleep(500);
        robot.driveToInch(0.3, 60);
        sleep(500);
        rotate(-60);
        sleep(500);
        robot.driveToInch(-0.75, -60);
        //drop off team marker
        sleep(1000);
        robot.driveToInch(0.75, 84);
        robot.getLeftDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        robot.getRightDrive().setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    private void rotate(double target){
        double offset = gyroscope.getRotation(GyroscopeProvider.Axis.YAW);

        target = Math.toRadians(target);
        double error = gyroscope.getRotation(GyroscopeProvider.Axis.YAW)-offset+target;
        double correction;
        while(opModeIsActive() && Math.abs(error) > (Math.PI / 360)) {
            error = gyroscope.getRotation(GyroscopeProvider.Axis.YAW)-offset+target;
            if(error < -6)
                error = error + 2*Math.PI;
            telemetry.addData("error ",error);
            telemetry.update();
            correction = pid.correction(error);
            robot.drive(correction, -correction);
        }
    }
}
