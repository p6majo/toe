package com.p6majo.info.classmodel;

public class Mensch {
    private int alter=0;
    private String name;

    public Mensch(String name){
        this.name = name;
    }

    public Mensch(String name,int alter){
        this.name = name;
        this.alter = alter;
    }


    public String getName() {
        return name;
    }

    public int getAlter() {
        return alter;
    }

    public String toString(){
        String info = this.getName();
        return info;
    }
}
