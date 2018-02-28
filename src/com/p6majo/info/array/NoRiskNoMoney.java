package com.p6majo.info.array;

public class NoRiskNoMoney {

    private boolean[] gewuerfelteZahlen=new boolean[6];
    private int gewinn =0;
    private boolean spielFertig = false;

   public void wuerfeln(){
           int wuerfel = (int) Math.round(Math.random()*6+0.5);
           System.out.println("Wurf: "+wuerfel);
           if (gewuerfelteZahlen[wuerfel-1]){
               this.gewinn=0;
               spielFertig = true;
           }
           else{
               gewinn+=1;
               gewuerfelteZahlen[wuerfel-1]=true;
           }
   }

    public int getGewinn() {
        return gewinn;
    }

    public boolean isSpielFertig(){
       return this.spielFertig;
    }
}
