package json.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import json.enums.MessageTypes;

/**
 * Represents a message that includes list of possible states.
 * This should be the first message the client sends. 
 * 
 * @author nikolijo, ales_omerzel
 *
 */
public class PossibleStatesMessage extends BaseMessage {
	
	public double[] possibleStates;
	public double initState;
	
	public PossibleStatesMessage() {
		super();
	}
	
	public PossibleStatesMessage(String srcIP, String dstIP, String srcID, String dstID) {
		super(srcIP, dstIP, srcID, dstID);
		this.type = MessageTypes.POSSIBLE_STATES_MSG;
	}
	
	public PossibleStatesMessage(String srcIP, String dstIP, String srcID, String dstID,
			double[] possibleStates, double initState) {
		super(srcIP, dstIP, srcID, dstID);
		this.type = MessageTypes.POSSIBLE_STATES_MSG;
		this.possibleStates = possibleStates;
		this.initState = initState;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("[\n");
		sb.append(super.toString());
		sb.append("\tpossible states = [ ");
		if(possibleStates != null) {
			for(int i = 0; i < possibleStates.length; i++) {
				sb.append(possibleStates[i]);
				if(i < possibleStates.length - 1) {
					sb.append(", ");
				}
			}
		}		
		sb.append(" ]\n");		
		sb.append("\tinit state = " + initState + "\n");
		sb.append("]");
		return sb.toString();
	}

}
