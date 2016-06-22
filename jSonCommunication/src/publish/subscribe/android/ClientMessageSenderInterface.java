package publish.subscribe.android;

public interface ClientMessageSenderInterface {
	public void send(String stringMessage, String serverIP, String port, String clientQueueName) throws Exception;
}
