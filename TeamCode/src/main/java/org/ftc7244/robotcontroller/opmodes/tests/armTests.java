package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.ftc7244.robotcontroller.hardware.Robot;
@TeleOp
public class armTests extends LinearOpMode {
    Robot robot = new Robot(this);
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init();
        waitForStart();
        robot.getRaisingArm1().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.getRaisingArm2().setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.getRaisingArm1().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.getRaisingArm2().setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while(opModeIsActive()){
            telemetry.addData("Arm", robot.getRaisingArm1().getCurrentPosition());
            telemetry.addData("Arm1", robot.getRaisingArm2().getCurrentPosition());
            telemetry.update();
        }
    }
}
