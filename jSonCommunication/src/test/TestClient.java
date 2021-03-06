package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.JsonClientSide;
import json.messages.PossibleStatesMessage;
import json.messages.SelectedStateMessage;

public class TestClient {

	public static void main(String[] args) {
		System.out.println("Test Client ...");
		// TODO Auto-generated method stub
		double[] psm = {0.1, 0.2, 0.3, 0.4, 0.5};
		double init = 0.1;
		
		// Create client
		JsonClientSide client = new JsonClientSide("10:10:10:10", "7");
		client.setPossibleStates(psm);
		client.setSelectedState(init);
		
		// Send 
		String serverIP = "localhost";
		String serverID = "100";
		int portNumber = 4000;
		//client.sendPossibleStates(serverIP, serverID);
		//client.sendSelectedState(serverIP, serverID);
		
		//client.sendMessage("Test message", serverIP, portNumber);
		System.out.println("Success for Client!");
	}

}
