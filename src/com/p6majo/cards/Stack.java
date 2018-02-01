package com.p6majo.cards;

import java.util.ArrayList;
import java.util.Collections;

@SuppressWarnings("serial")
public class Stack<S extends Enum<S> ,V extends Enum<V>> extends ArrayList<Card> {
	
	public void shuffle(){
		Collections.shuffle(this);
	}
	
}
