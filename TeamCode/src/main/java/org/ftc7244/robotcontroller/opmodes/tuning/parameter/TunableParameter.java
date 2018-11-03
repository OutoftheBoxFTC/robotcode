package org.ftc7244.robotcontroller.opmodes.tuning.parameter;

import java.util.ArrayList;

public class TunableParameter {
    private ArrayList<Integer> integer, decimal;
    private String name;
    private int digit;

    public TunableParameter(double value, String name){
        String integer = ((int)value) + "",
                decimal = (value%1)+"";
        this.integer = new ArrayList<>();
        this.decimal = new ArrayList<>();

        for (int i = 0; i < integer.length(); i++) {
            this.integer.add(Integer.parseInt(integer.substring(i, i+1)));
        }
        for (int i = 0; i < decimal.length(); i++) {
            this.decimal.add(Integer.parseInt(decimal.substring(i, i+1)));
        }

        this.name = name;
        digit = 0;
    }

    public void advanceDigit(boolean direction){
        digit += direction?1:-1;
        if(digit >= 0){
            if(digit == integer.size()){
                integer.add(0);
            }
        }
        else {
            if (digit == -decimal.size()){
                ArrayList<Integer> decimal = new ArrayList<>();
                decimal.add(0);
                decimal.addAll(this.decimal);
                this.decimal = decimal;
            }
        }
    }

    public void changeDigit(boolean direction){
        if(digit >= 0){
            integer.set(digit, constrain(integer.get(digit)+(direction?1:-1)));
        }
        else {
            decimal.set(decimal.size()-digit, constrain(decimal.get(decimal.size()-digit) + (direction?1:-1)));
        }
    }

    private int constrain(int val){
        return val >= 10 ? 0 : val <= -1 ? 9 : val;
    }



    public double getValue(){
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < integer.size(); i++) {
            value.append(integer.get(i));
        }
        value.append(".");
        for (int i = 0; i < decimal.size(); i++) {
            value.append(decimal.get(i));
        }
        return Integer.parseInt(value.toString());
    }


}
