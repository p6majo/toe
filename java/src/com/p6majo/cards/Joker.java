package com.p6majo.cards;

/**
 * A wild card that can be used for any other card
 * The value and the symbol of the card is undefined
 * @author jmartin
 *
 */
public class Joker extends Card {

	public Joker() {
		super(null, null);
		super.isJoker = true;
		
	}

	public String toString(){
		return "Joker";
	}
}
