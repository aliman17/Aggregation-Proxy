package publish.subscribe.android;

import java.util.ArrayList;

import json.node.CommunicationListener;

public interface JsonServerParserInterface {
	void receiveJson(String json);

	void addListener(CommunicationListener listener);

	ArrayList<CommunicationListener> getListeners();
}
