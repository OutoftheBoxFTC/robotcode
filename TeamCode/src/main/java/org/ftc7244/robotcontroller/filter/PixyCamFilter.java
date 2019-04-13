package org.ftc7244.robotcontroller.filter;

import org.ftc7244.robotcontroller.sensor.pixycam.Pixycam2Provider;


public class PixyCamFilter {
    private static final int WIDTH_THRESHOLD = 290;
    private static final double NORMALIZED_THRESHOLD = 0.1;
    private final int arrayLength;
    private int counter;
    private IntakeState[] data;
    public PixyCamFilter(int length){
        this.arrayLength = length;
        reset();
    }

    public void update(Pixycam2Provider silver, Pixycam2Provider gold){
        data[counter] = silver.getWidth()>WIDTH_THRESHOLD||gold.getWidth()>WIDTH_THRESHOLD ||
                (gold.getWidth()>1&&silver.getWidth()>1)?IntakeState.FULL:
                gold.getWidth()>1||silver.getWidth()>1?IntakeState.HALF:
                        IntakeState.NONE;
        counter = (counter+1)%arrayLength;
    }

    public IntakeState evaluate(){
        double full=0, half=0;
        for (int i = 0; i < arrayLength; i++) {
            switch (data[i]){
                case HALF:
                    half++;
                    break;
                case FULL:
                    full++;
                    break;
            }
        }
        double normalizedHalf = half/arrayLength;
        if(normalizedHalf>NORMALIZED_THRESHOLD)
            return IntakeState.HALF;
        else if(full>0)
            return IntakeState.FULL;
        return IntakeState.NONE;
    }

    public void reset(){
        data = new IntakeState[arrayLength];
        counter = 0;
    }

    public enum IntakeState{
        NONE,
        HALF,
        FULL
    }
}
