package org.ftc7244.robotcontroller.autonamous.drive.procedure;


import android.content.Context;

import org.ftc7244.robotcontroller.autonamous.control.ControlSystem;
import org.ftc7244.robotcontroller.autonamous.control.PIDControl;
import org.ftc7244.robotcontroller.autonamous.drive.orientation.Orientation;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.ConditionalTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.DriveTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.RangeTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.SensitivityTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.TimeTerminator;

/**
 * PLANS FOR THIS CLASS
 *
 * Provide a changing rotation target based off a function and the arc length theorum
 *
 * Provide a target speed for travel dependant on our current rotation, our maximum rate of rotation, and the target rotation depicted
 * by the function and our current distance down the arc.
 *
 * As a consequence, linear movement and rotation will become a single motion
    * The power offset will be 0 (or very close to it) because it will detect that the maximum rotation rate will not be able to compensate
    * for any power offset at all until it reaches approximately to its desired rotation
 */
public class DriveProcedure {

    /**
     * rotationTarget is an absolute, unbounded measure of rotation.
     *
     * positive rotationDirection is counterclockwise from above
     */

    private double rotationTarget, distanceTarget, speed;

    private DriveTerminator rotationalTerminator, translationalTerminator;

    private ControlSystem controlSystem;

    private DriveProcedure(double x, double y, double speed, Direction direction, Orientation orientation, DriveTerminator rotationalTerminator, DriveTerminator translationalTerminator, ControlSystem controlSystem){
        double o = y-orientation.getY(),
                a = x-orientation.getX(),
                h = Math.sqrt(a*a+o*o),
                s = o/h,
                r = Math.asin(s);

        r=(a<0?(Math.PI-r):r)%(Math.PI*2);
        r = (r+direction.angle);
        r = r<0?(r+Math.PI*2):r>Math.PI*2?(r-Math.PI*2):r;
        rotationTarget = r;
        distanceTarget = h*direction.direction;
        this.speed = speed*direction.direction;
        this.rotationalTerminator = rotationalTerminator;
        this.translationalTerminator = translationalTerminator;
        this.controlSystem = controlSystem;
    }

    public DriveProcedure(double r, double d, double speed, DriveTerminator rotationalTerminator, DriveTerminator translationalTerminator, ControlSystem controlSystem, Orientation orientation){
        this.speed = speed;
        this.distanceTarget = d;
        this.rotationTarget = r+orientation.getR();
        this.rotationalTerminator = rotationalTerminator;
        this.translationalTerminator = translationalTerminator;
        this.controlSystem = controlSystem;
    }

    public double getRotationTarget() {
        return rotationTarget;
    }

    public double getDistanceTarget() {
        return distanceTarget;
    }

    public double getSpeed() {
        return -speed;
    }

    public DriveTerminator getRotationalTerminator() {
        return rotationalTerminator;
    }

    public DriveTerminator getTranslationalTerminator() {
        return translationalTerminator;
    }

    public ControlSystem getControlSystem() {
        return controlSystem;
    }

    public static class DriveProcedureBuilder{
        private double x , y, speed;

        private Orientation orientation;
        private DriveTerminator rotationalTerminator, translationalTerminator;
        private ControlSystem controlSystem;
        private Direction direction;

        public DriveProcedureBuilder(Orientation orientation, double x, double y, Context context){
            this.orientation = orientation;
            rotationalTerminator = new ConditionalTerminator(new TimeTerminator((long) 15e8), new SensitivityTerminator(Math.toRadians(0.5), 100));
            translationalTerminator = new RangeTerminator(-1, 1);
            speed = 1;
            this.x = x;
            this.y = y;
            direction = Direction.FOREWORD;
            //TODO determine PID constants

            controlSystem = new PIDControl(Math.toRadians(15), true, "default_pid", context);
        }

        public DriveProcedureBuilder setControlSystem(ControlSystem controlSystem) {
            this.controlSystem = controlSystem;
            return this;
        }

        public DriveProcedure getDriveProcedure() {
            return new DriveProcedure(x, y, speed, direction, orientation, rotationalTerminator, translationalTerminator, controlSystem);
        }

        public DriveProcedureBuilder setSpeed(double speed) {
            this.speed = speed;
            return this;
        }

        public DriveProcedureBuilder setDirection(Direction direction) {
            this.direction = direction;
            return this;
        }
    }

    public enum Direction{
        FOREWORD(1, 0),
        REVERSE(-1, Math.PI);
        private int direction;
        private double angle;
        Direction(int direction, double angle) {
            this.direction = direction;
            this.angle = angle;
        }

        public double getAngle() {
            return angle;
        }

        public int getDirection() {
            return direction;
        }
    }
}
