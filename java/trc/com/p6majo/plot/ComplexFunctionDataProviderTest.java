package com.p6majo.plot;

import com.p6majo.math.complex.Complex;
import org.junit.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.p6majo.math.function.FcnCos;

public class ComplexFunctionDataProviderTest {


    @Test
    public void start() {
        int xdim = 11;
        int ydim = 11;

        PlotRange plotRange = new PlotRange();
        plotRange.addRange(0,Math.PI*2,xdim);
        plotRange.addRange(0,Math.PI*2,ydim);


        ComplexFunctionDataProvider provider = new ComplexFunctionDataProvider(new FcnCos());
        provider.setPlotRange(plotRange);
         provider.start();



        for (int y=0;y<ydim;y++){
            for (int x=0;x<xdim;x++){
                System.out.print(provider.getData(y*xdim+x).toString()+" ");
            }
            System.out.println("\n");
        };

        /*
        System.out.println(IntStream.range(0,ydim)
                .boxed()
                .flatMap(y->IntStream.range(0,xdim)
                        .mapToObj(x->outData[y*xdim+x].toString())
                ).collect(Collectors.joining(" "))
        );
        */

    }
}