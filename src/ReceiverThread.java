import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

public class ReceiverThread extends Thread {
	
	protected Socket socket;
	protected static LinkedBlockingDeque<String> messageQ;
	
	public ReceiverThread(LinkedBlockingDeque<String> q, Socket socket) {
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
	            
	            
	            in.close();
	            socket.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
}
