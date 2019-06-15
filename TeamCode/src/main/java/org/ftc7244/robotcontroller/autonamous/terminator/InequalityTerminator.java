package org.ftc7244.robotcontroller.autonamous.terminator;

public class InequalityTerminator extends DriveTerminator {
    private int polarity;
    public InequalityTerminator(){
        polarity = 0;
    }
    @Override
    public boolean shouldTerminate(double error) {
        polarity = polarity==0?polarity:polarity>0?1:-1;
        if(polarity == 0){
            if(error > 0){
                polarity = 1;
            }else{
                polarity = -1;
            }
        }
        return polarity * error < 0;
    }
}
