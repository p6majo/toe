package com.p6majo.math.network;

public class TestVisualization {

    public static void main(String[] args){
        Network network = new Network(new int[]{3,3,8},Network.Seed.RANDOM,Network.sigmoid,Network.crossEntropy,true);

           //calculate the network for the conversion of binary numbers of three bits into a decimal
        DataList dataList= new DataList();
        dataList.add(new Data(new Double[]{0.,0.,0.},new Double[]{1.,0.,0.,0.,0.,0.,0.,0.}));//zero
        dataList.add(new Data(new Double[]{0.,0.,1.},new Double[]{0.,1.,0.,0.,0.,0.,0.,0.}));//one
        dataList.add(new Data(new Double[]{0.,1.,0.},new Double[]{0.,0.,1.,0.,0.,0.,0.,0.}));//two
        dataList.add(new Data(new Double[]{0.,1.,1.},new Double[]{0.,0.,0.,1.,0.,0.,0.,0.}));//three
        dataList.add(new Data(new Double[]{1.,0.,0.},new Double[]{0.,0.,0.,0.,1.,0.,0.,0.}));//four
        dataList.add(new Data(new Double[]{1.,0.,1.},new Double[]{0.,0.,0.,0.,0.,1.,0.,0.}));//five
        dataList.add(new Data(new Double[]{1.,1.,0.},new Double[]{0.,0.,0.,0.,0.,0.,1.,0.}));//six
        dataList.add(new Data(new Double[]{1.,1.,1.},new Double[]{0.,0.,0.,0.,0.,0.,0.,1.}));//seven

       long start = System.currentTimeMillis();
        network.stochasticGradientDescent(dataList,1,0.01);
        System.out.println("Time: "+(System.currentTimeMillis()-start));

    }

}
