package json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import json.enums.MessageTypes;
import json.messages.BaseMessage;
import json.messages.PossibleStatesMessage;

public class JsonParser {

	public BaseMessage parse(String jsonMessage) throws JSONException{
		
		JSONObject obj = new JSONObject( jsonMessage );
		String type = obj.getString("type");
		String srcIP = obj.getString("srcIP");
		String dstIP = obj.getString("dstIP");
		String srcID = obj.getString("srcID");
		String dstID = obj.getString("dstID");
		
		switch(type){
		case "POSSIBLE_STATES_MSG":
			ArrayList possibleStates = getPossibleStates(obj);
			double[] initState = getInitState(obj);
			return new PossibleStatesMessage(srcIP, dstIP, srcID, dstID,
				 possibleStates, initState);
			
		default:
			break;
		
		}
		return null;
	}
	
	
	private ArrayList getPossibleStates(JSONObject obj) throws JSONException{
		
		ArrayList possibleStates = new ArrayList();
		
		JSONArray arr = obj.getJSONArray("possibleStates");
		for (int i = 0; i < arr.length(); i++)
		{
		    JSONArray coordinates = arr.getJSONArray(i);
		    double[] coordinatesDouble = new double[coordinates.length()];
		    for (int j = 0; j < coordinates.length(); j++) {
		    	coordinatesDouble[j] = coordinates.getDouble(j);
		    }
		    possibleStates.add(coordinatesDouble);
		}

		return possibleStates;
		
	}
	
	private double[] getInitState(JSONObject obj) throws JSONException{
		
		JSONArray coordinates = obj.getJSONArray("initState");
		double[] coordinatesDouble = new double[coordinates.length()];
		
		for (int i = 0; i < coordinates.length(); i++)
		    coordinatesDouble[i] = coordinates.getDouble(i);
		
		return coordinatesDouble;
	}
}


// {"srcIP":"10.2.72.188","dstID":"ExampleServerID","type":POSSIBLE_STATES_MSG,"srcID":"ExampleClientID","dstIP":"localhost","possibleStates":[[35.041666666666664,35.69001340866089],[1145.8632427587956,42.975582122802734],[740.9923757563957,36.88046999945157]],"initState":[]}