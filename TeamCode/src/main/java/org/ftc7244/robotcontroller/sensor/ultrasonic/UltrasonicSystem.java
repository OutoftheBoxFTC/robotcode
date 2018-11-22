package org.ftc7244.robotcontroller.sensor.ultrasonic;

public class UltrasonicSystem {
    private UltrasonicSide left, right;
    private Wall wall;

    public UltrasonicSystem(SickUltrasonic leftLeading, SickUltrasonic leftTrailing, SickUltrasonic rightLeading, SickUltrasonic rightTrailing){
        left = new UltrasonicSide(rightLeading, rightTrailing, Side.RIGHT.distance);
        right = new UltrasonicSide(leftTrailing, leftLeading, Side.LEFT.distance);
        wall = null;

    }
    public void start(SickUltrasonic.Mode mode){
        left.setMode(mode);
        right.setMode(mode);
    }

    /**
     *
     * @param side the side of the robot being referenced
     * @return the rotational offset from the wall detected by that side
     */
    public double getRotationFrom(Side side){
        double value;
        if(side == Side.LEFT){
            value = left.getError()+side.rotation;
        }else if(side == Side.RIGHT){
            value = right.getError()+side.rotation;
        }else{
            return Double.POSITIVE_INFINITY;
        }
        if(wall != null){
            value += wall.getRotation() + side.getRotation();
        }
        return value%(Math.PI*2);
    }

    /**
     * Returns the absolute rotation
     * Rotation axis is 0 when terminal side is parallel with back wall and facing towards red
     * @return the absolute rotation given the wall being detected
     */
    public double getAbsoluteRotation(){
        double ultrasonicValue = getRotationFrom(Side.RIGHT);
        if(ultrasonicValue == Double.POSITIVE_INFINITY){
            ultrasonicValue = getRotationFrom(Side.LEFT);
            if(ultrasonicValue == Double.POSITIVE_INFINITY)
                return Double.POSITIVE_INFINITY;
        }
        return ultrasonicValue;
    }

    public double getUltrasonicAverage(Side side){
        if (side==Side.LEFT){
            return left.getAverage();
        } else if (side == Side.RIGHT){
            return right.getAverage();
        }
        return Double.POSITIVE_INFINITY;
    }

    public Wall getWall() {
        return wall;
    }

    public void setWall(Wall wall) {
        this.wall = wall;
    }

    public enum Side {
        LEFT(0, 1),
        RIGHT(Math.PI, 1);
        private double rotation, distance;

        Side(double rotation, double distance){
            this.rotation = rotation;
            this.distance = distance;
        }

        public double getRotation() {
            return rotation;
        }

        public double getDistance() {
            return distance;
        }
    }

    public enum Wall{
        BACK(0),
        RED(Math.PI/2),
        AUDIENCE(Math.PI),
        BLUE(3*Math.PI/2);

        private double rotation;
        Wall(double rotation){
            this.rotation = rotation;
        }

        public double getRotation() {
            return rotation;
        }
    }
}