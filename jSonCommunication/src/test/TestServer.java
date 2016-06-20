package test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.JsonClientSide;
import json.messages.PossibleStatesMessage;
import json.messages.SelectedStateMessage;
import json.server.ServerCommunicator;

public class TestServer {

	public static void main(String[] args) {
		System.out.println("Test Server ...");
		int portNumber = 4000;
		ServerCommunicator serverCom = new ServerCommunicator(portNumber);
		String message = serverCom.listen();
		System.out.println(message);
		System.out.println("Success for Server!");
	}

}
