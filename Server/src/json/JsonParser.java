package json;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import json.enums.MessageTypes;
import json.messages.BaseMessage;
import json.messages.PossibleStatesMessage;

public class JsonParser {
	public static JSONParser parser = new JSONParser();
	
	public static BaseMessage parse(String jsonMessage) throws ParseException{
		
		JSONObject obj = (JSONObject)parser.parse(jsonMessage);
		String type = (String) obj.get("type");
		String srcIP = (String) obj.get("srcIP");
		String dstIP = (String) obj.get("dstIP");
		String srcID = (String) obj.get("srcID");
		String dstID = (String) obj.get("dstID");

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
	
	
	private static ArrayList getPossibleStates(JSONObject obj) {
		
		ArrayList possibleStates = new ArrayList();
		
		JSONArray arr = (JSONArray)obj.get("possibleStates");
		for (int i = 0; i < arr.size(); i++)
		{
		    JSONArray coordinates = (JSONArray)arr.get(i);
		    double[] coordinatesDouble = new double[coordinates.size()];
		    for (int j = 0; j < coordinates.size(); j++) {
		    	coordinatesDouble[j] = (double)coordinates.get(j);
		    }
		    possibleStates.add(coordinatesDouble);
		}

		return possibleStates;
		
	}
	
	private static double[] getInitState(JSONObject obj) {
		
		JSONArray coordinates = (JSONArray)obj.get("initState");
		double[] coordinatesDouble = new double[coordinates.size()];
		
		for (int i = 0; i < coordinates.size(); i++)
		    coordinatesDouble[i] = (double)coordinates.get(i);
		
		return coordinatesDouble;
	}
}


// {"srcIP":"10.2.72.188","dstID":"ExampleServerID","type":POSSIBLE_STATES_MSG,"srcID":"ExampleClientID","dstIP":"localhost","possibleStates":[[35.041666666666664,35.69001340866089],[1145.8632427587956,42.975582122802734],[740.9923757563957,36.88046999945157]],"initState":[]}