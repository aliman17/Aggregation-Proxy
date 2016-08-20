package json.messages;

import java.util.ArrayList;

import json.WriteJSON;
import json.enums.MessageTypes;
import state.PossibleStatePoint;

/**
 * Represents a message that includes list of possible states.
 * This should be the first message the client sends. 
 * 
 * @author nikolijo, ales_omerzel
 *
 */
public class PossibleStatesMessage extends BaseMessage {

	public String possibleStatesJson;
	public PossibleStatePoint initState;
	
	public PossibleStatesMessage() {
		super();
	}
	
	public PossibleStatesMessage(String srcIP, String dstIP, String srcID, String dstID) {
		super(srcIP, dstIP, srcID, dstID);
		this.type = MessageTypes.POSSIBLE_STATES_MSG;
	}
	
	public PossibleStatesMessage(String srcIP, String dstIP, String srcID, String dstID,
								 ArrayList<PossibleStatePoint> possibleStates, PossibleStatePoint initState) {
		super(srcIP, dstIP, srcID, dstID);
		this.type = MessageTypes.POSSIBLE_STATES_MSG;

		String[] possibleStatesString = new String[possibleStates.size()];
		for (int i = 0; i < possibleStatesString.length; i++){
			possibleStatesString[i] = WriteJSON.serialize(""+i, possibleStates.get(i).values);
		}
		this.possibleStatesJson = WriteJSON.serialize("possibleStates",possibleStatesString);

		this.initState = initState;
	}
	
	/*
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
	*/
}
