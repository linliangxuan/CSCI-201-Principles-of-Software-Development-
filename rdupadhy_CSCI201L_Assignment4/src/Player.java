import java.util.Vector;

public class Player {

	private String username;
	private GameServerThread gameServerThread;
	private Vector<Card> cards;
	private int chips;
	private int currentBet;

	public Player(String username) {
		this.username = username;
		cards = new Vector<Card>();
		chips = 500;
		currentBet = 0;
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
	
	
}
