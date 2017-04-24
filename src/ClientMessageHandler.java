import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientMessageHandler extends Thread {
	
	private Socket socket = null;
	
	public ClientMessageHandler(Socket socket){
		this.socket = socket;
	}
	
	public void run() {
		try (
	            BufferedReader in = new BufferedReader(
	                new InputStreamReader(
	                    socket.getInputStream()));
	        ) {
	            String inputLine = in.readLine();

	            int sender;
	            int thisClient;
	            String message;
	            
	            //parse message
	            inputLine = inputLine.substring(inputLine.indexOf(":") + 1); //ditch "From:"
	            sender = Integer.parseInt(inputLine.substring(0, 1));
	            inputLine = inputLine.substring(inputLine.indexOf(":") + 1); //ditch "To:"
	            thisClient = Integer.parseInt(inputLine.substring(0, 1));
	            inputLine = inputLine.substring(inputLine.indexOf(":") + 1); //ditch "Message:"
	            message = inputLine;
	            
	            //output to console for now - TODO make GUI later?
	            StringBuilder sb = new StringBuilder();
	            sb.append("\n(Message Received: Process");
	            sb.append(sender);
	            sb.append("->Process");
	            sb.append(thisClient);
	            sb.append(": ");
	            sb.append(message);
	            sb.append(")\n>");
	            System.out.print(sb.toString());
	            
	            socket.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
}
