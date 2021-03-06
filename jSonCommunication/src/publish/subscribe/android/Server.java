package publish.subscribe.android;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class Server implements Runnable {

	ServerSocket socket;

	public Server(ServerSocket socket) {
		this.socket = socket;
	}

	@Override
	public synchronized void run() {
		while (true) {
			try {

				// WifiManager wm = (WifiManager)
				// getSystemService(WIFI_SERVICE);
				// String ip =
				// Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

				System.out
						.println("*************Server Started****************");
				// Enumeration en = NetworkInterface.getNetworkInterfaces();
				// while(en.hasMoreElements()){
				// NetworkInterface ni=(NetworkInterface) en.nextElement();
				// Enumeration ee = ni.getInetAddresses();
				// while(ee.hasMoreElements()) {
				// InetAddress ia= (InetAddress) ee.nextElement();
				// System.out.println(ia.getHostAddress());
				// }
				// }
				InetAddress IP = InetAddress.getLocalHost();
				System.out.println("IP of Server is := " + IP.getHostAddress());
				System.out.println("Server getInetAddress- "
						+ socket.getInetAddress());
				System.out.println("Server LocalSocketAddress- "
						+ socket.getLocalSocketAddress());
				System.out
						.println("Server LocalPort- " + socket.getLocalPort());
				System.out
						.println("*******************************************");
				Socket clientSocket = socket.accept();
				CommunicationHandler h = new CommunicationHandler(clientSocket);
				new Thread(h).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	static class CommunicationHandler implements Runnable {
		Socket socket;

		public CommunicationHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				ObjectInputStream in = new ObjectInputStream(
						new BufferedInputStream(socket.getInputStream()));
				while (true) {
					Packet reading = (Packet) in.readObject();
					reading.getMessage();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
