import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.LinkedBlockingDeque;

public class Receiver extends Thread {
	
	protected int port;
	protected static LinkedBlockingDeque<String> messageQ;
	
	public Receiver(LinkedBlockingDeque<String> q) {
		port = 1108;
		messageQ = q;
	}
	
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(port)) { 
            while (true) {
	            new ReceiverThread(messageQ, serverSocket.accept()).start();
	        }
	    } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            System.exit(-1);
        }
	}
}
