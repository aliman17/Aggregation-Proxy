package publish.subscribe.parser;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.activemq.broker.BrokerService;

import json.node.CommunicationListener;

public class Server implements ServerInterface{
	private ServerAsyncListener asyncReceiver;
	private String clientQueueName;
	private String brokerUrl;
	
	private ConfigurationLoader config;
	
	public Server(){		
		config = new ConfigurationLoader();
		clientQueueName = config.clientQueueName;
		brokerUrl = config.brokerUrl;
		asyncReceiver = new ServerAsyncListener(this, brokerUrl, clientQueueName);
		System.out.println("[Server] My IP is: " + getIP());
	}
	
	public void startBroker(){
		try {
			// This message broker is embedded
			BrokerService broker = new BrokerService();
			broker.setPersistent(false);
			broker.setUseJmx(false);
			broker.addConnector(this.brokerUrl);
			broker.start();
			System.out.println("Broker is running");
		} catch (Exception e) {
			System.out.println("Broker Err");
			e.printStackTrace();
		}
	}

	@Override
	public void sendJson(String json, String clientIP) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		this.startBroker();
		asyncReceiver.setupMessageQueueConsumer();
	}

	@Override
	public String getIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(CommunicationListener listener) {
		// TODO Auto-generated method stub
		
	}
	
	public static String getMyIp() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	public static void main(String[] args){
		Server server = new Server();
		server.start();
	}
}
