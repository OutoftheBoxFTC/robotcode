package org.ftc7244.robotcontroller.TeleOp;

public class StateMachine {
    private final State[] states;

    private long start;
    private int state;

    long wait = 0;

    public StateMachine(State... states){
        this.states = states;
    }

    public void update(){
        if(wait > 0){
            if(System.currentTimeMillis()-start>=wait){
                wait = 0;
                advance();
            }
        }
        else if(state>=0&&state<states.length) {
            states[state].update();
        }
    }

    public void advance(){
        state++;
        if(state >= states.length){
            state = -1;
        }
        start = System.currentTimeMillis();
    }

    public void advance(long wait){
        this.wait = wait;
        start = System.currentTimeMillis();
    }



    public long getElapsedTime() {
        return System.currentTimeMillis()- start;
    }

    public boolean isRunning(){
        return start >= 0 && state < states.length;
    }
}