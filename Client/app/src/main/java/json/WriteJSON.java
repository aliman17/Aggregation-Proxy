package json;

import flexjson.JSONSerializer;
import json.messages.BaseMessage;

public class WriteJSON {
	
	public static String serialize(BaseMessage bmsg){
		String msg = new JSONSerializer().serialize(bmsg);
		return msg;
	}
}
