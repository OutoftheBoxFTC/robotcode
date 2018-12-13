package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import net.frogbots.opencv_webcam.OpenCvWebcam;
import net.frogbots.opencv_webcam.OpenCvWebcamCallback;

import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.CameraMode;
import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.FrameFormat;
import org.ftc7244.robotcontroller.hardware.Robot;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

@TeleOp(name = "Webcam Test")
public class WebcamTest extends LinearOpMode implements OpenCvWebcamCallback{
    private OpenCvWebcam webcam;
    private Robot robot;

    @Override
    public void runOpMode() {
        robot = new Robot(this);
        robot.init();
        int cameraMonitorViewID = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = new OpenCvWebcam(robot.getW1(), cameraMonitorViewID);
        webcam.openCameraDevice();

        waitForStart();
        CameraMode streamingMode = new CameraMode(320, 240, 30, FrameFormat.YUYV);
        webcam.startStreaming(streamingMode, this);
        while (opModeIsActive()){
            telemetry.addData("FPS", webcam.getFps());
            webcam.showViewport();

            telemetry.update();
        }
        webcam.stopStreaming();
        webcam.closeCamera();
    }

    @Override
    public Mat onFrameAvailableForUserPipeline(Mat mat) {
        Imgproc.rectangle(mat, new Point(mat.cols()/4, mat.rows()/4), new Point((mat.cols()/4)*3, (mat.rows()/4)*3), new Scalar(0, 255, 0), 4);
        return mat;
    }
}
