import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GameServerThread extends Thread {

	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private GameServer gameServer;
	private String gamename;
	private String username;
	
	
	public GameServerThread(Socket socket, GameServer gameServer) {
		try {
			this.gameServer = gameServer;
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());
			this.start();
		} catch (IOException ioe) {
			System.out.println("ioe in GameServerThread constructor: " + ioe.getMessage());
		}
	}
	
	public void sendMessage(GameMessage gameMessage) {
		try {
			oos.writeObject(gameMessage);
			oos.flush();
		} catch (IOException ioe) {
			System.out.println("ioe in GameServerThread.sendMessage(): " + ioe.getMessage());
		}
	}
	
	public void run() {
		try {
			while(true) {
				GameMessage receivedMessage = (GameMessage)ois.readObject();
				gameServer.responseMenu(receivedMessage, this);
			}
		} catch (IOException ioe) {
			System.out.println("ioe in GameServerThread.run(): " + ioe.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe in GameServerThread.run(): " + cnfe.getMessage());
		}
	}

	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
