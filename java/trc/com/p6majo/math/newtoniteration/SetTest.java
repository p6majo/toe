package com.p6majo.math.newtoniteration;

import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class SetTest {

    @Test
    public void testSets() throws Exception {


        class MyClass {
            public Double value;

            public MyClass(double value) {
                this.value = value;
            }

            public String toString() {
                return this.value.toString();
            }
        }

        Set<MyClass> classSet = new TreeSet<MyClass>(new Comparator<MyClass>(){

            @Override
            public int compare(MyClass o1, MyClass o2) {
                return Double.compare(o1.value,o2.value);
            }
        });

        for (int i=0;i<10;i++) classSet.add(new MyClass(Math.random()));

        System.out.println(classSet.toString());
        int element = (int) (Math.floor(Math.random()*10));
        System.out.println("Now change the value of the "+element+" element in the set.");


        Iterator<MyClass> it = classSet.iterator();
        int count=1;
        while (it.hasNext()&& count<element){
            count++;
            it.next();
        }

        it.next().value=0.8;

        System.out.println(classSet.toString());
        System.out.println("The change doesn't trigger a new sort. The set must be resorted manually");


    }

}
