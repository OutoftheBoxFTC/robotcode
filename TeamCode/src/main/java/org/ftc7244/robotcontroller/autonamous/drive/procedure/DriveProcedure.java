package org.ftc7244.robotcontroller.autonamous.drive.procedure;


import org.ftc7244.robotcontroller.autonamous.control.PIDControl;
import org.ftc7244.robotcontroller.autonamous.control.ControlSystem;
import org.ftc7244.robotcontroller.autonamous.drive.orientation.Orientation;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.DriveTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.RangeTerminator;
import org.ftc7244.robotcontroller.autonamous.drive.procedure.terminator.SensitivityTerminator;

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

    private DriveProcedure(double x, double y, double speed, Orientation orientation, DriveTerminator rotationalTerminator, DriveTerminator translationalTerminator, ControlSystem controlSystem){
        double o = orientation.getY()-y,
                a = orientation.getX()-x,
                h = Math.sqrt(a*a+o*o),
                s = o/h,
                r = Math.asin(s);
        rotationTarget = a<0?2*Math.PI-r:r;
        distanceTarget = h;
        this.speed = speed;
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

        public DriveProcedureBuilder(Orientation orientation, double x, double y){
            this.orientation = orientation;
            rotationalTerminator = new SensitivityTerminator(Math.toRadians(1), 2);
            translationalTerminator = new RangeTerminator(Double.NEGATIVE_INFINITY, 0);
            speed = 1;
            this.x = x;
            this.y = y;
            //TODO determine PID constants
            controlSystem = new PIDControl(0, 0, 0, true);
        }

        public DriveProcedureBuilder setControlSystem(ControlSystem controlSystem) {
            this.controlSystem = controlSystem;
            return this;
        }

        public DriveProcedure getDriveProcedure() {
            return new DriveProcedure(x, y, speed, orientation, rotationalTerminator, translationalTerminator, controlSystem);
        }

        public DriveProcedureBuilder setSpeed(double speed) {
            this.speed = speed;
            return this;
        }
    }
}
