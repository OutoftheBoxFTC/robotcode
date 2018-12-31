package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class blinkingTests extends OpMode {
    RevBlinkinLedDriver revBlinkinLedDriver;
    int increment = 0;
    long timer = 0;
    @Override
    public void init() {
        revBlinkinLedDriver = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        revBlinkinLedDriver.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void loop() {
        if(increment == 0) {
            revBlinkinLedDriver.setPattern(BlinkinPattern.CP1_2_SPARKLE_1_ON_2);
        }else if(increment == 1){
            revBlinkinLedDriver.setPattern(BlinkinPattern.CP1_2_BEATS_PER_MINUTE);
        }else if(increment == 2){
            revBlinkinLedDriver.setPattern(BlinkinPattern.CP1_2_COLOR_WAVES);
        }else if(increment == 3){
            revBlinkinLedDriver.setPattern(BlinkinPattern.CP1_2_SINELON);
        }
        if(timer == 0){
            timer = System.currentTimeMillis() + 10000;
        }
        if(timer <= System.currentTimeMillis()){
            increment ++;
            timer = 0;
        }
        if(increment > 3){
            increment = 0;
        }
    }
}
