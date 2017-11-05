import java.util.Vector;

public class Game {
	
	private String gamename;
	private Vector<Player> players;
	private int currentPlayerIndex;
	private Deck deck;
	
	public Game(String gamename) {
		this.setGamename(gamename);
		players = new Vector<Player>();
		currentPlayerIndex = 0;
	}
	
	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
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
	
}
