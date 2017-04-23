import java.io.IOException;
import java.net.ServerSocket;

public class ClientListener extends Thread {
	
	protected int myPort;
	
	public ClientListener(int myPort) {
		this.myPort = myPort;
	}
	
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(myPort)) { 
            while (true) {
	            new ClientMessageHandler(serverSocket.accept()).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + myPort);
            System.exit(-1);
        }
	}
}
