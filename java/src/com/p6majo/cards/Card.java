package com.p6majo.cards;

/**
 * A card is defined by its symbol and its value
 * Attention: both symbol and value can be null, representing possible joker cards
 * @author jmartin
 *
 */
public class Card {
	private Symbol symbol;
	private CardValues value;
	protected boolean isJoker;
	
	public Card(Symbol symbol, CardValues cardValue){
		if (symbol!=null && cardValue!=null){
			this.symbol = symbol;
			this.value = cardValue;
			this.isJoker = false;
		}
		else this.isJoker = true;
		
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public CardValues getValue() {
		return value;
	}

	public void setValue(CardValues value) {
		this.value = value;
	}
	
	public String toString(){
		return symbol.toString()+":"+value.toString();
	}
}
