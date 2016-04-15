package json.node;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import json.enums.JsonTags;
import json.enums.SubscriptionPolicy;

/**
 * JsonCommunication is interface for communication between 
 * JsonRealTimeDiasApp peerlet and JsonServerParser. An instance of this interface should be passed to the JsonServerParser.
 * 
 * @author nikolijo
 *
 */
public interface CommunicationListener {
	
	/**
	 * Setting new list of possible states with initial state. This should be the first
	 * message a node receives from the client.
	 * 
	 * @param list - list of possible states
	 * @param initState - initial state
	 */
	public void setPossibleStates(ArrayList<Double> list, double initState);
	
	/**
	 * Setting new selected state
	 * @param state - new selected state
	 */
	public void setSelectedState(double state);
	
	/**
	 * Returning the values of all aggregate functions back to the client
	 * @return null if bootstrapping period is not finished, non-empty HashMap otherwise
	 */
	public HashMap<JsonTags, Double> getAllAggregates();	
	
	/**
	 * Returns aggregate that corresponds to the tag
	 * @param tag - enum that matches some of the aggregation functions
	 * @return null if bootstrapping period is not finished, double value otherwise
	 */
	public Double getAggregate(JsonTags tag);
	
	/**
	 * Schedules creating an aggregate message as response to request message at the end of an epoch.
	 * @param tags - list of requested values of aggregation functions
	 * @param clientIP - client IP address
	 */
	public void scheduleAnswer(JsonTags[] tags, String clientIP);
	
	public void subscribe(String IP, SubscriptionPolicy policy, int[] timeMillis, JsonTags[] aggregationFunctions);
	
	public void unsubscribe(String IP, JsonTags[] aggregationFunctions);

}
