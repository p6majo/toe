package com.p6majo.cards;

import org.junit.Test;


public class DiceTest {
    @Test
    public void rollTest() throws Exception {
        int[] counter = new int[6];
        for (int i=0;i<1000000;i++){
            int diceValue = Dice.roll();
            counter[diceValue-1]++;
        }

        for (int i=0;i<6;i++){
            System.out.println((i+1)+": "+counter[i]+" times.");
        }

        int sum = 0;
        for (int i=1;i<101;i++){
            sum = sum +i;
        }
    }

}