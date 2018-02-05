package com.p6majo.info.aachen.classmodel;

public class Hausmeister extends Mensch {
    public Hausmeister(String name){
        super(name);
    }

    public String toString() {
        return "Hausmeister: " + super.toString();
    }
}
