package com.p6majo.info.classmodel;

public class Hausmeister extends Mensch {
    public Hausmeister(String name){
        super(name);
    }

    public String toString() {
        return "Hausmeister: " + super.toString();
    }
}
