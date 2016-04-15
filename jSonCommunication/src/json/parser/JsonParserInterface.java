package json.parser;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import json.node.CommunicationListener;

/**
 * JsonParserInterface is interface that server side uses to access the json parser.
 * 
 * @author nikolijo
 *
 */
public interface JsonParserInterface {
	
	
	public void receiveJson(String json, String clientIP);
	
	public void addListener(CommunicationListener listener);
	
	public ArrayList<CommunicationListener> getListeners();
}
