package org.ftc7244.robotcontroller.opmodes.tuning.parameter;

import java.util.ArrayList;

/**This mapping is a sample. It can theoretically go infinitely in either direction from the decimal point
 *
 * Number: 1 0 0 0 0 0 0 0 . 0  0  0  0  0  0
 * Digit:  7 6 5 4 3 2 1 0  -1 -2 -3 -4 -5 -6
 * String: 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14
 * Index:  0 1 2 3 4 5 6 7   0  1  2  3  4  5
 */
public class TunableDecimal {
    private ArrayList<Integer> integer, decimal;
    private String name;
    private int digit;

    public TunableDecimal(double value, String name){
        setValue(value);
        this.name = name;
    }

    public void setValue(double value){
        String integer = ((int)value) + "",
                decimal = (value%1)+"";
        this.integer = new ArrayList<>();
        this.decimal = new ArrayList<>();

        for (int i = 0; i < integer.length(); i++) {
            this.integer.add(Integer.parseInt(integer.substring(i, i+1)));
        }
        for (int i = 2; i < decimal.length(); i++) {
            this.decimal.add(Integer.parseInt(decimal.substring(i, i+1)));
        }
        digit = 0;
    }

    public void advanceDigit(boolean direction){
        digit += direction?1:-1;
        int index = index(digit);
        if(digit >= 0){
            if(index < 0) {
                ArrayList<Integer> integer = new ArrayList<>();
                integer.add(0);
                integer.addAll(this.integer);
                this.integer = integer;
            }
        }
        else {
            if(index >= decimal.size()){
                decimal.add(0);
            }
        }
    }

    public void changeDigit(boolean direction){
        int index = index(digit);
        if(digit >= 0){
            integer.set(index, constrain(integer.get(index)+(direction?1:-1)));
        }
        else {
            decimal.set(index, constrain(decimal.get(index) + (direction?1:-1)));
        }
    }

    private int constrain(int val){
        return val >= 10 ? 0 : val <= -1 ? 9 : val;
    }

    private int index(int digit){
        if(digit >= 0){
            return integer.size()-1-digit;
        }
        return -digit-1;
    }

    private int stringIndex(int digit){
        return integer.size()-digit+(digit<0?2:1);
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

    @Override
    public String toString() {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < integer.size(); i++) {
            value.append(integer.get(i));
        }
        value.append(".");
        for (int i = 0; i < decimal.size(); i++) {
            value.append(decimal.get(i));
        }
        int stringIndex = stringIndex(digit);
        value.insert(stringIndex-2, "|");
        value.insert(stringIndex, "|");
        return value.toString();
    }

    public String getName() {
        return name;
    }

    public int getSelectedDigit(){
        int index = index(digit);
        return digit < 0 ? decimal.get(index) : integer.get(index);
    }
}
