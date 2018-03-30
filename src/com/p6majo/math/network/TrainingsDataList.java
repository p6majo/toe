package com.p6majo.math.network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainingsDataList<F> extends ArrayList<TrainingsData<F>> {

   public TrainingsDataList(){
       super();
   }

    public TrainingsDataList(List<TrainingsData<F>> dataList){
        this.addAll(dataList);
    }

    public void shuffle() {
        Collections.shuffle(this);
    }

}
