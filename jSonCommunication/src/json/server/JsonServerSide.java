package json.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import json.node.CommunicationListener;
import json.parser.JsonParserInterface;
import json.parser.JsonServerParser;

/**
 * This class is created only for testing purposes!
 * Instead reading from files, it should communicate with client through sockets!
 * @author nikolijo
 *
 */
public class JsonServerSide implements JsonServerInterface{
	
	private JsonParserInterface myParser;
	private String myIPaddress;
	private String myID;
	
	public JsonServerSide(String myIPaddress, String myID) {
		this.myIPaddress = myIPaddress;
		this.myID = myID;
		this.myParser = new JsonServerParser(this);
	}	
	
	@Override
	public void start() {
		this.readPossibleStates();
		this.readSelectedStates(4);
		this.readSelectedStates(2);
		this.readRequests();
	}
	
	@Override
	public void sendJson(String json, String clientIP) {
		writeToFile("jsonOutput/aggregates/node-0.json", json);
	}
	
	public void handleJson(String json, String clientIP) {
		myParser.receiveJson(json, clientIP);
	}	
	
	@Override
	public String getIP() {
		return this.myIPaddress;
	}

	@Override
	public String getID() {
		return this.myID;
	}

	@Override
	public void addListener(CommunicationListener listener) {
		this.myParser.addListener(listener);
	}	
	
	public void makeSubDirs(int iterator) {
		File f = new File("jsonOutput/aggregates/" + iterator + "/");
		f.mkdir();
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
	
	public void readFromFile(String fileName) {
		BufferedReader in = null;
		try {
			
			in = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line=in.readLine()) != null) {
				sb.append(line + "\n");
			}
			handleJson(sb.toString(), "/127.23.23.3:2323");
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public void readPossibleStates() {
		readFromFile("jsonOutput/possibleStates/node-0.json");
	}
	
	public void readSelectedStates(int i) {
		readFromFile("jsonOutput/selectedStates/" + i + "/node-0.json");
	}
	
	public void readRequests() {
		readFromFile("jsonOutput/requests/node-0.json");
	}
	
	public void readSubscriptions() {
		readFromFile("jsonOutput/subscriptions/node-0.json");
	}

}
