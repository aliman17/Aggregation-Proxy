package json;

import flexjson.JSONSerializer;
import json.messages.BaseMessage;

public class WriteJSON {
	
	public static String serialize(BaseMessage bmsg){
		// Include possibleStates is patch to include double array, otherwise
		// the values are not presented in the array
		String msg = new JSONSerializer().include("possibleStates").serialize(bmsg);
		return msg;
	}
}
