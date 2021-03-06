package publish.subscribe.parser;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import publish.subscribe.string.TestConstants;

public class ClientMessageSender implements ClientMessageSenderInterface {

	private static int ackMode = Session.AUTO_ACKNOWLEDGE;

	private boolean transacted = false;
	private MessageProducer producer;

	public ClientMessageSender() {}

	@Override
	public void send(String stringMessage, String serverIP, String port, String clientQueueName) 
			throws Exception {
		ActiveMQConnectionFactory connectionFactory = 
				new ActiveMQConnectionFactory(serverIP + ":" + port);
		
		Connection connection;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			Session session = connection.createSession(transacted, ackMode);
			Destination adminQueue = session.createQueue(clientQueueName);

			// Setup a message producer to send message to the queue the server
			// is consuming from
			this.producer = session.createProducer(adminQueue);
			this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Now create the actual message you want to send
			TextMessage txtMessage = session.createTextMessage();
			txtMessage.setText(stringMessage);

			this.producer.send(txtMessage);
		} catch (JMSException e) {
			// Handle the exception appropriately
			System.out.println(e);
		}
	}

	public static void main(String[] args) throws Exception {
		ClientMessageSender sender = new ClientMessageSender();
		sender.send("Hello", TestConstants.serverIP, TestConstants.port, "client.messages");
	}
}
