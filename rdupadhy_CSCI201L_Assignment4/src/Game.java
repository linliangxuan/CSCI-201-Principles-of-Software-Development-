import java.util.Vector;

public class Game {
	
	private String gamename;
	private int numPlayers;
	private Vector<Player> players;
	private int currentPlayerIndex;
	private Deck deck;
	private Vector<Card> dealerCards;
	
	public Game(String gamename) {
		this.gamename = gamename;
		players = new Vector<Player>();
		currentPlayerIndex = 0;
		numPlayers = 0;
		deck = new Deck();
		dealerCards = new Vector<Card>();
	}
	
	public Game(String gamename, int numPlayers) {
		this.gamename = gamename;
		this.numPlayers = numPlayers;
		players = new Vector<Player>();
		numPlayers = 0;
		deck = new Deck();
		currentPlayerIndex = 0;
		dealerCards = new Vector<Card>();
	}
	
	public int dealerCardsValue() {
		int dealerCardsValue = 0;
		for(int i = 0; i < dealerCards.size(); i++) {
			Card card = dealerCards.get(i);
			dealerCardsValue += card.value();
		}
		return dealerCardsValue;
	}
	
	public void addDealerCards(Vector<Card> cards) {
		dealerCards.addAll(cards);
	}
	
	public void shuffleDeck() {
		deck.shuffle();
	}
	
	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	
	public Vector<Player> getPlayers() {
		return players;
	}
	
	public void setPlayers(Vector<Player> players) {
		this.players = players;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public boolean usernameExists(String username) {
		for(int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			if(player.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	public int getCurrentPlayerIndex() {
		return currentPlayerIndex;
	}

	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
	}

	public Deck getDeck() {
		return deck;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public Vector<Card> getDealerCards() {
		return dealerCards;
	}

	public void setDealerCards(Vector<Card> dealerCards) {
		this.dealerCards = dealerCards;
	}
	
}
