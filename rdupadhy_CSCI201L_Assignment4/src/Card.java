
public class Card {
	
	private Suit suit;
	private Rank rank;
	public enum Suit {
		SPADES,
		HEARTS,
		DIAMONDS,
		CLUBS
	}
	public enum Rank {
		ACE,
		TWO,
	    THREE,
	    FOUR,
	    FIVE,
	    SIX,
	    SEVEN,
	    EIGHT,
	    NINE,
	    TEN,
	    JACK,
	    QUEEN,
	    KING
	}
	
	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}
	
	public String printCard() {
		String returnString = rank + " of " + suit;
		return returnString;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(Suit suit) {
		this.suit = suit;
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public int value() {
		if(rank.ordinal() <= 9) {
			return rank.ordinal() + 1;
		}
		return 10;
	}
	
}
