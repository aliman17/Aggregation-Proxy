package client;

import java.net.InetSocketAddress;

import json.node.CommunicationListener;

/**
 * Client side should implement this interface. It only provides
 * sendJson method for now that JsonClientParser uses to send answer to 
 * server's requests.
 * @author ales_omerzel
 *
 */
public interface JsonClientInterface {
	
	/**
	 * Sends json message to the server with serverIP address
	 * @param json - json string to be sent
	 * @param i
	 * @param clientIP - servers's IP address
	 */
	
	public void start();
	
	public String getIP();
	
	public String getID();
	
	public void sendPossibleStates(String serverIP, String serverID);

	public void sendSelectedState(String serverIP, String serverID);
	
	public void sendJson(String json, String serverIP, String serverID);
	
	// TODO: For now, we don't need to listen to the server but only 
	// send some file
	//public void addListener(CommunicationListener listener);

}
