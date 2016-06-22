package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import json.messages.PossibleStatesMessage;
import json.messages.SelectedStateMessage;
import json.node.CommunicationListener;
import json.parser.JsonParserInterface;
import json.parser.JsonServerParser;

/**
 * This class is created only for testing purposes!
 * Instead of writing to files, it should communicate with server through sockets!
 * @author ales_omerzel
 *
 */
public class JsonClientSide implements JsonClientInterface{
	
	private JsonParserInterface myParser;
	private String myIPaddress;
	private String myID;
	private double[] possibleStates;
	private double selectedState;
	
	public JsonClientSide(String myIPaddress, String myID) {
		this.myIPaddress = myIPaddress;
		this.myID = myID;
		this.start();
	}	
	
	public void setPossibleStates(double[] possibleStates){
		this.possibleStates = possibleStates;
	}
	
	public void setSelectedState(double selectedState){
		this.selectedState = selectedState;
	}
	
	public void start() {
		// TODO: Get server IP and ID
		String tmpServerIP = "localhost";
		String tmpServerID = "100";
		this.sendPossibleStates(tmpServerIP, tmpServerID);
		this.sendSelectedState(tmpServerIP, tmpServerID);
	}
	
	public void sendPossibleStates(String serverIP, String serverID) {
		PossibleStatesMessage psm = new PossibleStatesMessage(myIPaddress, serverIP, myID, serverID);
		psm.possibleStates = this.possibleStates;
		String json = psm.toJson();
		sendJson(json, serverIP, serverID);
	}
	
	public void sendSelectedState(String serverIP, String serverID){
		SelectedStateMessage ssm = new SelectedStateMessage(myIPaddress, serverIP, myID, serverID);
		ssm.selectedState = this.selectedState;
		String json = ssm.toJson();
		sendJson(json, serverIP, serverID);		
	}


	@Override
	public void sendJson(String json, String serverIP, String serverID) {
		//TODO select right serverID
		int portNumber = 4000;
		sendMessage(json, serverIP, portNumber);
	}
	
	public void sendMessage(String message, String serverName, int portNumber){
		ClientCommunicator serverCom = new ClientCommunicator(serverName, portNumber);
		serverCom.write(message);
	}
	

	public void writeToFile(String fileName, String json) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
			out.println(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

	@Override
	public String getIP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
