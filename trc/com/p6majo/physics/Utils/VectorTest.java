package com.p6majo.physics.Utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class VectorTest {

    @Test
    public void start(){
        Vector vec  = new Vector(2);
        System.out.println("initialize vector without initial data: "+vec.toString());
        vec  = new Vector(3,1.0,2.0);
        System.out.println("initialize vector with partial initial data: "+vec.toString());
        vec  = new Vector(2,1.0,2.0);
        System.out.println("initialize vector with initial data: "+vec.toString());
        vec  = new Vector(1.0,2.0);
        System.out.println("initialize vector with only initial data: "+vec.toString());
    }

}