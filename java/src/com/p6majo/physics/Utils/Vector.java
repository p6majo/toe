package com.p6majo.physics.Utils;

public class Vector {
    private int dim;
    private double[]  components;

    public Vector(int dim, double ... components){
        this.dim = dim;
        this.components = new double[dim];
        int count  = 0;
        for (double comp:components){
            this.components[count]=comp;
            count++;
        }
        for (int c = count;c<dim;c++) this.components[c]=0.;
    }

    public Vector( double ... components){
        this.dim = components.length;
        this.components = components;
    }

    public String toString(){
        StringBuilder out = new StringBuilder();
        out.append("(");
        for (int i=0;i<this.components.length-1;i++)
            out.append(components[i]+",");
        out.append(components[components.length-1]+")");
        return out.toString();
    }
}
