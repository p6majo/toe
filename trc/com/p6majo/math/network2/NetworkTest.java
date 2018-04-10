package com.p6majo.math.network2;

import com.p6majo.math.network2.layers.CrossEntropyLayer;
import com.p6majo.math.network2.layers.LinearLayer;
import com.p6majo.math.network2.layers.SigmoidLayer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NetworkTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void constructionTest(){
        Network network = new Network(false);

        LinearLayer ll = new LinearLayer(new int[]{1,784},16);
        network.addLayer(ll);

        SigmoidLayer sig = new SigmoidLayer(new int[]{16});
        network.addLayer(sig);

        LinearLayer ll2 = new LinearLayer(new int[]{16},10);
        network.addLayer(ll2);

        SigmoidLayer sig2 = new SigmoidLayer(new int[]{10});
        network.addLayer(sig2);

        CrossEntropyLayer cel = new CrossEntropyLayer(new int[]{10});
        network.addLayer(cel);

        System.out.println(network.toString());
    }
}