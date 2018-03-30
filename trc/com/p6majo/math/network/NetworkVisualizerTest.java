package com.p6majo.math.network;


import com.p6majo.math.utils.Utils;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Collectors;

public class NetworkVisualizerTest extends TestCase {

    private Network network = null;
    private TrainingsDataList<Double> dataList;


    @Before
    public void setUp() throws Exception {
        //calculate the network for the conversion of binary numbers of three bits into a decimal

        dataList= new TrainingsDataList<Double>();
        dataList.add(new DoubleTrainingsData(new Double[]{0.,0.,0.},new Double[]{1.,0.,0.,0.,0.,0.,0.,0.}));//zero
        dataList.add(new DoubleTrainingsData(new Double[]{0.,0.,1.},new Double[]{0.,1.,0.,0.,0.,0.,0.,0.}));//one
        dataList.add(new DoubleTrainingsData(new Double[]{0.,1.,0.},new Double[]{0.,0.,1.,0.,0.,0.,0.,0.}));//two
        dataList.add(new DoubleTrainingsData(new Double[]{0.,1.,1.},new Double[]{0.,0.,0.,1.,0.,0.,0.,0.}));//three
        dataList.add(new DoubleTrainingsData(new Double[]{1.,0.,0.},new Double[]{0.,0.,0.,0.,1.,0.,0.,0.}));//four
        dataList.add(new DoubleTrainingsData(new Double[]{1.,0.,1.},new Double[]{0.,0.,0.,0.,0.,1.,0.,0.}));//five
        dataList.add(new DoubleTrainingsData(new Double[]{1.,1.,0.},new Double[]{0.,0.,0.,0.,0.,0.,1.,0.}));//six
        dataList.add(new DoubleTrainingsData(new Double[]{1.,1.,1.},new Double[]{0.,0.,0.,0.,0.,0.,0.,1.}));//seven


        System.out.println("Data set for training: \n"+dataList.stream().map(Object::toString).collect(Collectors.joining("\n")));
    }


    @Test
    public void testVisualizer() {

        //test network with cross entropy cost function
        this.network = new Network(new int[]{3,8},Network.Seed.RANDOM,Network.sigmoid,Network.standardCostFunction,true);
        NetworkVisualizer visualizer = new NetworkVisualizer(network,NetworkVisualizer.VisualizerModus.ALL_VERTICES);


        this.network.stochasticGradientDescentStreamDelta(dataList,1,0.1*Network.ETA,0.001);
        System.out.println(this.network.toString());


        for (TrainingsData<Double> data:dataList){
            System.out.println("Output: "+ Utils.array2String(this.network.eval(data).getActivations(),2));
            System.out.println("Expectation: "+Utils.array2String(data.getExpectation(),2));
        }

        //check for mistakes of the trained network
        for (TrainingsData<Double> data:dataList) {
            Double[] out = this.network.eval(data).getActivations();
            for (int i = 0; i < data.getInput().length; i++) {
                assertEquals(Math.round(out[i]),(int)(double) (data.getExpectation()[i]));
            }
        }

    }


}