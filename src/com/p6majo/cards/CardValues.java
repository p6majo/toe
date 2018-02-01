package com.p6majo.cards;

public interface CardValues {
	public enum SkatValues implements CardValues {
		Sieben,
		Acht,
		Neun,
		Zehn,
		Unter,
		Ober,
		Koenig,
		Ass;
	}
	
	public enum RommeValues implements CardValues {
		two,
		three,
		four,
		five,
		six,
		seven,
		eight,
		nine,
		ten,
		bobby,
		queen,
		king,
		ass;
	}
}
