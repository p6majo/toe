package com.p6majo.cards;

@SuppressWarnings("serial")
public class SkatStack extends Stack<Symbol.GermanSymbols,CardValues.SkatValues> {
	
	
	public SkatStack(){	
		
		for (Symbol symbol: Symbol.GermanSymbols.values()){
			for (CardValues value: CardValues.SkatValues.values()){
				Card card = new Card(symbol,value);
				this.add(card);
			}
		}
		
		System.out.println("For this stack "+this.size()+" cards have been created!");
	}
}
