package com.p6majo.math.network2;

import com.p6majo.math.network.DataList;
import com.p6majo.math.network2.layers.CrossEntropyLayer;
import com.p6majo.math.network2.layers.L2Layer;
import com.p6majo.math.network2.layers.LinearLayer;
import com.p6majo.math.network2.layers.SigmoidLayer;
import org.junit.Before;
import org.junit.Test;
import org.nd4j.linalg.factory.Nd4j;

import static com.p6majo.math.network2.Network.Seed.ALL_UNITY;
import static org.junit.Assert.*;

public class NetworkTest {
    Data[] dataList = new Data[1];
    
    
    @Before
    public void setUp() throws Exception {


        
        //dataList[0]=(new Data(Nd4j.create(new float[]{0.f,0.f,0.f}), Nd4j.create(new float[]{1.f,0.f,0.f,0.f,0.f,0.f,0.f,0.f})));//zero

        dataList[0]=(new Data(Nd4j.create(new float[]{0.f,0.f,1.f}),Nd4j.create(new float[]{0.f,1.f,0.f,0.f,0.f,0.f,0.f,0.f})));//one
        /*
        dataList[2]=(new Data(Nd4j.create(new float[]{0.f,1.f,0.f}),Nd4j.create(new float[]{0.f,0.f,1.f,0.f,0.f,0.f,0.f,0.f})));//two
        dataList[3]=(new Data(Nd4j.create(new float[]{0.f,1.f,1.f}),Nd4j.create(new float[]{0.f,0.f,0.f,1.f,0.f,0.f,0.f,0.f})));//three
        dataList[4]=(new Data(Nd4j.create(new float[]{1.f,0.f,0.f}),Nd4j.create(new float[]{0.f,0.f,0.f,0.f,1.f,0.f,0.f,0.f})));//four
        dataList[5]=(new Data(Nd4j.create(new float[]{1.f,0.f,1.f}),Nd4j.create(new float[]{0.f,0.f,0.f,0.f,0.f,1.f,0.f,0.f})));//five
        dataList[6]=(new Data(Nd4j.create(new float[]{1.f,1.f,0.f}),Nd4j.create(new float[]{0.f,0.f,0.f,0.f,0.f,0.f,1.f,0.f})));//six
        dataList[7]=(new Data(Nd4j.create(new float[]{1.f,1.f,1.f}),Nd4j.create(new float[]{0.f,0.f,0.f,0.f,0.f,0.f,0.f,1.f})));//seven
        */
    }

    @Test
    public void construction2Test(){
        Network network = new Network(false);

        LinearLayer ll = new LinearLayer(new int[]{1,3},8,ALL_UNITY);
        network.addLayer(ll);

        SigmoidLayer sig = new SigmoidLayer(new int[]{8});
        network.addLayer(sig);

        L2Layer l2 = new L2Layer(new int[]{8});
        network.addLayer(l2);

        System.out.println(network.toString());
        network.train(dataList,dataList,1);
        System.out.println(network.toString());
    }
}