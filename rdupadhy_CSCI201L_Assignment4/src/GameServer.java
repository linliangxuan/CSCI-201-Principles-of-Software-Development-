import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class GameServer {

private Vector<GameServerThread> gameServerThreads;
private Map<String, Game> gameMap;
	
	public GameServer(int port) throws IllegalArgumentException, IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Successfully started the Black Jack server on port " + port);
		gameServerThreads = new Vector<GameServerThread>();
		gameMap = new HashMap<String, Game>();
		
		while(true) {
			Socket socket = serverSocket.accept();
			GameServerThread gameServerThread = new GameServerThread(socket, this);
			gameServerThreads.add(gameServerThread);
			responseMenu(null, gameServerThread);
		}
	
	}
	
	public void responseMenu(GameMessage receviedMessage, GameServerThread gameServerThread) {
		if(receviedMessage == null) {
			startOrJoinInitialMenu(receviedMessage, gameServerThread);
		}
		else if(receviedMessage.getType() == GameMessage.Type.START_OR_JOIN_INITIAL) {
			startOrJoinOptionsMenu(receviedMessage, gameServerThread);
		}
		else if(receviedMessage.getType() == GameMessage.Type.START_PLAYERS) {
			startGamePlayersMenu(receviedMessage, gameServerThread);
		}
		else if(receviedMessage.getType() == GameMessage.Type.START_GAMENAME) {
			startGameNameMenu(receviedMessage, gameServerThread);
		}
		else if(receviedMessage.getType() == GameMessage.Type.START_USERNAME) {
			startUserNameMenu(receviedMessage, gameServerThread);
		}
		else if(receviedMessage.getType() == GameMessage.Type.JOIN_GAMENAME) {
			joinGameNameMenu(receviedMessage, gameServerThread);
		}
		else if(receviedMessage.getType() == GameMessage.Type.JOIN_USERNAME) {
			joinUserNameMenu(receviedMessage, gameServerThread);
		}
	}
	
	public void startOrJoinInitialMenu(GameMessage receviedMessage, GameServerThread gameServerThread) {
		String response = "Please chooose from the options below\n1) Start Game\n2) Join Game";
		GameMessage responseMessage = new GameMessage(GameMessage.Type.START_OR_JOIN_INITIAL, response);
		gameServerThread.sendMessage(responseMessage);
	}
	
	public void startOrJoinOptionsMenu(GameMessage receviedMessage, GameServerThread gameServerThread) {
		if(receviedMessage.getMessage().equals("1")) {
			String response = "Please chooose the number of players in the game";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.START_PLAYERS, response);
			gameServerThread.sendMessage(responseMessage);
		} 
		else if(receviedMessage.getMessage().equals("2")) {
			String response = "Please enter the name of the game you wish to join";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.JOIN_GAMENAME, response);
			gameServerThread.sendMessage(responseMessage);
		}
	}
	
	public void startGamePlayersMenu(GameMessage receviedMessage, GameServerThread gameServerThread) {
		if(receviedMessage.getMessage().equals("1") || receviedMessage.getMessage().equals("2") || receviedMessage.getMessage().equals("3")) {
			String response = "Please chooose a name for your game";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.START_GAMENAME, response);
			gameServerThread.sendMessage(responseMessage);
		}
	}
	
	public void startGameNameMenu(GameMessage receviedMessage, GameServerThread gameServerThread) {
		if(gameMap.containsKey(receviedMessage.getMessage())) {
			String response = "Invalid choice. This game name has already been chosen by another user\nPlease chooose a name for your game";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.START_GAMENAME, response);
			gameServerThread.sendMessage(responseMessage);
		}
		else {
			String gamename = receviedMessage.getMessage();
			Game game = new Game(gamename);
			gameMap.put(gamename, game);
			gameServerThread.setGamename(gamename);
			String response = "Please choose a username";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.START_USERNAME, response);
			gameServerThread.sendMessage(responseMessage);
		}
	}
	
	public void startUserNameMenu(GameMessage receviedMessage, GameServerThread gameServerThread) {
		String username = receviedMessage.getMessage();
		String gamename = gameServerThread.getGamename();
		Game game = gameMap.get(gamename);
		Player player = new Player(username);
		player.setGameServerThread(gameServerThread);
		gameServerThread.setUsername(username);
		game.addPlayer(player);
		String response = "Waiting for 2 other players to join...";
		GameMessage responseMessage = new GameMessage(GameMessage.Type.START_WAITING, response);
		gameServerThread.sendMessage(responseMessage);
	}
	
	public void joinGameNameMenu(GameMessage receviedMessage, GameServerThread gameServerThread) {
		if(gameMap.containsKey(receviedMessage.getMessage())) {
			String gamename = receviedMessage.getMessage();
			Game game = gameMap.get(gamename);
			gameServerThread.setGamename(gamename);			
			String response = "Please choose a username";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.JOIN_USERNAME, response);
			gameServerThread.sendMessage(responseMessage);
		}
		else {
			String response = "Invalid choice. There are no ongoing games with this name\nPlease enter the name of the game you wish to join";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.JOIN_GAMENAME, response);
			gameServerThread.sendMessage(responseMessage);
		}
	}
	
	public void joinUserNameMenu(GameMessage receviedMessage, GameServerThread gameServerThread) {
		String username = receviedMessage.getMessage();
		String gamename = gameServerThread.getGamename();
		Game game = gameMap.get(gamename);
		if(game.usernameExists(username) == true) {
			String response = "Invalid choice. This username has already been chosen by another player in this game\nPlease choose a username";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.START_WAITING, response);
			gameServerThread.sendMessage(responseMessage);
		}
		else {
			Player player = new Player(username);
			player.setGameServerThread(gameServerThread);
			gameServerThread.setUsername(username);
			game.addPlayer(player);
			
			int numPlayersLeft = 3 - game.getPlayers().size();
			if(numPlayersLeft == 1) {
				Player firstPlayer = game.getPlayers().get(0);
				GameServerThread firstGameServerThread = null;
				for(int i = 0; i < gameServerThreads.size(); i++) {
					GameServerThread tempGameServerThread = gameServerThreads.get(i);
					String tempGameServerThreadUsername = tempGameServerThread.getUsername();
					String firstPlayerUsername = firstPlayer.getUsername();
					if(tempGameServerThreadUsername.equals(firstPlayerUsername)) {
						firstGameServerThread = tempGameServerThread;
						break;
					}
				}
				String firstResponse = "Waiting for 1 other player to join...";
				GameMessage firstResponseMessage = new GameMessage(GameMessage.Type.JOIN_PLAYERS_LEFT, firstResponse);
				if(firstResponseMessage != null) {
					firstGameServerThread.sendMessage(firstResponseMessage);
				}
				String response = "The game will start shortly. Waiting for other players to join...";
				GameMessage responseMessage = new GameMessage(GameMessage.Type.START_WAITING, response);
				gameServerThread.sendMessage(responseMessage);
			}
			else if(numPlayersLeft == 0) {
				String allResponse = "Let the game commence. Good luck to all players!";
				GameMessage allResponseMessage= new GameMessage(GameMessage.Type.JOIN_ALL_PLAYERS, allResponse);
				broadcast(allResponseMessage, gameServerThread);
			}
		}
	}
	
	public void broadcast(GameMessage responseMessage, GameServerThread responseGameServerThread) {
		String responseGamename = responseGameServerThread.getGamename();
		Game responseGame = gameMap.get(responseGamename);
		if(responseMessage != null) {
			for(int i = 0; i < gameServerThreads.size(); i++) {
				GameServerThread gameServerThread = gameServerThreads.get(i);
				String gamename = gameServerThread.getGamename();
				Game game = gameMap.get(gamename);
				if(gameServerThread != responseGameServerThread && game == responseGame) {
					gameServerThread.sendMessage(responseMessage);
				}
			}
		}
	}
	
	public void emit(GameMessage responseMessage, GameServerThread responseGameServerThread) {
		String responseGamename = responseGameServerThread.getGamename();
		Game responseGame = gameMap.get(responseGamename);
		if(responseMessage != null) {
			for(int i = 0; i < gameServerThreads.size(); i++) {
				GameServerThread gameServerThread = gameServerThreads.get(i);
				String gamename = gameServerThread.getGamename();
				Game game = gameMap.get(gamename);
				if(game == responseGame) {
					gameServerThread.sendMessage(responseMessage);
				}
			}
		}
	}

	
	public static void main(String [] args) {

		System.out.println("Welcome to the Black Jack Server!");
		Scanner scan = new Scanner(System.in);
		
		while(true) {
			try {
				System.out.println("Please enter a port.");
				int port = scan.nextInt();
				GameServer gameServer = new GameServer(port);
				break;
			} catch (IllegalArgumentException iae) {
				System.out.println("Invalid port number.");
				continue;
			} catch (IOException ioe) {
				System.out.println("Invalid port number.");
				continue;
			}
			catch (InputMismatchException ime) {
				scan.next();
				System.out.println("Invalid port number.");
				continue;
			}
		}

		scan.close();
		
	}
	
}