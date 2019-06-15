package org.ftc7244.robotcontroller.autonamous.terminator;

public class TimeTerminator extends DriveTerminator {
    private long lastTime;
    private long duration;
    public TimeTerminator(long duration){
        lastTime = -1;
        this.duration = duration;
    }
    @Override
    public boolean shouldTerminate(double error) {
        if(lastTime==-1){
            lastTime = System.nanoTime();
        }
        return System.nanoTime() - lastTime > duration;
    }
}
