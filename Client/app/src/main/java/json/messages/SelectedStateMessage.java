package json.messages;

import json.enums.MessageTypes;
import state.PossibleStatePoint;

/**
 * Represents a message for setting new selected state
 * 
 * @author nikolijo
 *
 */
public class SelectedStateMessage extends BaseMessage{
	
	public PossibleStatePoint selectedState;
	
	public SelectedStateMessage() {
		super();
		this.type = MessageTypes.SELECTED_STATE_MSG;
	}
	
	public SelectedStateMessage(String srcIP, String dstIP, String srcID, String dstID) {
		super(srcIP, dstIP, srcID, dstID);
		this.type = MessageTypes.SELECTED_STATE_MSG;
	}
	
	public SelectedStateMessage(String srcIP, String dstIP, String srcID, String dstID,
			PossibleStatePoint selectedState) {
		super(srcIP, dstIP, srcID, dstID);
		this.type = MessageTypes.SELECTED_STATE_MSG;
		this.selectedState = selectedState;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("[\n");
		sb.append(super.toString());		
		sb.append("\tselected state = " + selectedState + "\n");
		sb.append("]");
		return sb.toString();
	}

}
