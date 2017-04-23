import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Queue;

public class ReceiverThread extends Thread {
	
	protected Socket socket;
	protected static Queue<String> messageQ;
	
	public ReceiverThread(Queue<String> q, Socket socket) {
		this.socket = socket;
		messageQ = q;
	}
	
	public void run() {
		try {

	            BufferedReader in = new BufferedReader(
		                new InputStreamReader(
		                    socket.getInputStream()));
	            
	            String inputLine = in.readLine();
	            messageQ.add(inputLine);
	            notifyAll();
	            
	            
	            in.close();
	            socket.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
}
