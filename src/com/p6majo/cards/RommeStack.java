package com.p6majo.cards;

@SuppressWarnings("serial")
public class RommeStack extends Stack<Symbol.FrenchSymbols,CardValues.RommeValues> {
	
	
	public RommeStack(){	
		
		for (Symbol symbol: Symbol.FrenchSymbols.values()){
			for (CardValues value: CardValues.RommeValues.values()){
				Card card = new Card(symbol,value);
				this.add(card);
			}
		}
		
		this.add(new Joker());
		this.add(new Joker());
		this.add(new Joker());
		
		System.out.println("For this stack "+this.size()+" cards have been created!");
	}
} 