package com.p6majo.math.network;

public interface SigmoidFunction {
    abstract double eval(double x);
    abstract double derivative(double x);
    abstract double inverse(double x);
}
