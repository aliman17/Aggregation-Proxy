package publish.subscribe.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationLoader {

	String serverIP;
	String serverID;
	String serverPort;
	String clientQueueName;
	String brokerUrl;

	public ConfigurationLoader() {
		try {
			this.loadConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadConfig() throws IOException {
		InputStream inputStream;
		try {
			Properties prop = new Properties();
			// Open config file
			String propFileName = "./config.properties";
			// Open stream
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			// Load properties
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
			// Get properties
			this.serverIP = prop.getProperty("serverIP");
			this.serverID = prop.getProperty("serverID");
			this.serverPort = prop.getProperty("serverPort");
			this.brokerUrl = prop.getProperty("brokerUrl");
			this.clientQueueName = prop.getProperty("clientQueueName");
			// Close stream
			inputStream.close();
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}
