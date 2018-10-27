package org.ftc7244.robotcontroller.sensor.ultrasonic;

public class UltrasonicSystem {
    SickUltrasonic leftFront, leftBack, rightFront, rightBack;
    public UltrasonicSystem(SickUltrasonic frontLeftSensor, SickUltrasonic backLeftSensor, SickUltrasonic frontRightSensor, SickUltrasonic backRightSensor){
        leftFront = frontLeftSensor;
        leftBack = backLeftSensor;
        rightFront = frontRightSensor;
        rightBack = backRightSensor;

    }
    public void start(SickUltrasonic.Mode mode){
        leftFront.setMode(mode);
        leftBack.setMode(mode);
        rightBack.setMode(mode);
        rightFront.setMode(mode);
    }
    //TODO tan inv this. This is going to be interpreted as the literal degree offset from the wall. We only justified an approximation in proportionality, not value
    public double getError(UltrasonicSide side){
        if(side == UltrasonicSide.LEFT){
            return leftFront.getUltrasonicLevel() - leftBack.getUltrasonicLevel();
        }else if(side == UltrasonicSide.RIGHT){
            return rightFront.getUltrasonicLevel() - rightBack.getUltrasonicLevel();
        }else{
            return -1;
        }
    }

    public enum UltrasonicSide{
        LEFT(0),
        RIGHT(Math.PI);
        private double rotation;

        UltrasonicSide(double rotation){
            this.rotation = rotation;
        }

        public double getRotation() {
            return rotation;
        }
    }
}