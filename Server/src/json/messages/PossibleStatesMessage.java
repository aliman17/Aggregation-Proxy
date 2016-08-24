package json.messages;

import java.util.ArrayList;
import java.util.Arrays;

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
	
	public String toString(){
		String str = this.srcIP + " " + this.srcID + " " + this.dstIP + " " + this.dstID + " ";
		str += "possibleStates:[";
		for(int i = 0; i < this.possibleStates.size(); i++){
			str += Arrays.toString((double[]) this.possibleStates.get(i));
			str += " ";
		}
		str += "] ";
		str += "initStates:" + Arrays.toString(this.initState);
		return str;
	}
	
}
