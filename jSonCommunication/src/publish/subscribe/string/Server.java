package publish.subscribe.string;

import json.node.CommunicationListener;
import json.parser.JsonParserInterface;

public class Server implements ServerInterface{
	private AsyncReceiver asyncReceiver;
	private JsonParserInterface parser;
	private String myIPaddress;
	private String myID;
	
	public Server(){
		myID = "ExampleServerID";
		myIPaddress = "ExampleServerIP";
		asyncReceiver = new AsyncReceiver();
	}

	@Override
	public void sendJson(String json, String clientIP) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
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

	@Override
	public void addListener(CommunicationListener listener) {
		// TODO Auto-generated method stub
		
	}
}
