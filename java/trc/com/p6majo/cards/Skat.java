package com.p6majo.cards;

public class Skat {
	
	public static void main(String[] args){
		SkatStack stack = new SkatStack();
		stack.shuffle();
		System.out.println(stack.toString());
		
		RommeStack stack2 = new RommeStack();
		stack2.shuffle();
		System.out.println(stack2.toString());
		
		
	}
}
