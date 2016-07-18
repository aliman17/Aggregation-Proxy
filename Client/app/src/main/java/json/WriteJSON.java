package json;

import flexjson.JSONSerializer;
import json.messages.BaseMessage;

public class WriteJSON {
	
	public static String serialize(String name, Object msg){
		// Include possibleStates is patch to include double array, otherwise
		// the values are not presented in the array
		String msgSer = new JSONSerializer().include(name).serialize(msg);
		return msgSer;
	}
}
