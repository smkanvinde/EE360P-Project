import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Coordinator {
	
	protected static Queue<String> messageQ = new LinkedList<String>();
	
	
	public static void main(String[] args) {
		int serverPort = 1108; //TODO this is the server port
		Scanner in = new Scanner(System.in);
		System.out.println("How many Clients should I start? >");
		int numClients = in.nextInt();
		while (numClients <= 0){
			System.out.println("You must input a positive number of threads to start.");
			System.out.println("How many Clients should I start? >");
			in.nextInt();
		}
		
		//setup list of clients
		ArrayList<Integer> clientPorts = new ArrayList<Integer>();
		for (int i = 0; i < numClients; i++) {
			clientPorts.add(serverPort + 1 + i); // 1109, 1110, 1111....
		}
		
		//spawn Receiver and Sender
		new Receiver(messageQ).start();
		new Sender(messageQ, clientPorts).start();
		
		//Start clients
		for (int i = 0; i < numClients; i++) {
			new Client(i, numClients, clientPorts.get(i)).start();
		}
		in.close();
	}
}
