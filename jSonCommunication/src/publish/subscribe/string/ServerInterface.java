package publish.subscribe.string;

import java.net.InetSocketAddress;

import json.node.CommunicationListener;

/**
 * Server side should implement this interface. It only provides
 * sendJson method for now that JsonServerParser uses to send answer to 
 * clients' requests.
 * @author nikolijo
 *
 */
public interface ServerInterface {
	
	/**
	 * Sends json message to the client with clientIP address
	 * @param json - json string to be sent
	 * @param i
	 * @param clientIP - client's IP address
	 */
	public void sendJson(String json, String clientIP);
	
	public void start();
	
	public String getIP();
	
	public String getID();
	
	public void addListener(CommunicationListener listener);

}
