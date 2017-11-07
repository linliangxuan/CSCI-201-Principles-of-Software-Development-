import java.util.Vector;

public class Test {

	public static void printCards(Vector<Card> cards) {
		for(int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			System.out.println(card.value() + ": " + card.printCard());
		}
	}
	
	public static void main(String [] args) {
		Deck deck = new Deck();
		System.out.println(deck.getCards().size());
		deck.shuffle();
		Vector<Card> cards1 = deck.drawTwoCards();
		System.out.println(deck.getCards().size());
		printCards(cards1);
		Vector<Card> cards2 = deck.drawTwoCards();
		printCards(cards2);
	}
	
}
