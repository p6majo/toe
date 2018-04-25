package com.p6majo.streams;

import org.junit.Test;

import java.util.stream.IntStream;

public class IntStreamTest {
    @Test
    public void intStreamTest(){
       IntStream.iterate(0,i->i+2).limit(10/2).forEach(i->System.out.println(i+""));
    }
}
