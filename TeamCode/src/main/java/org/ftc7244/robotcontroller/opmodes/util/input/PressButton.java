package org.ftc7244.robotcontroller.opmodes.util.input;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * This holds of the state of the button and if it has been pressed and released it will keep
 * its pressed state until it is pressed and released again. This acts as a toggle button.
 */
public class PressButton extends Button {

    private boolean active, pressed, previousState;

    public PressButton(Gamepad gamepad, ButtonType type, boolean pressed) {
        super(gamepad, type);
        active = false;
        this.pressed = pressed;
        previousState = false;
    }

    public PressButton(Gamepad gamepad, ButtonType type) {
        this(gamepad, type, false);
    }

    @Override
    public boolean isPressed() {
        boolean state = super.isPressed();
        //if the code does know the button has been pressed and it has been pressed
        if (!pressed && state) {
            //change the pressed status to true to wait for the button to be released
            pressed = true;
            //once the pressed status no longer matches the state the button has been released
        } else if (pressed && !state) {
            //change the active state and forget the pressed state
            active = !active;
            pressed = false;
        }
        //if nothing has changed returned the current status
        return active;
    }

    public boolean isUpdated(){
        boolean state = isPressed(), returnVal = state!=previousState;
        previousState = state;
        return returnVal;
    }

    public void release(){
        pressed = false;
        active = false;
    }
}