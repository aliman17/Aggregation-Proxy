package publish.subscribe.android;

import java.net.InetAddress;
import java.net.UnknownHostException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import json.messages.BaseMessage;
import json.messages.PossibleStatesMessage;
import json.messages.SelectedStateMessage;

public class Client implements ClientInterface {

	private String clientIP;
	private String clientID;

	private String serverIP;
	private String serverPort;
	private String serverID;
	private String clientQueueName;

	private double[] possibleStates;
	private double initState;
	private double selectedState;

	private ClientMessageSenderInterface sender;
	private ConfigurationLoader config;

	public Client() {
		// Get client's IP address
		try {
			clientIP = getMyIp();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// TODO: Get client's ID
		clientID = "ExampleID";

		// Get server's configurations
		config = new ConfigurationLoader();
		serverIP = config.serverIP;
		serverID = config.serverID;
		serverPort = config.serverPort;
		clientQueueName = config.clientQueueName;

		// TODO: Set states
		possibleStates = new double[4];
		possibleStates[0] = 1;
		possibleStates[1] = 2;
		possibleStates[2] = 3;
		possibleStates[3] = 4;
		initState = 1;
		selectedState = 2;

		// Declare class for sending messages to the server
		sender = new ClientMessageSender();
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

		PossibleStatesMessage posStMsg = new PossibleStatesMessage(
				this.clientIP, this.serverIP, this.clientID,
				this.serverID, this.possibleStates, this.initState);

		String stringMessage;
		try {
			stringMessage = objectToString(posStMsg);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
			return;
		}

		try {
			this.sender.send(stringMessage, this.serverIP, this.serverPort, this.clientQueueName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendSelectedState() {

		SelectedStateMessage selStMsg = new SelectedStateMessage(
				this.clientIP, this.serverIP, this.clientID,
				this.serverID, this.selectedState);

		String stringMessage;
		try {
			stringMessage = objectToString(selStMsg);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
			return;
		}

		try {
			this.sender.send(stringMessage, this.serverIP, this.serverPort, this.clientQueueName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String objectToString(BaseMessage messageObj) 
			throws JsonProcessingException {
		ObjectMapper om = new ObjectMapper();
		String stringMessage = om.writeValueAsString(messageObj);
		return stringMessage;
	}

	public static String getMyIp() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.start();
	}
}
