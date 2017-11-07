import java.io.Serializable;

public class GameMessage implements Serializable {

	private static final long serialVersionUID = 1;
	public enum Type {
		START_OR_JOIN_INITIAL,
		START_OR_JOIN_OPTIONS,
		START_PLAYERS,
		START_GAMENAME,
		START_USERNAME,
		START_WAITING,
		JOIN_GAMENAME,
		JOIN_USERNAME,
		JOIN_PLAYERS_LEFT,
		JOIN_ALL_PLAYERS,
		ASK_BET_PLAYER,
		BET_OTHER,
		RESPONSE_BET_PLAYER,
		RESPONSE_OTHER,
		UI,
		STAY_OR_HIT_PLAYER,
		STAY_OR_HIT_OTHER,
		STAY_PLAYER,
		STAY_OTHER,
		HIT_NORMAL_PLAYER,
		HIT_NORMAL_OTHER,
		HIT_BUSTED_PLAYER,
		HIT_BUSTED_OTHER,
		STAY_OR_HIT_UI,
		DEALER_STAY_OR_HIT,
		
	}
	private String message;
	private Type type;
	
	public GameMessage(Type type, String message) {
		this.setType(type);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
}
