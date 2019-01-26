package org.ftc7244.robotcontroller.sensor.vuforia;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.SwitchableCamera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.CameraControl;

public class test implements SwitchableCamera {
    @Override
    public void setActiveCamera(CameraName cameraName) {

    }

    @Override
    public CameraName getActiveCamera() {
        return null;
    }

    @Override
    public CameraName[] getMembers() {
        return new CameraName[0];
    }

    @NonNull
    @Override
    public CameraName getCameraName() {
        return null;
    }

    @Override
    public CameraCaptureRequest createCaptureRequest(int androidFormat, Size size, int fps) throws CameraException {
        return null;
    }

    @NonNull
    @Override
    public CameraCaptureSession createCaptureSession(Continuation<? extends CameraCaptureSession.StateCallback> continuation) throws CameraException {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public Camera dup() {
        return null;
    }

    @Nullable
    @Override
    public <T extends CameraControl> T getControl(Class<T> controlType) {
        return null;
    }
}
