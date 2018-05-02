package com.p6majo.math.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public abstract class FunctionWithParameters<A,B> implements Function<A,B> {
    protected Map<String,A> params = new TreeMap<String,A>();

    public void addParam(String key, A value){
        params.put(key,value);
    }

    public void addAll(Map<String,A> params){
        this.params.putAll(params);
    }

    public Map<String,A> getParams(){
        return this.params;
    }

    public void setParam(String key, A value){
        params.replace(key,value);
    }
}
