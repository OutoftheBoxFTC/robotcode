package org.ftc7244.robotcontroller.autonamous.drive;


import org.ftc7244.robotcontroller.autonamous.control.RotationalControlSystem;
import org.ftc7244.robotcontroller.autonamous.drive.terminator.DriveProcedureTerminator;

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

    private DriveProcedureTerminator rotationalTerminator, translationalTerminator;

    private RotationalControlSystem controlSystem;

    public DriveProcedure(double x, double y, double speed, Orientation orientation, DriveProcedureTerminator rotationalTerminator, DriveProcedureTerminator translationalTerminator, RotationalControlSystem controlSystem){
        double o = orientation.getY()-y,
                a = orientation.getX()-x,
                h = Math.sqrt(a*a+o*o),
                s = o/h,
                r = Math.asin(s);
        rotationTarget = a<0?2*Math.PI-r:r;
        distanceTarget = s;
        this.speed = speed;
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
        return speed;
    }

    public DriveProcedureTerminator getRotationalTerminator() {
        return rotationalTerminator;
    }

    public DriveProcedureTerminator getTranslationalTerminator() {
        return translationalTerminator;
    }

    public RotationalControlSystem getControlSystem() {
        return controlSystem;
    }
}
