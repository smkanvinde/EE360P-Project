import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import javax.swing.*;

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
			try {
				sleep(2000); //allow time for threads to send messages
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Object[] arr = messageQ.toArray(); //rest of the queue - accessible by arr[i].toString();
			
			JFrame f = new JFrame();
			String data[][] = new String[arr.length][3];
			for (int i = 0; i < arr.length; i++) {
				for (int j = 0; j < 3; j++){
					String temp = arr[i].toString();
					if (j == 2){
						data[i][j] = temp.substring(temp.indexOf(":")+1);
					}
					else {
						data[i][j] = temp.substring(temp.indexOf(":")+1, temp.indexOf(":")+2);
						arr[i] = (Object)temp.substring(temp.indexOf(":") + 3);
					}
				}
			}
			
			String column[] = {"FROM", "TO", "MESSAGE"};
			JTable jt = new JTable(data, column);
			jt.setBounds(30,40,200,300);
			JScrollPane sp = new JScrollPane(jt);
			f.add(sp);
			
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			f.setLocation(dim.width/4-f.getSize().width/2, dim.height/2-f.getSize().height/2);
			
			f.setSize(300, 400);
			f.setVisible(true);
			
			
			JFrame controlPanel = new JFrame();
			StringBuilder prompt = new StringBuilder();
			prompt.append("Message at head of queue:\n");
			prompt.append("\t");
			prompt.append(message);
			prompt.append("\n");
			prompt.append("What would you like to do?\n");
			prompt.append("\t0: Refresh the queue display.\n");
			prompt.append("\t1: Send this message unchanged.\n");
			prompt.append("\t2: Change this message's contents and send it.\n");
			prompt.append("\t3: Delete this message without sending.\n");
			prompt.append("\t4: Defer this message to the end of the queue.\n");
			prompt.append("\t5: Exit system.\n");
			
			
			JTextArea area = new JTextArea(prompt.toString());
			area.setBounds(20,20,450,175);
			controlPanel.add(area);
			controlPanel.setSize(500, 200);
			controlPanel.setLayout(null);
			controlPanel.setVisible(true);
			
			int userChoice = -1;
			
			JFrame input = new JFrame();
			String foo = JOptionPane.showInputDialog(input, "Enter your choice here.");
			if (foo == null || foo.equals(""))
				userChoice = -1;
			else 
				try {
					userChoice = Integer.parseInt(foo);
				} catch (NumberFormatException e) {
					userChoice = 0;
				}
			while (userChoice < 0 || userChoice > 5){
				JOptionPane.showMessageDialog(input, "Error: You must enter a number [0-5].");
				foo = JOptionPane.showInputDialog(input, "Enter your choice here.");
				if (foo == null || foo.equals(""))
					userChoice = -1;
				else 
					try {
						userChoice = Integer.parseInt(foo);
					} catch (NumberFormatException e) {
						userChoice = -1;
					}
			}
			
			
			
			
			String hostname = "localhost";
			int target = -1;
			
			switch(userChoice) {
			case 0:
				messageQ.addFirst(message);
				//everything handled by reset
				break;
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
				String newMessage = message.substring(0, message.indexOf("Message:") + 8);
				String userMessage = JOptionPane.showInputDialog(input, "What would you like the message contents to be?");
				
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
				System.exit(0);
				break;				
			}
			f.setVisible(false);
			controlPanel.setVisible(false);
		}
	}
}
