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
    private DataList dataList;

    @Before
    public void setUp() throws Exception {
        //calculate the network for the conversion of binary numbers of three bits into a decimal
        dataList= new DataList();
        dataList.add(new Data(new Double[]{0.,0.,0.},new Double[]{1.,0.,0.,0.,0.,0.,0.,0.}));//zero
        dataList.add(new Data(new Double[]{0.,0.,1.},new Double[]{0.,1.,0.,0.,0.,0.,0.,0.}));//one
        dataList.add(new Data(new Double[]{0.,1.,0.},new Double[]{0.,0.,1.,0.,0.,0.,0.,0.}));//two
        dataList.add(new Data(new Double[]{0.,1.,1.},new Double[]{0.,0.,0.,1.,0.,0.,0.,0.}));//three
        dataList.add(new Data(new Double[]{1.,0.,0.},new Double[]{0.,0.,0.,0.,1.,0.,0.,0.}));//four
        dataList.add(new Data(new Double[]{1.,0.,1.},new Double[]{0.,0.,0.,0.,0.,1.,0.,0.}));//five
        dataList.add(new Data(new Double[]{1.,1.,0.},new Double[]{0.,0.,0.,0.,0.,0.,1.,0.}));//six
        dataList.add(new Data(new Double[]{1.,1.,1.},new Double[]{0.,0.,0.,0.,0.,0.,0.,1.}));//seven

    }

    @Test
    public void testToString() {

        //test network with cross entropy cost function
        this.network = new Network(new int[]{3,8},Network.Seed.RANDOM,Network.sigmoid,Network.crossEntropy);
        this.network.stochasticGradientDescent(dataList,1,0.01);
        System.out.println(this.network.toString());


        System.out.println(this.network.runVerboseTest(10));
        //check for mistakes of the trained network
        assertEquals(1., this.network.runTest(100),0.001);

        //test network with standard l2 cost function
        this.network = new Network(new int[]{3,8},Network.Seed.RANDOM,Network.sigmoid,Network.standardCostFunction);
        this.network.stochasticGradientDescent(dataList,1,0.01);
        System.out.println(this.network.toString());


        System.out.println(this.network.runVerboseTest(10));
        //check for mistakes of the trained network
        assertEquals(1., this.network.runTest(100),0.001);


    }


}