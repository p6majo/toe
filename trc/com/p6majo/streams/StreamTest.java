package com.p6majo.streams;

import com.p6majo.math.complex.Complex;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.function.Function;
import java.util.stream.IntStream;

public class StreamTest extends TestCase {

    @Test
    public void testStream() {
        //for loop
        for (int y=0;y<10;y++) System.out.println(y);


        //int stream
        IntStream.range(0,10).boxed().forEach(System.out::print);
        System.out.println();
        IntStream.range(0,10).boxed().parallel().forEach(System.out::print);
        System.out.println();

        Function<Complex,Complex> function = (a)->a.times(a);

        //nexted for loop
        for (int y=0;y<10;y++) {
            for (int x = 0; x < 2; x++) {
                System.out.print("(" + x + "," + y + ")");
            }
            System.out.println();
        }

        int xdim = 2000;
        int ydim = 3000;

        long start = System.currentTimeMillis();
        Complex[] array = IntStream.range(0,ydim).boxed().parallel()
                .flatMap(y->IntStream
                        .range(0,xdim)
                        .mapToObj(x->new Complex(x,y))
                )
                .map(function)
                .toArray(Complex[]::new);
        System.out.println("parallel computing: "+(System.currentTimeMillis()-start)+" ms.");

        long start2 = System.currentTimeMillis();
        Complex[] array2 = IntStream.range(0,ydim).boxed()
                .flatMap(y->IntStream
                        .range(0,xdim)
                        .mapToObj(x->new Complex(x,y))
                )
                .map(function)
                .toArray(Complex[]::new);
        System.out.println("serial computing: "+(System.currentTimeMillis()-start)+" ms.");


        long start3 = System.currentTimeMillis();
        long count = IntStream.range(0,array.length).mapToObj(x->array[x].minus(array2[x])).filter(x->x.compareTo(Complex.NULL)==0).count();
        System.out.println("Comparison serially: "+(System.currentTimeMillis()-start3)+" ms.");

        long start4 = System.currentTimeMillis();
        long count2 = IntStream.range(0,array.length).parallel().mapToObj(x->array[x].minus(array2[x])).filter(x->x.compareTo(Complex.NULL)==0).count();
        System.out.println("Comparison parallely: "+(System.currentTimeMillis()-start4)+" ms.");


        assertEquals(Math.min(count,count2),ydim*xdim);

        System.out.println("Passing of the test shows, that although the stream is processed in a parallel way, the order of the elements is preserved");
    }


}
