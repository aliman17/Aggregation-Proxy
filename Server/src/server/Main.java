package server;

import java.net.ServerSocket;

public class Main {

	public static int port = 8080;
	public static void main(String[] args) throws Exception {

		ServerSocket androidApp = null;
		try {
			androidApp = new ServerSocket(port);

			ServerListener accReadingListener = new ServerListener(
					androidApp);
			new Thread(accReadingListener).start();


		} catch (Exception e) {
			e.printStackTrace();
			
			androidApp.close();
			return;
		}
	}
}