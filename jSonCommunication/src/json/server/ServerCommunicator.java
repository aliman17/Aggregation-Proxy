package json.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerCommunicator {
	private int portNumber;
	
	public ServerCommunicator(int portNumber){
		this.portNumber = portNumber;
	}
	
	public String listen(){
		String message = null;
		try {
			ServerSocket serverSocket = new ServerSocket(this.portNumber);
			Socket clientSocket = serverSocket.accept();
		    BufferedReader in = new BufferedReader(
		        new InputStreamReader(clientSocket.getInputStream()));
		    message = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
}
