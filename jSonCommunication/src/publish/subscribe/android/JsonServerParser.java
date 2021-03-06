package publish.subscribe.android;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

import json.enums.JsonTags;
import json.enums.MessageTypes;
import json.enums.SubscriptionPolicy;
import json.enums.Utility;
import json.messages.AggregateMessage;
import json.messages.BaseMessage;
import json.messages.PossibleStatesMessage;
import json.messages.RequestMessage;
import json.messages.SelectedStateMessage;
import json.messages.SubscriptionMessage;
import json.messages.UnsubscriptionMessage;
import json.node.CommunicationListener;
import json.parser.JsonParserInterface;
import json.server.JsonServerInterface;
import json.server.JsonServerSide;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonServerParser implements JsonServerParserInterface{
	
	private ArrayList<CommunicationListener> listeners;
	private ServerInterface server;
	
	
	public JsonServerParser(ServerInterface server) {
		this.server = server;
		this.listeners = new ArrayList<CommunicationListener>();
	}
	
	
	@Override
	public void receiveJson(String json) {
		
		String clientIP = "ExampleClientIP"; // TODO, this should be in function arguments
		
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println("Received JSON");
		
		try {
			JsonNode node = objectMapper.readValue(json,JsonNode.class);
			
			JsonNode typeNode = node.get("type");
			String type = typeNode.asText();
			System.out.println("TYPE IS: " + type);
			MessageTypes mType = Utility.string2MessageTypes(type);
			
			switch(mType) {
			case SELECTED_STATE_MSG:
				SelectedStateMessage ssm = objectMapper.readValue(json, SelectedStateMessage.class);
				
				if(this.check(ssm, clientIP)) {
					System.out.println(ssm.toString());
					processSSM(ssm);
				} else {
					System.err.println("Checking failed!");
				}				
				break;
			case POSSIBLE_STATES_MSG:
				PossibleStatesMessage psm = objectMapper.readValue(json, PossibleStatesMessage.class);
				if(this.check(psm, clientIP)) {
					System.out.println(psm.toString());
					processPSM(psm);
				} else {
					System.err.println("Checking failed!");
				}
				break;
			case AGGREGATE_MSG:
				// ERROR
				// node can never receive this type of message
				// node can only produce this message and send it back to client
				System.err.println("Node cannot receive message of type " + MessageTypes.AGGREGATE_MSG + " from client. This message will be ignored!");
				break;
			case SUBSCRIPTION_MSG:
				SubscriptionMessage sm = objectMapper.readValue(json, SubscriptionMessage.class);
				if(this.check(sm, clientIP)) {
					processSM(sm);
				}				
				break;
			case UNSUBSCRIPTION_MSG:
				UnsubscriptionMessage um = objectMapper.readValue(json, UnsubscriptionMessage.class);
				if(this.check(um, clientIP)) {
					processUM(um);
				}				
				break;
			case REQUEST_MSG:
				RequestMessage rm = objectMapper.readValue(json, RequestMessage.class);
				if(this.check(rm, clientIP)) {
					processRM(rm);
				}				
				break;
			default:
				// some other kind of message	
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean check(BaseMessage msg, String clientIP) {
		if (true) return true; // TODO, remove this line when clientIP is set
		boolean flag = true;
		System.out.println("msg.getDstIP() = " + msg.getDstIP() + " this.server.getIP() = " + this.server.getIP() +
							" msg.getDstID() = " + msg.getDstID() + " this.server.getID() = " + this.server.getID() +
							" msg.getSrcIP() = " + msg.getSrcIP() + " clientIP = " + clientIP);
		if(!msg.getDstIP().equals(this.server.getIP()) || 
				!msg.getDstID().equals(this.server.getID()) ||
				!msg.getSrcIP().equals(clientIP)) {
			flag = false;
		}
		
		return flag;
	}
	
	public ServerInterface getServer() {
		return this.server;
	}
	
	
	@Override
	public void addListener(CommunicationListener listener) {
		if(this.listeners == null) {
			this.listeners = new ArrayList<CommunicationListener>();
		}
		this.listeners.add(listener);
	}
	
	@Override
	public ArrayList<CommunicationListener> getListeners() {
		return this.listeners;
	}
	
	private void onPossibleStatesChanged(ArrayList<Double> states, double initState) {
		for(CommunicationListener listener : this.getListeners()) {
			listener.setPossibleStates(states, initState);
		}
	}
	
	private void onSelectedStateChanged(double selectedState) {
		for(CommunicationListener listener : this.getListeners()) {
			listener.setSelectedState(selectedState);
		}
	}
	
	private void onRequestReceived(JsonTags[] tags, String clientIP) {
		for(CommunicationListener listener : this.getListeners()) {
			//listener.scheduleAnswer(tags, clientIP);
			this.answer(listener.getAllAggregates(), tags, clientIP);
		}
	}
	
	private void onSubscriptionReceived(String IP, SubscriptionPolicy policy, JsonTags[] functions, int[] timeMillis) {
		for(CommunicationListener listener : this.getListeners()) {
			listener.subscribe(IP, policy, timeMillis, functions);
		}
	}
	
	private void onUnsubscriptionReceived(String IP, JsonTags[] functions) {
		for(CommunicationListener listener : this.getListeners()) {
			listener.unsubscribe(IP, functions);
		}
	}
	
	
	
	/**
	 * Processes Selected State Messages and invokes corresponding function
	 * in the aggregation node.
	 * @param ssm - SelectedStateMessage object
	 * @param clientIP - client's IP address (not used for now)
	 */
	public void processSSM(SelectedStateMessage ssm) {
		if(ssm != null) {
			double newState = ssm.selectedState;
			this.onSelectedStateChanged(newState);
		}		
	}
	
	/**
	 * Processes Possible States Messages and invokes corresponding function
	 * in the aggregation node.
	 * @param psm - PossibleStatesMessage object
	 * @param clientIP - client's IP address (not used for now)
	 */
	public void processPSM(PossibleStatesMessage psm) {
		if(psm != null && psm.possibleStates != null) {
			double[] possibleStates = psm.possibleStates;
			if(psm.possibleStates.length == 0) {
				this.onPossibleStatesChanged(null, psm.initState);
			} else {
				ArrayList<Double> list = new ArrayList<Double>();
				for(int i = 0; i < possibleStates.length; i++) {
					list.add(possibleStates[i]);
				}
				double initState = psm.initState;
				this.onPossibleStatesChanged(list, initState);
			}			
		} else if(psm.possibleStates == null) {
			this.onPossibleStatesChanged(null, 0);
		}
	}
	
	/**
	 * Processes request Message and invokes corresponding function in
	 * the aggregation node. It however, does NOT provide the answer to this request
	 * @param rm - RequestMessage object
	 */
	public void processRM(RequestMessage rm) {
		if(rm.aggregationFunctions != null) {			
			JsonTags[] tags = new JsonTags[rm.aggregationFunctions.length];
			for(int i = 0; i < rm.aggregationFunctions.length; i++) {
				tags[i] = Utility.stringToAggTag(rm.aggregationFunctions[i]);
			}
			this.onRequestReceived(tags, rm.getSrcIP());			
		} else {
			System.err.println("Request message has empty request array!");
		}		
	}
	
	/**
	 * Processes subscription message and invokes corresponding function in
	 * the aggregation node.
	 * @param sm - subscription message
	 */
	public void processSM(SubscriptionMessage sm) {
		if(sm != null) {
			JsonTags[] tags = new JsonTags[sm.aggregationFunctions.length];
			for(int i = 0; i < tags.length; i++) {
				JsonTags tag = Utility.stringToAggTag(sm.aggregationFunctions[i]);
				tags[i] = tag;
			}
			this.onSubscriptionReceived(sm.getSrcIP(), sm.subscriptionPolicy, tags, sm.millisTime);
		}
	}
	
	/**
	 * Processes unsubscription message and invokes corresponding function in
	 * the aggregation node.
	 * @param um - unsubscription message
	 */
	public void processUM(UnsubscriptionMessage um) {
		if(um != null) {
			JsonTags[] tags = new JsonTags[um.aggregationFunctions.length];
			for(int i = 0; i < tags.length; i++) {
				JsonTags tag = Utility.stringToAggTag(um.aggregationFunctions[i]);
				tags[i] = tag;
			}
			this.onUnsubscriptionReceived(um.getSrcIP(), tags);
		}
	}
	
	
	
	/**
	 * Provides an answer to client's request message. Creates an Aggregate Message object
	 * and parses it into json string that sends to server object.
	 * @param hmap - HashMap that matches jsonTags of aggregation functions to their values
	 * @param tags - tags of aggregation functions that client requested
	 * @param clientIP - client's IP address
	 */
	public void answer(HashMap<JsonTags, Double> hmap, JsonTags[] tags, String clientIP) {
		AggregateMessage am = new AggregateMessage(this.server.getIP().toString(), clientIP.toString(), this.server.getID(), null);
		
		am.aggregationFunction = new String[tags.length];
		am.value = new double[tags.length];
		for(int i = 0; i < tags.length; i++) {
			am.aggregationFunction[i] = Utility.aggTagToString(tags[i]);
			am.value[i] = hmap.get(tags[i]);
		}
		ObjectMapper om = new ObjectMapper();
		String json = null;
		try {
			json = om.writeValueAsString(am);
			getServer().sendJson(json, clientIP);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
	}

}
