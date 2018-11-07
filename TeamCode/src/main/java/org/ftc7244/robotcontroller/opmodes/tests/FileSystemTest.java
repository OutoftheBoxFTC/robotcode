package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.ftc7244.robotcontroller.opmodes.tuning.FileSystem;

@TeleOp(name = "File System Test")
public class FileSystemTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Result", FileSystem.loadFromFile("test", hardwareMap.appContext));
        telemetry.update();
        FileSystem.saveToFile("test", "test", hardwareMap.appContext);
        waitForStart();
    }
}
