package com.p6majo.math.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DoubleTrainingsDataList extends ArrayList<DoubleTrainingsData> {

   public DoubleTrainingsDataList(){
       super();
   }

    public DoubleTrainingsDataList(List<DoubleTrainingsData> dataList){
        this.addAll(dataList);
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

    public String toString(){
        return this.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }

}
