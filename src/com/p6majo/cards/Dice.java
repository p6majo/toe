package com.p6majo.cards;

public class Dice {
    public static int roll(){
        return (int) Math.round(Math.random()*6+0.5);
    }
}
