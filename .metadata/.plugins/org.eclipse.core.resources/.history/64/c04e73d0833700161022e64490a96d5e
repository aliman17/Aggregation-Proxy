package publish.subscribe.string;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import json.messages.BaseMessage;
import json.messages.PossibleStatesMessage;
import json.messages.SelectedStateMessage;

public class Client implements ClientInterface {
	
	private SenderInterface sender;
	private double[] possibleStates;
	private double initState;
	private double selectedState;
	
	public Client() {
		// TODO Auto-generated constructor stub
		sender = new Sender();
		possibleStates = new double[4];
		possibleStates[0] = 1;
		possibleStates[1] = 2;
		possibleStates[2] = 3;
		possibleStates[3] = 4;
		initState = 1;
		selectedState = 2;
	}
	
	@Override
	public void start() {
		this.sendPossibleStates();
		this.sendSelectedState();
	}

	@Override
	public void setPossibleStates(double[] possibleStates) {
		this.possibleStates = possibleStates;		
	}

	@Override
	public void setSelectedStates(double selectedState) {
		this.selectedState = selectedState;
	}

	@Override
	public void sendPossibleStates() {
		
		PossibleStatesMessage posStMsg = 
				new PossibleStatesMessage(
						"ExampleSourceIP",
						"ExampleDestIP",
						"ExampleSourceID",
						"ExampleDestID",
						this.possibleStates, 
						this.initState);
		
		String serverIP = TestConstants.serverIP;  	// TODO: Test
		String port = TestConstants.port;			// TODO: Test
		
		String stringMessage;
		try {
			stringMessage = objectToString(posStMsg);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
			return;
		}
		
		// Send
		try {
			this.sender.send(stringMessage, serverIP, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendSelectedState() {
		
		SelectedStateMessage selStMsg = 
				new SelectedStateMessage(
						"ExampleSourceIP",
						"ExampleDestIP",
						"ExampleSourceID",
						"ExampleDestID"	);

		String serverIP = TestConstants.serverIP;  	// TODO: Test
		String port = TestConstants.port;			// TODO: Test
		
		String stringMessage;
		try {
			stringMessage = objectToString(selStMsg);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
			return;
		}
		
		// Send
		try {
			this.sender.send(stringMessage, serverIP, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String objectToString(BaseMessage messageObj) throws JsonProcessingException{
		ObjectMapper om = new ObjectMapper();
		String stringMessage = om.writeValueAsString(messageObj);
		return stringMessage;
	}
	
	public static void main(String[] args){
		Client client = new Client();
		client.start();
	}
}
