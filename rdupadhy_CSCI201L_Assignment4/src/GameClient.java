import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GameClient extends Thread {

	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private GameMessage.Type type;

	private GameClient(String hostname, int port) throws UnknownHostException, IOException {
		Socket socket = new Socket(hostname, port);
		ois = new ObjectInputStream(socket.getInputStream());
		oos = new ObjectOutputStream(socket.getOutputStream());
		
		this.start();
		Scanner scanner = new Scanner(System.in);
		try {
			while (true) {
				String response = scanner.nextLine();
				GameMessage responseMessage = new GameMessage(type, response);
				oos.writeObject(responseMessage);
				oos.flush();
			}
		} catch (IOException ioe) {
			System.out.println("ioe in GameClient constructor: " + ioe.getMessage());
		}
	}

	public void run() {
		try {
			while(true) {
				GameMessage receivedMessage = (GameMessage)ois.readObject();
				type = receivedMessage.getType();
				System.out.println(receivedMessage.getMessage());
			}			
		} catch (IOException ioe) {
			System.out.println("ioe in GameClient.run(): " + ioe.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe in GameClient.run(): " + cnfe.getMessage());
		}
	}

	public static void main(String [] args) {

		System.out.println("Welcome to Black Jack!");
		Scanner scan = new Scanner(System.in);

		while(true) {
			try {
				System.out.println("Please enter the ipaddress");
				String hostname = scan.nextLine();
				System.out.println("Please enter the port");
				int port = scan.nextInt();
				//scan.nextLine();
				GameClient gameClient = new GameClient(hostname, port);
				break;
			} catch (UnknownHostException ue) {
				System.out.println("Unable to connect to server with provided fields");
				continue;
			} catch (IOException ioe) {
				System.out.println("Unable to connect to server with provided fields");
				continue;
			} catch (InputMismatchException ime) {
				scan.next();
				System.out.println("Unable to connect to server with provided fields");
				continue;
			}
		}
				
		scan.close();

	}

}
