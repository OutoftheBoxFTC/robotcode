package org.ftc7244.robotcontroller.opmodes.autonamous.dead_reckoning;

public class Crater extends DeadReckoningBase {

    @Override
    protected void run() {
        int pixyCase = 1;
        double rotation = 0;
        switch (pixyCase){
            case 0:
                rotation = -23;
                break;
            case 2:
                rotation = 23;
                break;
        }
        rotateGyro(rotation);
        robot.intake(-1);
        drive(24, 0.5);
        sleep(200);
        robot.intake(0);
        drive(24, -0.5);
        rotateGyro(45-rotation);
        drive(48, 0.5);
        rotateGyro(-90);
        parralelize(robot.getLeadingLeftUS(), robot.getTrailingLeftUS(), 13.25, 0.8, 0.0000000025, 19000000);
        drive(90, -0.5);
        drive(130, 0.5);
    }
}
