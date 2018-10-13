package org.ftc7244.robotcontroller.sensor.ultrasonic;

public class UltrasonicSystem {

    private static final double US_DISTANCE_LEFT = 1, US_DISTANCE_RIGHT = 1;

    private UltrasonicSide[] sides;
    public UltrasonicSystem(SickUltrasonic leftLeading, SickUltrasonic leftTrailing, SickUltrasonic rightLeading, SickUltrasonic rightTrailing){
        sides = new UltrasonicSide[]{
                new UltrasonicSide(rightLeading, rightTrailing, US_DISTANCE_RIGHT),
                new UltrasonicSide(leftTrailing, leftLeading, US_DISTANCE_LEFT)
        };

    }
    public void start(SickUltrasonic.Mode mode){
        for(UltrasonicSide side : sides){
            side.setMode(mode);
        }
    }
    public double getError(Side side){
        return side == null ? 0 : sides[side.index].getError();
    }

    public enum Side {
        LEFT(1),
        RIGHT(0);
        private int index;
        Side(int index){
            this.index = index;
        }
    }
}