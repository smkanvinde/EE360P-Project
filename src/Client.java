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
	
	public int getNum() {return this.num;}
	public int getNumClients() {return this.numClients;}
	public int getMyPort() {return this.myPort;}
	
	public void run() {
		String hostName = "localhost";
		int portNumber = 1108; //this is the server port
		
		
		try {
	            
	            new ClientListener(this.getMyPort()).start(); //start a listener for incoming messages
	            sleep(1000); //wait a second for all clients to be started
	            
	            while (true){
		            Socket socket = new Socket(hostName, portNumber);
		            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		            
		            StringBuilder messageSB = new StringBuilder();
		            messageSB.append("hello ");
		            
	            	//String thisMessage = "hello world"; //TODO change this to suit functionality
	            	
	            	//send a message to a random other client
	            	int target = (int)(Math.random() * this.getNumClients());
	            	
	            	while (target == this.getNum()) { //don't send yourself a message
	            		target = (int)(Math.random() * this.getNumClients());
	            	}
	            	
	            	messageSB.append(target);
	            	messageSB.append(" this is ");
	            	messageSB.append(this.getNum());
	            	String thisMessage = messageSB.toString();
	            	
	            	//String message = "From:" + this.getNum() + "\nTo:" + target + "\nMessage:" + thisMessage;
	            	StringBuilder sb = new StringBuilder();
	            	sb.append("From:");
	            	sb.append(this.getNum());
	            	sb.append(" To:");
	            	sb.append(target);
	            	sb.append(" Message:");
	            	sb.append(thisMessage);
	            	out.println(sb.toString());
	            	out.close();
	            	socket.close();
	            	
	            	sleep(30000); //sleep 30s for human workability
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
