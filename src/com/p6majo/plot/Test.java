package com.p6majo.plot;

public interface Test {
    default int method(int x,int y){
        return x+y;
    }
}
