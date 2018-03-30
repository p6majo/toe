package com.p6majo.math.network;

import com.p6majo.math.utils.Utils;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * In this test a network is trained that consists of three input neurons and eight output neurons
 * The network is supposed to convert numbers from binary representations into decimal representations
 * ie. 101 -> 5, 111->7 etc.
 * @author p6majo
 */
public class NetworkTest extends TestCase {
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

    }

    @Test
    public void testToString() {

        //test network with cross entropy cost function
        this.network = new Network(new int[]{3,8},Network.Seed.RANDOM,Network.sigmoid,Network.crossEntropy);
        this.network.stochasticGradientDescentWithAdaptiveLearningRate(dataList,1,0.01*Network.ETA);
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



        //test network with standard l2 cost function
        this.network = new Network(new int[]{3,8},Network.Seed.RANDOM,Network.sigmoid,Network.standardCostFunction);
        this.network.stochasticGradientDescentWithAdaptiveLearningRate(dataList,1,0.01*Network.ETA);
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