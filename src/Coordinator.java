import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import javax.swing.*;

public class Coordinator {
	
	protected static LinkedBlockingDeque<String> messageQ = new LinkedBlockingDeque<String>();
	
	
	public static void main(String[] args) {
		int serverPort = 1108; //TODO this is the server port
		
		JFrame f = new JFrame();
		String num = JOptionPane.showInputDialog(f, "How many Clients should I start?");
		int numClients;
		if (num == null || num.equals(""))
			numClients = 0;
		else 
			try {
				numClients = Integer.parseInt(num);
			} catch (NumberFormatException e) {
				numClients = 0;
			}
		while (numClients <= 0){
			JOptionPane.showMessageDialog(f, "Error: You must enter a positive number of Clients to start.");
			num = JOptionPane.showInputDialog(f, "How many clients should I start?");
			if (num == null || num.equals(""))
				numClients = 0;
			else 
				try {
					numClients = Integer.parseInt(num);
				} catch (NumberFormatException e) {
					numClients = 0;
				}
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
		
	}
}
