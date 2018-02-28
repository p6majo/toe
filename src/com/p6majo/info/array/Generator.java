package com.p6majo.info.array;

/**
 * Hilfsklasse, die verschiede Arrays erzeugt und zur�ck gibt.
 * 
 * @author (Thomas Karp) 
 * @version (21.4.2008)
 */
public class Generator
{
    /**
     * Berechnet eine Menge von Zufallszahlen. Die Anzahl der Zufallszahlen ist ebenfalls zuf�llig.
     * @return Array von Zufallszahlen.
     */
    public static int[] getZufallszahlen()
    {
        int anzahl = (int) (Math.random() * 101);
        int[] zahlen = new int[anzahl];
        for(int i = 0; i < zahlen.length; i++)
        {
            zahlen[i] = (int) (Math.random() * 100000);
        }
        return zahlen;
    }
    
    /**
     * Liefert ein Array von Hunden zur�ck.
     */
    public static Hund[] getHunde()
    {
        int anzahl = (int) (Math.random() * 10);
        Hund[] hunde = new Hund[anzahl];
        for(int i = 0; i < hunde.length; i++)
        {
            hunde[i] = new Hund();
        }
        return hunde;
    }
}
