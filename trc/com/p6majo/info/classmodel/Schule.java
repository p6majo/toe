package com.p6majo.info.classmodel;

import java.util.ArrayList;

public class Schule {
    private String name;
    private ArrayList<Lehrer> lehrerschaft;
    private ArrayList<Schueler> schuelerschaft;
    private Hausmeister hausmeister;

    public Schule(String name){
        this.name = name;
        this.lehrerschaft = new ArrayList<Lehrer>();
        this.schuelerschaft = new ArrayList<Schueler>();
    }
    public void addSchueler(Schueler schueler){
        this.schuelerschaft.add(schueler);
    }
    public void addLehrer(Lehrer lehrer){
        this.lehrerschaft.add(lehrer);
    }
    public void setHausmeister(Hausmeister hausmeister){
        this.hausmeister = hausmeister;
    }
    public String toString(){
        String info="In diese Schule ("+this.getName()+") gehen:\n";
        info+=lehrerschaft.size()+" Lehrer\n";
        info+=schuelerschaft.size()+" Schueler\n";
        info+="und ein Hausmeister namens "+hausmeister.getName()+".";
        return info;
    }
    public String getName() {
        return name;
    }
}
