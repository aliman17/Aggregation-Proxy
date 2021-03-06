package publish.subscribe.android;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ServerAsyncListener implements MessageListener {
	private static int ackMode = Session.AUTO_ACKNOWLEDGE;
	private String messageQueueName;
	private String messageBrokerUrl;
	private Session session;
	private boolean transacted = false;
	private JsonServerParser parser;

	public ServerAsyncListener(ServerInterface server, String messageBrokerUrl, String messageQueueName) {
		this.messageBrokerUrl = messageBrokerUrl;
		this.messageQueueName = messageQueueName;
		this.parser = new JsonServerParser(null); // TODO, null has to be 'server'
	}

	public void setupMessageQueueConsumer() {
		ActiveMQConnectionFactory connectionFactory = 
				new ActiveMQConnectionFactory(messageBrokerUrl);
		Connection connection;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			this.session = connection.createSession(this.transacted, ackMode);
			Destination adminQueue = this.session.createQueue(messageQueueName);

			// Set up a consumer to consume messages off of the admin queue
			MessageConsumer consumer = this.session.createConsumer(adminQueue);
			consumer.setMessageListener(this);
		} catch (JMSException e) {
			// Handle the exception appropriately
			System.out.println("Setup message queue failed");
			System.out.println(e);
		}
	}

	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;
		try {
			System.out.println("received: " + msg.getText());
			parser.receiveJson(msg.getText());

		} catch (JMSException ex) {
			ex.printStackTrace();
		}
	}
}
