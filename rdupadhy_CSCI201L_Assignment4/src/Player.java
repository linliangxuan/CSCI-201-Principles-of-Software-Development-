import java.util.Vector;

public class Player {

	private String username;
	private GameServerThread gameServerThread;
	private Vector<Card> cards;
	private int chips;
	private int currentBet;
	private boolean bust;
	private boolean blackjack;

	public Player(String username) {
		this.username = username;
		this.cards = new Vector<Card>();
		chips = 500;
		currentBet = 0;
		bust = false;
		blackjack = false;
	}
	
	public void addCards(Vector<Card> cards) {
		this.cards.addAll(cards);
	}
	
	public int playerCardsValue() {
		int playerCardsValue = 0;
		for(int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			playerCardsValue += card.value();
		}
		return playerCardsValue;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public GameServerThread getGameServerThread() {
		return gameServerThread;
	}

	public void setGameServerThread(GameServerThread gameServerThread) {
		this.gameServerThread = gameServerThread;
	}

	public int getChips() {
		return chips;
	}

	public void setChips(int chips) {
		this.chips = chips;
	}

	public int getCurrentBet() {
		return currentBet;
	}

	public void setCurrentBet(int currentBet) {
		this.currentBet = currentBet;
	}

	public Vector<Card> getCards() {
		return cards;
	}

	public void setCards(Vector<Card> cards) {
		this.cards = cards;
	}

	public boolean isBust() {
		return bust;
	}

	public void setBust(boolean bust) {
		this.bust = bust;
	}

	public boolean isBlackjack() {
		return blackjack;
	}

	public void setBlackjack(boolean blackjack) {
		this.blackjack = blackjack;
	}
	
	
}
