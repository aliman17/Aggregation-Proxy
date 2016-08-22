package json.messages;

import java.util.ArrayList;

import json.enums.MessageTypes;

/**
 * Represents a message that includes list of possible states.
 * This should be the first message the client sends. 
 * 
 * @author nikolijo, ales_omerzel
 *
 */
public class PossibleStatesMessage extends BaseMessage {
	
	public ArrayList possibleStates;
	public double[] initState;
	
	public PossibleStatesMessage(String srcIP, String dstIP, String srcID, String dstID,
			ArrayList possibleStates, double[] initState) {
		super(srcIP, dstIP, srcID, dstID);
		this.type = MessageTypes.POSSIBLE_STATES_MSG;
		this.possibleStates = possibleStates;
		this.initState = initState;
	}
	
}
