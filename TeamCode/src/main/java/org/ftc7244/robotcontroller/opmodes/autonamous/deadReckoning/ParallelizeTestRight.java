package org.ftc7244.robotcontroller.opmodes.autonamous.deadReckoning;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

import org.ftc7244.robotcontroller.autonamous.control.PIDControl;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.TimeTerminator;

@Autonomous(name = "Right Parallelize Test")
public class ParallelizeTestRight extends DeadReckoningBase {

    public ParallelizeTestRight() {
        super(false);
    }

    @Override
    protected void run() {
        double distance = 13.25;
        UltrasonicSensor us1 = robot.getLeadingRightUS(), us2 = robot.getTrailingRightUS();

        double error = getRotationalError(0, -getError(us2, us1, distance));
        PIDControl control = new PIDControl(0.8, 0.0000000025, 19000000, Math.toRadians(15), true);
        ConditionalTerminator terminator = new ConditionalTerminator(new SensitivityTerminator(Math.toRadians(0.3), 100));
        while (opModeIsActive()&&!terminator.shouldTerminate(error)){
            double correction = control.correction(error);
            //robot.drive(correction, -correction);
            telemetry.addData("us1", us1.getUltrasonicLevel());
            telemetry.addData("us2", us2.getUltrasonicLevel());
            telemetry.addData("error", error);
            telemetry.update();
            error = getRotationalError(0, -getError(us1, us2, distance));
        }
        //robot.drive(0, 0);
    }
}