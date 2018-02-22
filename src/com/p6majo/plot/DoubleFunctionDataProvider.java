package com.p6majo.plot;

import com.p6majo.math.complex.Complex;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.IntStream;

public class DoubleFunctionDataProvider extends DoubleDataProvider {

    final private Function<Double,Double> function;

    /*
    private static double dx;
    private static double dy;

    private static double xmin;
    private static double ymin;

    private static int ySamples;
    private static int xSamples;
    */

    public DoubleFunctionDataProvider(Function<Double, Double> function){
        super();
        this.function  = function;
    }


    /**
     * Causes this thread to begin execution; the Java Virtual Machine
     * calls the <code>run</code> method of this thread.
     * <p>
     * The result is that two threads are running concurrently: the
     * current thread (which returns from the call to the
     * <code>start</code> method) and the other thread (which executes its
     * <code>run</code> method).
     * <p>
     * It is never legal to start a thread more than once.
     * In particular, a thread may not be restarted once it has completed
     * execution.
     *
     * @throws IllegalThreadStateException if the thread was already
     *                                     started.
     * @see #run()
     * @see #stop()
     */
    @Override
    public synchronized void start() {
        long dataDuration = System.currentTimeMillis();
        Range xRange = plotRange.getRange(0);

        int xSamples = xRange.getSamples();

        final double dx = (xRange.getEnd().doubleValue()-xRange.getStart().doubleValue())/(xSamples-1);

        final double xmin = xRange.getStart().doubleValue();

        //this is not so good since the reference to the old data array is destroyed
        ToDoubleFunction<Integer> rescaleFunction= new ToDoubleFunction<Integer>() {
            @Override
            public double applyAsDouble(Integer value) {
                return function.apply(xmin+dx*value);
            }
        };

        double[] tmpData = IntStream.range(0,xSamples)
                .boxed()
                .parallel()
                .mapToDouble(rescaleFunction)
                .toArray();

        IntStream.range(0,xSamples).forEach(i->setData(i,tmpData[i]));
        System.out.println("Data generated in "+(System.currentTimeMillis()-dataDuration)+" ms.");
    }

}