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
private Map<GameServerThread, Integer> numPlayersMap;
	
	public GameServer(int port) throws IllegalArgumentException, IOException {
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Successfully started the Black Jack server on port " + port);
		gameServerThreads = new Vector<GameServerThread>();
		gameMap = new HashMap<String, Game>();
		numPlayersMap = new HashMap<GameServerThread, Integer>();
		
		while(true) {
			Socket socket = serverSocket.accept();
			GameServerThread gameServerThread = new GameServerThread(socket, this);
			gameServerThreads.add(gameServerThread);
			responseMenu(null, gameServerThread);
		}
	
	}
	
	public void responseMenu(GameMessage receivedMessage, GameServerThread gameServerThread) {
		if(receivedMessage == null) {
			startOrJoinInitialMenu(receivedMessage, gameServerThread);
		}
		else if(receivedMessage.getType() == GameMessage.Type.START_OR_JOIN_INITIAL) {
			startOrJoinOptionsMenu(receivedMessage, gameServerThread);
		}
		else if(receivedMessage.getType() == GameMessage.Type.START_PLAYERS) {
			startGamePlayersMenu(receivedMessage, gameServerThread);
		}
		else if(receivedMessage.getType() == GameMessage.Type.START_GAMENAME) {
			startGameNameMenu(receivedMessage, gameServerThread);
		}
		else if(receivedMessage.getType() == GameMessage.Type.START_USERNAME) {
			startUserNameMenu(receivedMessage, gameServerThread);
		}
		else if(receivedMessage.getType() == GameMessage.Type.JOIN_GAMENAME) {
			joinGameNameMenu(receivedMessage, gameServerThread);
		}
		else if(receivedMessage.getType() == GameMessage.Type.JOIN_USERNAME) {
			joinUserNameMenu(receivedMessage, gameServerThread);
		}
		else if(receivedMessage.getType() == GameMessage.Type.ASK_BET_PLAYER) {
			betPlayerMenu(receivedMessage, gameServerThread);
		}
		else if(receivedMessage.getType() == GameMessage.Type.STAY_OR_HIT_PLAYER) {
			stayOrHitMenu(receivedMessage, gameServerThread);
		}
	}
	
	public void startOrJoinInitialMenu(GameMessage receivedMessage, GameServerThread gameServerThread) {
		String response = "Please chooose from the options below\n1) Start Game\n2) Join Game";
		GameMessage responseMessage = new GameMessage(GameMessage.Type.START_OR_JOIN_INITIAL, response);
		gameServerThread.sendMessage(responseMessage);
	}
	
	public void startOrJoinOptionsMenu(GameMessage receivedMessage, GameServerThread gameServerThread) {
		if(receivedMessage.getMessage().equals("1")) {
			String response = "Please chooose the number of players in the game";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.START_PLAYERS, response);
			gameServerThread.sendMessage(responseMessage);
		} 
		else if(receivedMessage.getMessage().equals("2")) {
			String response = "Please enter the name of the game you wish to join";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.JOIN_GAMENAME, response);
			gameServerThread.sendMessage(responseMessage);
		}
	}
	
	public void startGamePlayersMenu(GameMessage receivedMessage, GameServerThread gameServerThread) {
		if(receivedMessage.getMessage().equals("1") || receivedMessage.getMessage().equals("2") || receivedMessage.getMessage().equals("3")) {
			int numPlayers = Integer.parseInt(receivedMessage.getMessage());
			numPlayersMap.put(gameServerThread, numPlayers);
			String response = "Please chooose a name for your game";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.START_GAMENAME, response);
			gameServerThread.sendMessage(responseMessage);
		}
	}
	
	public void startGameNameMenu(GameMessage receivedMessage, GameServerThread gameServerThread) {
		if(gameMap.containsKey(receivedMessage.getMessage())) {
			String response = "Invalid choice. This game name has already been chosen by another user\nPlease chooose a name for your game";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.START_GAMENAME, response);
			gameServerThread.sendMessage(responseMessage);
		}
		else {
			String gamename = receivedMessage.getMessage();
			int numPlayers = numPlayersMap.get(gameServerThread);
			Game game = new Game(gamename, numPlayers);
			gameMap.put(gamename, game);
			gameServerThread.setGamename(gamename);
			String response = "Please choose a username";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.START_USERNAME, response);
			gameServerThread.sendMessage(responseMessage);
		}
	}
	
	public void startUserNameMenu(GameMessage receivedMessage, GameServerThread gameServerThread) {
		String username = receivedMessage.getMessage();
		String gamename = gameServerThread.getGamename();
		Game game = gameMap.get(gamename);
		Player player = new Player(username);
		player.setGameServerThread(gameServerThread);
		gameServerThread.setUsername(username);
		game.addPlayer(player);
		int numPlayers = game.getNumPlayers();
		int newNumPlayersLeft = numPlayers - game.getPlayers().size();
		if(newNumPlayersLeft >= 1) {
			String response = "";
			if(newNumPlayersLeft == 1) {
				response = "Waiting for " + newNumPlayersLeft + " other player to join...";
			}
			else {
				response = "Waiting for " + newNumPlayersLeft + " other players to join...";
			}
			GameMessage responseMessage = new GameMessage(GameMessage.Type.START_WAITING, response);
			gameServerThread.sendMessage(responseMessage);
		}
		else {
			String allStartResponse = "Let the game commence. Good luck to all players!";
			GameMessage allStartResponseMessage= new GameMessage(GameMessage.Type.JOIN_ALL_PLAYERS, allStartResponse);
			emit(allStartResponseMessage, gameServerThread);
			
			String allDealerResponse = "Dealer is shuffling cards...";
			game.shuffleDeck();
			GameMessage allDealerResponseMessage= new GameMessage(GameMessage.Type.JOIN_ALL_PLAYERS, allDealerResponse);
			emit(allDealerResponseMessage, gameServerThread);
			
			Player player1 = game.getPlayers().firstElement();
			game.setCurrentPlayerIndex(0);
			String player1Username = player1.getUsername();
			int player1Chips = player1.getChips();
			String player1Response = player1Username + ", it is your turn to make a bet. Your chips total is " + player1Chips;
			GameMessage player1ResponseMessage = new GameMessage(GameMessage.Type.ASK_BET_PLAYER, player1Response);
			GameServerThread player1GameServerThread = player1.getGameServerThread();
			player1GameServerThread.sendMessage(player1ResponseMessage);
			String remainingPlayersResponse = "It is " + player1Username + "'s turn to make a bet";
			GameMessage remainingPlayersResponseMessage = new GameMessage(GameMessage.Type.BET_OTHER, remainingPlayersResponse);
			broadcast(remainingPlayersResponseMessage, player1GameServerThread);
		}
	}
	
	public void joinGameNameMenu(GameMessage receivedMessage, GameServerThread gameServerThread) {
		if(gameMap.containsKey(receivedMessage.getMessage())) {
			String gamename = receivedMessage.getMessage();
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
	
	public void joinUserNameMenu(GameMessage receivedMessage, GameServerThread gameServerThread) {
		String username = receivedMessage.getMessage();
		String gamename = gameServerThread.getGamename();
		Game game = gameMap.get(gamename);
		if(game.usernameExists(username) == true) {
			String response = "Invalid choice. This username has already been chosen by another player in this game\nPlease choose a username";
			GameMessage responseMessage = new GameMessage(GameMessage.Type.JOIN_USERNAME, response);
			gameServerThread.sendMessage(responseMessage);
		}
		else {
			Player player = new Player(username);
			player.setGameServerThread(gameServerThread);
			gameServerThread.setUsername(username);
			game.addPlayer(player);
			
			int numPlayers = game.getNumPlayers();
			int newNumPlayersLeft = numPlayers - game.getPlayers().size();
			if(newNumPlayersLeft >= 1) {
				Player player1 = game.getPlayers().get(0);
				GameServerThread player1GameServerThread = player1.getGameServerThread();
				String player1Response = "";
				if(newNumPlayersLeft == 1) {
					player1Response = "Waiting for " + newNumPlayersLeft + " other player to join...";
				}
				else {
					player1Response = "Waiting for " + newNumPlayersLeft + " other players to join...";
				}
				GameMessage player1ResponseMessage = new GameMessage(GameMessage.Type.JOIN_PLAYERS_LEFT, player1Response);
				player1GameServerThread.sendMessage(player1ResponseMessage);

				String response = "The game will start shortly. Waiting for other players to join...";
				GameMessage responseMessage = new GameMessage(GameMessage.Type.START_WAITING, response);
				gameServerThread.sendMessage(responseMessage);
			}
			else if(newNumPlayersLeft == 0) {
				String allStartResponse = "Let the game commence. Good luck to all players!";
				GameMessage allStartResponseMessage= new GameMessage(GameMessage.Type.JOIN_ALL_PLAYERS, allStartResponse);
				emit(allStartResponseMessage, gameServerThread);
				
				String allDealerResponse = "Dealer is shuffling cards...";
				GameMessage allDealerResponseMessage= new GameMessage(GameMessage.Type.JOIN_ALL_PLAYERS, allDealerResponse);
				emit(allDealerResponseMessage, gameServerThread);
				
				Player player1 = game.getPlayers().firstElement();
				game.setCurrentPlayerIndex(0);
				String player1Username = player1.getUsername();
				int player1Chips = player1.getChips();
				String player1Response = player1Username + ", it is your turn to make a bet. Your chips total is " + player1Chips;
				GameMessage player1ResponseMessage = new GameMessage(GameMessage.Type.ASK_BET_PLAYER, player1Response);
				GameServerThread player1GameServerThread = player1.getGameServerThread();
				player1GameServerThread.sendMessage(player1ResponseMessage);
				String remainingPlayersResponse = "It is " + player1Username + "'s turn to make a bet.";
				GameMessage remainingPlayersResponseMessage = new GameMessage(GameMessage.Type.BET_OTHER, remainingPlayersResponse);
				broadcast(remainingPlayersResponseMessage, player1GameServerThread);
			}
		}
	}
	
	public void betPlayerMenu(GameMessage receivedMessage, GameServerThread gameServerThread) {
		String gamename = gameServerThread.getGamename();
		Game game = gameMap.get(gamename);
		int currentPlayerIndex = game.getCurrentPlayerIndex();
		Player currentPlayer = game.getPlayers().get(currentPlayerIndex);
		int currentPlayerBet = Integer.parseInt(receivedMessage.getMessage());
		currentPlayer.setCurrentBet(currentPlayerBet);
		String currentPlayerResponse = "You bet " + currentPlayerBet + " chips";
		GameMessage currentPlayerResponseMessage = new GameMessage(GameMessage.Type.RESPONSE_BET_PLAYER, currentPlayerResponse);
		gameServerThread.sendMessage(currentPlayerResponseMessage);
		
		String currentPlayerUsername = currentPlayer.getUsername();
		String allPlayersResponse = currentPlayerUsername + " bet " + currentPlayerBet + " chips";
		GameMessage allPlayersResponseMessage = new GameMessage(GameMessage.Type.RESPONSE_OTHER, allPlayersResponse);
		broadcast(allPlayersResponseMessage, gameServerThread);
		
		int numPlayers = game.getNumPlayers();
		int newPlayerIndex = (currentPlayerIndex + 1) % numPlayers;
		game.setCurrentPlayerIndex(newPlayerIndex);
		
		if(currentPlayerIndex != (numPlayers - 1)) {
			Player newPlayer = game.getPlayers().get(newPlayerIndex);
			GameServerThread newPlayerGameServerThread = newPlayer.getGameServerThread();
			String newPlayerUsername = newPlayer.getUsername();
			int newPlayerChips = newPlayer.getChips();
			String newPlayerResponse = newPlayerUsername + ", it is your turn to make a bet. Your chips total is " + newPlayerChips;
			GameMessage newPlayerResponseMessage = new GameMessage(GameMessage.Type.ASK_BET_PLAYER, newPlayerResponse);
			newPlayerGameServerThread.sendMessage(newPlayerResponseMessage);
			
			String remainingPlayersResponse = "It is " + newPlayerUsername + "'s turn to make a bet.";
			GameMessage remainingPlayersResponseMessage = new GameMessage(GameMessage.Type.BET_OTHER, remainingPlayersResponse);
			broadcast(remainingPlayersResponseMessage, newPlayerGameServerThread);
		} 
		else {
			Deck deck = game.getDeck();
			Vector<Card> dealerCards = deck.drawTwoCards();
			game.setDealerCards(dealerCards);
			String dealerUIs = dealerUI(game, true);
			
			String response = dividerString() + "\n" + dealerUIs + "\n";
			for(int i = 0; i < numPlayers; i++) {
				Player player = game.getPlayers().get(i);
				Vector<Card> playerCards = deck.drawTwoCards();
				player.setCards(playerCards);
				response += playerUI(player, true, false, false) + "\n";
			}
			response += dividerString();
			GameMessage responseMessage = new GameMessage(GameMessage.Type.UI, response);
			emit(responseMessage, gameServerThread);
			
			Player player1 = game.getPlayers().firstElement();
			String player1Username = player1.getUsername();
			System.out.println("currentPlayerIndex: " + currentPlayerIndex);
			String player1Response = "It is your turn to add cards to your hand\nEnter either '1' or 'stay' to stay. Enter either '2' or 'hit' to hit.";
			GameMessage player1ResponseMessage = new GameMessage(GameMessage.Type.STAY_OR_HIT_PLAYER, player1Response);
			GameServerThread player1GameServerThread = player1.getGameServerThread();
			player1GameServerThread.sendMessage(player1ResponseMessage);
			String remainingPlayersResponse = "It is " + player1Username + "'s turn to add cards to their card.";
			GameMessage remainingPlayersResponseMessage = new GameMessage(GameMessage.Type.STAY_OR_HIT_OTHER, remainingPlayersResponse);
			broadcast(remainingPlayersResponseMessage, player1GameServerThread);
		}
	}
	
	public void stayOrHitMenu(GameMessage receivedMessage, GameServerThread gameServerThread) {
		String stayOrHitString = receivedMessage.getMessage();
		String gamename = gameServerThread.getGamename();
		Game game = gameMap.get(gamename);
		int currentPlayerIndex = game.getCurrentPlayerIndex();
		Player currentPlayer = game.getPlayers().get(currentPlayerIndex);
		
		if(stayOrHitString.equals("1") || stayOrHitString.equals("stay")) {
			String currentPlayerResponse = "You stayed.";
			GameMessage currentPlayerResponseMessage = new GameMessage(GameMessage.Type.STAY_PLAYER, currentPlayerResponse);
			gameServerThread.sendMessage(currentPlayerResponseMessage);
			
			String currentPlayerUsername = currentPlayer.getUsername();
			String remainingPlayersResponse = currentPlayerUsername + " stayed.";
			GameMessage remainingPlayersResponseMessage = new GameMessage(GameMessage.Type.STAY_OTHER, remainingPlayersResponse);
			broadcast(remainingPlayersResponseMessage, gameServerThread);
			
			int currentPlayerCardsValue = currentPlayer.playerCardsValue();
			String allPlayersResponse = "";
			if(currentPlayerCardsValue == 21) {
				currentPlayer.setBlackjack(true);
				allPlayersResponse = playerUI(currentPlayer, false, false, true) + "\n" + dividerString();
			}
			else {
				allPlayersResponse = playerUI(currentPlayer, false, false, false) + "\n" + dividerString();
			}
			GameMessage allPlayersResponseMessage = new GameMessage(GameMessage.Type.STAY_OR_HIT_UI, allPlayersResponse);
			emit(allPlayersResponseMessage, gameServerThread);
			
			int numPlayers = game.getNumPlayers();
			int newPlayerIndex = (currentPlayerIndex + 1) % numPlayers;
			game.setCurrentPlayerIndex(newPlayerIndex);
			
			if(currentPlayerIndex == (numPlayers - 1)) {
				dealerPlayMenu(receivedMessage, gameServerThread);
			}
			else {
				Player newPlayer = game.getPlayers().get(newPlayerIndex);
				String newPlayerUsername = newPlayer.getUsername();
				String newPlayerResponse = "It is your turn to add cards to your hand\nEnter either '1' or 'stay' to stay. Enter either '2' or 'hit' to hit.";
				GameMessage newPlayerResponseMessage = new GameMessage(GameMessage.Type.STAY_OR_HIT_PLAYER, newPlayerResponse);
				GameServerThread newPlayerGameServerThread = newPlayer.getGameServerThread();
				newPlayerGameServerThread.sendMessage(newPlayerResponseMessage);
				remainingPlayersResponse = "It is " + newPlayerUsername + "'s turn to add cards to their card.";
				remainingPlayersResponseMessage = new GameMessage(GameMessage.Type.STAY_OR_HIT_OTHER, remainingPlayersResponse);
				broadcast(remainingPlayersResponseMessage, newPlayerGameServerThread);
			}
		}
		else if(stayOrHitString.equals("2") || stayOrHitString.equals("hit")) {
			Deck deck = game.getDeck();
			Vector<Card> drawnCards = deck.drawOneCard();
			Card card = drawnCards.firstElement();
			currentPlayer.addCards(drawnCards);
			String currentPlayerResponse = "You hit. You were dealt the " + card.getRank() + " of " + card.getSuit() + ".";
			GameMessage currentPlayerResponseMessage = new GameMessage(GameMessage.Type.HIT_NORMAL_PLAYER, currentPlayerResponse);
			gameServerThread.sendMessage(currentPlayerResponseMessage);
			
			String currentPlayerUsername = currentPlayer.getUsername();
			String remainingPlayersResponse = currentPlayerUsername + " hit. They were dealt the " + card.getRank() + " of " + card.getSuit() + ".";
			GameMessage remainingPlayersResponseMessage = new GameMessage(GameMessage.Type.HIT_NORMAL_OTHER, remainingPlayersResponse);
			broadcast(remainingPlayersResponseMessage, gameServerThread);
			
			int currentPlayerCardsValue = currentPlayer.playerCardsValue();
			int currentBet = currentPlayer.getCurrentBet();
			if(currentPlayerCardsValue > 21) {
				currentPlayerResponse = "You busted. You lose " + currentBet + " chips";
				currentPlayerResponseMessage = new GameMessage(GameMessage.Type.HIT_BUSTED_PLAYER, currentPlayerResponse);
				gameServerThread.sendMessage(currentPlayerResponseMessage);
				
				remainingPlayersResponse = currentPlayerUsername + " busted! You lose " + currentBet + " chips";
				remainingPlayersResponseMessage = new GameMessage(GameMessage.Type.HIT_BUSTED_OTHER, remainingPlayersResponse);
				broadcast(remainingPlayersResponseMessage, gameServerThread);
		
				String allPlayersResponse = "";
				currentPlayer.setBust(true);
				allPlayersResponse = playerUI(currentPlayer, false, true, false) + "\n" + dividerString();
				GameMessage allPlayersResponseMessage = new GameMessage(GameMessage.Type.STAY_OR_HIT_UI, allPlayersResponse);
				emit(allPlayersResponseMessage, gameServerThread);
				
				int numPlayers = game.getNumPlayers();
				int newPlayerIndex = (currentPlayerIndex + 1) % numPlayers;
				game.setCurrentPlayerIndex(newPlayerIndex);
				
				if(currentPlayerIndex == (numPlayers - 1)) {
					dealerPlayMenu(receivedMessage, gameServerThread);
				} 
				else {
					Player newPlayer = game.getPlayers().get(newPlayerIndex);
					String newPlayerUsername = newPlayer.getUsername();
					String newPlayerResponse = "It is your turn to add cards to your hand\nEnter either '1' or 'stay' to stay. Enter either '2' or 'hit' to hit.";
					GameMessage newPlayerResponseMessage = new GameMessage(GameMessage.Type.STAY_OR_HIT_PLAYER, newPlayerResponse);
					GameServerThread newPlayerGameServerThread = newPlayer.getGameServerThread();
					newPlayerGameServerThread.sendMessage(newPlayerResponseMessage);
					remainingPlayersResponse = "It is " + newPlayerUsername + "'s turn to add cards to their card.";
					remainingPlayersResponseMessage = new GameMessage(GameMessage.Type.STAY_OR_HIT_OTHER, remainingPlayersResponse);
					broadcast(remainingPlayersResponseMessage, newPlayerGameServerThread);
				}
			}
			else {
				String continueResponse = "Enter either '1' or 'stay' to stay. Enter either '2' or 'hit' to hit.";
				GameMessage continueResponseMessage = new GameMessage(GameMessage.Type.STAY_OR_HIT_PLAYER, continueResponse);
				gameServerThread.sendMessage(continueResponseMessage);
			}
		}
	}
	
	public void dealerPlayMenu(GameMessage receivedMessage, GameServerThread gameServerThread) {
		String allPlayersResponse = "It is now time for the dealer to play.";
		GameMessage allPlayersResponseMessage = new GameMessage(GameMessage.Type.DEALER_STAY_OR_HIT, allPlayersResponse);
		emit(allPlayersResponseMessage, gameServerThread); 
				
		String gamename = gameServerThread.getGamename();
		Game game = gameMap.get(gamename);
		Deck deck = game.getDeck();
		
		int dealerCardsValue = game.dealerCardsValue();
		Vector<Card> cardsDealt = new Vector<Card>();
		int timesHit = 0;
		while(dealerCardsValue < 17) {
			Vector<Card> drawnCards = deck.drawOneCard();
			Card card = drawnCards.firstElement();
			cardsDealt.add(card);
			game.addDealerCards(drawnCards);
			dealerCardsValue = game.dealerCardsValue();
			timesHit++;
		}
		if(timesHit == 0) {
			allPlayersResponse = "The dealer hit 0 times";
		}
		else if(timesHit == 1) {
			allPlayersResponse = "The dealer hit " + timesHit + " time. They were dealt: the " + cardsDealt.get(0).getRank() + " of " + cardsDealt.get(0).getSuit(); 
		}
		else {
			allPlayersResponse = "The dealer hit " + timesHit + " times. They were dealt:";
			for(int i = 0; i < cardsDealt.size(); i++) {
				Card cardDealt = cardsDealt.get(i);
				if(i == cardsDealt.size() - 1) {
					allPlayersResponse += " the " + cardDealt.getRank() + " of " + cardDealt.getSuit();
				}
				else {
					allPlayersResponse += " the " + cardDealt.getRank() + " of " + cardDealt.getSuit() + ",";
				}
			}
		}
		
		allPlayersResponseMessage = new GameMessage(GameMessage.Type.DEALER_STAY_OR_HIT, allPlayersResponse);
		emit(allPlayersResponseMessage, gameServerThread);
		
		allPlayersResponse = dealerUI(game, false);
		allPlayersResponseMessage = new GameMessage(GameMessage.Type.DEALER_STAY_OR_HIT, allPlayersResponse);
		emit(allPlayersResponseMessage, gameServerThread);
		
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
	
	public String dealerUI(Game game, boolean deal) {
		if(deal) {
			String dealerString = "DEALER\n\n";
			String dealerCardsString = "Cards: | ";
			String returnString = "";
			Vector<Card> cards = game.getDealerCards();
			for(int i = 0; i < cards.size(); i++) {
				if(i == 0) {
					dealerCardsString += "? | ";
				}
				else {
					Card card = cards.get(i);
					dealerCardsString += card.getRank() + " of " + card.getSuit() + " | "; 
				}
			}
			returnString = dealerString + dealerCardsString;
			return returnString;
		}
		
		String dealerString = "DEALER\n\n";
		String dealerCardsString = "Cards: | ";
		String dealerStatusString = "";
		String returnString = "";
		boolean aceExists = false;
		
		Vector<Card> cards = game.getDealerCards();
		for(int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			if(card.getRank() == Card.Rank.ACE) {
				aceExists = true;
			}
			dealerCardsString += card.getRank() + " of " + card.getSuit() + " | ";
		}
		
		dealerStatusString = "Status: ";
		if(aceExists) {
			int value1 = 0;
			int value2 = 0;
			for(int i = 0; i < cards.size(); i++) {
				Card card = cards.get(i);
				if(card.getRank() == Card.Rank.ACE) {
					value1 += 1;
					value2 += 11;
				}
				else {
					value1 += card.value();
					value2 += card.value();
				}
			}
			dealerStatusString += value1 + " or " + value2;
		}
		else {
			int value = 0;
			for(int i = 0; i < cards.size(); i++) {
				Card card = cards.get(i);
				value += card.value();
			}
			dealerStatusString += value;
		}
		
		returnString = dividerString() + "\n" + dealerString + dealerStatusString + "\n" + dealerCardsString + "\n" + dividerString();
		return returnString;
	}
	
	public String playerUI(Player player, boolean deal, boolean bust, boolean blackjack) {
		String bufferString = "";
		if(deal) {
			bufferString = dividerString() + "\n" + dividerString() + "\n";
		}
		else {
			bufferString = dividerString() + "\n";
		}
		String playerSectionString = playerSection(player, bust, blackjack);
		String returnString = bufferString + playerSectionString;
		return returnString;
	}
	
	public String dividerString() {
		String dividerString = "";
		for(int i = 0; i <= 55; i++) {
			dividerString += "-";
		}
		return dividerString;
	}
	
	public String playerSection(Player player, boolean bust, boolean blackjack) {
		Vector<Card> cards = player.getCards();
		boolean aceExists = false;
		String username = player.getUsername();
		String usernameString = "Player: " + username + "\n";
		String playerStatusString = "";
		String playerCardsString = "";
		String playerTotalString = "";
		String returnString = "";
		
		playerCardsString = "Cards: | ";
		for(int i = 0; i < cards.size(); i++) {
			Card card = cards.get(i);
			if(card.getRank() == Card.Rank.ACE) {
				aceExists = true;
			}
			playerCardsString += card.getRank() + " of " + card.getSuit() + " | "; 
		}
		
		playerStatusString = "Status: ";
		if(aceExists) {
			int value1 = 0;
			int value2 = 0;
			for(int i = 0; i < cards.size(); i++) {
				Card card = cards.get(i);
				if(card.getRank() == Card.Rank.ACE) {
					value1 += 1;
					value2 += 11;
				}
				else {
					value1 += card.value();
					value2 += card.value();
				}
			}
			playerStatusString += value1 + " or " + value2;
			if(bust) {
				playerStatusString += " - bust ";
			}
			else if(blackjack) {
				playerStatusString += " - blackjack ";
			}
		}
		else {
			int value = 0;
			for(int i = 0; i < cards.size(); i++) {
				Card card = cards.get(i);
				value += card.value();
			}
			playerStatusString += value;
			if(bust) {
				playerStatusString += " - bust ";
			}
			else if(blackjack) {
				playerStatusString += " - blackjack ";
			}
		}
		
		int playerChips = player.getChips();
		int playerCurrentBet = player.getCurrentBet();
		playerTotalString = "Chip Total: " + playerChips + " | " + "Bet Amount: " + playerCurrentBet;
	
		returnString = usernameString + "\n" + playerStatusString + "\n" + playerCardsString + "\n" + playerTotalString;
	
		return returnString;
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