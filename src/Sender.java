import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

public class Sender extends Thread {
	
	protected static Queue<String> messageQ;
	protected ArrayList<Integer> clients;
	
	public Sender(Queue<String> q, ArrayList<Integer> clients) {
		messageQ = q;
		this.clients = clients;
	}
	
	public void run() {
		
		
		
		while (true) {
			Scanner in = new Scanner(System.in);
			if (messageQ.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			String message = messageQ.poll();
			
			System.out.println("There is a message in the queue:\n");
			System.out.println(message + "\n");
			System.out.println("What would you like to do?\n");
			System.out.println("\t1: Send this message unchanged.\n");
			System.out.println("\t2: Change this message's contents and send it.\n");
			System.out.println("\t3: Delete this message without sending.\n");
			System.out.println("\t4: Defer this message to the end of the queue.\n>");
			
			int userChoice = 0;
			
			userChoice = in.nextInt();
			
			
			while (userChoice <= 0 || userChoice > 4) {
				System.out.println("Please provide a valid input.");
				System.out.println("What would you like to do?\n");
				System.out.println("\t1: Send this message unchanged.\n");
				System.out.println("\t2: Change this message's contents and send it.\n");
				System.out.println("\t3: Delete this message without sending.\n");
				System.out.println("\t4: Defer this message to the end of the queue.\n>");
			}
			
			String hostname = "localhost";
			int target = -1;
			
			switch(userChoice) {
			case 1:
				target = Integer.parseInt(message.substring(message.indexOf("To:") + 5, message.indexOf("To:") + 6));
				
				
				try {
						Socket socket = new Socket(hostname, clients.get(target));
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						out.println(message);
						out.close();
						socket.close();
			        } catch (UnknownHostException e) {
			            System.err.println("Don't know about host " + hostname);
			            System.exit(1);
			        } catch (IOException e) {
			            System.err.println("Couldn't get I/O for the connection to " +
			                hostname);
			            System.exit(1);
			        }
				break;
			case 2:
				System.out.println("What would you like the message contents to be?\n>");
				String newMessage = message.substring(0, message.indexOf("Message:") + 9);
				String userMessage = "";
				
				userMessage = in.nextLine();
				
				newMessage = newMessage + userMessage;
				
				target = Integer.parseInt(message.substring(message.indexOf("To:") + 5, message.indexOf("To:") + 6));
				
				try {
						Socket socket = new Socket(hostname, clients.get(target));
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
						out.println(newMessage);
						out.close();
						socket.close();
			        } catch (UnknownHostException e) {
			            System.err.println("Don't know about host " + hostname);
			            System.exit(1);
			        } catch (IOException e) {
			            System.err.println("Couldn't get I/O for the connection to " +
			                hostname);
			            System.exit(1);
			        }
				
				break;
			case 3:
				message = new String();
				//do nothing
				break;
			case 4:
				messageQ.add(message);
				break;
			}
			in.close();
		}
	}
}
