import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;

public class Sender extends Thread {
	
	protected static LinkedBlockingDeque<String> messageQ;
	protected ArrayList<Integer> clients;
	
	public Sender(LinkedBlockingDeque<String> q, ArrayList<Integer> clients) {
		messageQ = q;
		this.clients = clients;
	}
	
	public void run() {
		
		Scanner in = new Scanner(System.in);
		
		while (true) {
			
			
			String message = "";
			try {
				message = messageQ.take(); //blocking call
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			System.out.println("====================================");
			System.out.println("There is a message in the queue:\n");
			StringBuilder sb = new StringBuilder();
			sb.append(message);
			sb.append("\n");
			System.out.println(sb.toString());
			System.out.println("What would you like to do?");
			System.out.println("\t1: Send this message unchanged.");
			System.out.println("\t2: Change this message's contents and send it.");
			System.out.println("\t3: Delete this message without sending.");
			System.out.println("\t4: Defer this message to the end of the queue.");
			System.out.println("\t5: Reprint prompt.");
			System.out.print("\t6: Exit system.\n>");
			
			int userChoice = 0;
			
			userChoice = in.nextInt();
			in.nextLine();
			
			
			while (userChoice <= 0 || userChoice > 6) {
				System.out.println("Please provide a valid input.");
				System.out.println("What would you like to do?");
				System.out.println("\t1: Send this message unchanged.");
				System.out.println("\t2: Change this message's contents and send it.");
				System.out.println("\t3: Delete this message without sending.");
				System.out.println("\t4: Defer this message to the end of the queue.");
				System.out.println("\t5: Reprint prompt.");
				System.out.print("\t6: Exit system.\n>");
				userChoice = in.nextInt();
				in.nextLine();
			}
			
			String hostname = "localhost";
			int target = -1;
			
			switch(userChoice) {
			case 1:
				target = Integer.parseInt(message.substring(message.indexOf("To:") + 3, message.indexOf("To:") + 4));
				
				
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
				System.out.print("What would you like the message contents to be?\n>");
				String newMessage = message.substring(0, message.indexOf("Message:") + 8);

				
				String userMessage = in.nextLine();
				
				StringBuilder newMessageSB = new StringBuilder();
				newMessageSB.append(newMessage);
				newMessageSB.append(userMessage);
				newMessage = newMessageSB.toString();
				
				target = Integer.parseInt(message.substring(message.indexOf("To:") + 3, message.indexOf("To:") + 4));
				
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
			case 5:
				messageQ.addFirst(message);
				break;
			case 6:
				System.exit(0);
				break;
			}
			//in.close();
		}
	}
}
