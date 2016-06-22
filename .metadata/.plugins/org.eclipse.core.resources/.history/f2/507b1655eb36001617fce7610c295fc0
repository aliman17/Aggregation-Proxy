package publish.subscribe.string;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import json.enums.MessageTypes;
import json.enums.Utility;
import json.messages.PossibleStatesMessage;
import json.messages.RequestMessage;
import json.messages.SelectedStateMessage;
import json.messages.SubscriptionMessage;
import json.messages.UnsubscriptionMessage;

public class JsonServerParser {
public void receiveJson(String json, String clientIP) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		
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
}
