package com.p6majo.info.bundesliga;

public class Liga {

    private String liga;
    private Verein[] vereine;

    public Liga(String liga,int anzahl){
        this.liga = liga;
        this.vereine = new Verein[anzahl];
    }

    public void setVerein(Verein verein, int pos){
        this.vereine[pos]=verein;
    }

    public void listVereine(){

        System.out.println("----------------------------");
        System.out.println(this.liga);
        System.out.println("----------------------------");
        for (int v = 0 ;v<vereine.length;v++){
            System.out.println(vereine[v].toString());
        }
        System.out.println("----------------------------");
    }

    public void listTabelle(){
        //Sortierung und dann Ausgabe
    }

}
