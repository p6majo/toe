package com.p6majo.math.network2;

import java.util.ArrayList;
import java.util.List;

public class TestResult {

    private final List<Integer> failures;
    private final float[] probabilities;
    private final int batchSize;


    public TestResult(int batchSize){
        this.batchSize=batchSize;
        this.probabilities = new float[batchSize];
        this.failures = new ArrayList<Integer>();
    }

    public double getSuccessRate(){
        return 1.- (double) this.failures.size()/this.batchSize;
    }

    public void addFailure(int i) {
        this.failures.add(i);
    }

    public void setProbability(int i,float prop){
        probabilities[i]=prop;
    }
}
