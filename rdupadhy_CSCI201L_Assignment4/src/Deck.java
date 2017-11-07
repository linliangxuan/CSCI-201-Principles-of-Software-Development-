import java.util.Collections;
import java.util.Vector;

public class Deck {

	private Vector<Card> cards;

	public Deck() {
		cards = new Vector<Card>();
		for(Card.Suit suit : Card.Suit.values()) {
			for(Card.Rank rank : Card.Rank.values()) {
				Card card = new Card(suit, rank);
				cards.add(card);
			}
		}
	}
	
	public void shuffle() {
		Collections.shuffle(cards);		
	}
	
	public Vector<Card> drawTwoCards() {
		Vector<Card> returnVector = new Vector<Card>();
		if(cards.size() >= 2) {
			returnVector.add(cards.get(0));
			returnVector.add(cards.get(1));
			cards.remove(0);
			cards.remove(0);
		}
		else if(cards.size() == 1) {
			returnVector.add(cards.get(0));
			cards.remove(0);
		} 
		return returnVector;
	}
	
	public Vector<Card> drawOneCard() {
		Vector<Card> returnVector = new Vector<Card>();
		if(cards.size() >= 1) {
			returnVector.add(cards.get(0));
			cards.remove(0);
		} 
		return returnVector;
	}

	public Vector<Card> getCards() {
		return cards;
	}

	public void setCards(Vector<Card> cards) {
		this.cards = cards;
	}

}
