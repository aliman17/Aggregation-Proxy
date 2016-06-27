package json;

import flexjson.JSONDeserializer;
import json.messages.BaseMessage;

public class ReadJSON {
	
	public static BaseMessage deserialize(String flexJson){
		BaseMessage msg = (BaseMessage) new JSONDeserializer()
				.deserialize(flexJson);
		return msg;
	}
}
