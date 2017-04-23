import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	
	protected int num;
	protected int numClients;
	protected int myPort;
	
	public Client(int num, int numClients, int myPort) {
		this.num = num;
		this.numClients = numClients;
		this.myPort = myPort;
	}
	
	public void run() {
		String hostName = "localhost";
		int portNumber = 1108; //this is the server port
		
		
		try {
	            
	            new ClientListener(myPort).start(); //start a listener for incoming messages
	            sleep(1000); //wait a second for all clients to be started
	            
	            while (true){
		            Socket socket = new Socket(hostName, portNumber);
		            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		            
	            	String thisMessage = "hello world"; //TODO change this to suit functionality
	            	
	            	//send a message to a random other client
	            	int target = (int)(Math.random() * numClients);
	            	
	            	while (target == num) { //don't send yourself a message
	            		target = (int)(Math.random() * numClients);
	            	}
	            	
	            	String message = "From:" + num + "\nTo:" + target + "\nMessage:" + thisMessage;
	            	out.println(message);
	            	
	            	socket.close();
	            	sleep(30000); //sleep for human workability
	            }
	            	
	        } catch (UnknownHostException e) {
	            System.err.println("Don't know about host " + hostName);
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to " + hostName);
	            System.exit(1);
	        } catch (InterruptedException e){
	        	e.printStackTrace();
	        	System.exit(1);
	        }
	}
}
