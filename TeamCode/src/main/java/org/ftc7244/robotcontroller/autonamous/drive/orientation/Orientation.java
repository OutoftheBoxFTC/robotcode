package org.ftc7244.robotcontroller.autonamous.drive.orientation;

/**
 * "Only a sith deals in absolutes" - Obiwan Kenobi
 *
 * This class will hold information pertaining to the best known absolute orientation of the center of the robot at any given point in time.
 *
 * X: Positive towards red allience side
 * Y: Positive Towards back end of field (From audience perspective)
 * R: Positive Counterclockwise from above. Constrained to the interval [0, 2pi)
 *
 * From the audience's perspective, The red alliance is on the right side
 *
 * Translation units are in inches, and rotation units are in radians
 *
 * X and Y axes are 0 at field center
 * Rotation axis is 0 when terminal side is parallel with back wall and facing towards red
 *
 * We are sith
 */
public class Orientation {

    private double x, y, r;

    public Orientation(double x, double y, double r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setR(double r){
        this.r = r;
    }
}