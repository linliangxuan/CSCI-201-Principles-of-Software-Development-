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
		BET,
		
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
