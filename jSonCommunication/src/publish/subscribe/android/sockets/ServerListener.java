package publish.subscribe.android.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

class ServerListener implements Runnable {

	ServerSocket socket;

	public ServerListener(ServerSocket socket) {
		this.socket = socket;
	}

	@Override
	public synchronized void run() {
		InetAddress IP;
		try {
			System.out
			.println("*************Server Started****************");
			IP = InetAddress.getLocalHost();
			System.out.println("IP of Server is := " + IP.getHostAddress());
			System.out.println("Server getInetAddress- "
					+ socket.getInetAddress());
			System.out.println("Server LocalSocketAddress- "
					+ socket.getLocalSocketAddress());
			System.out
					.println("Server LocalPort- " + socket.getLocalPort());
			System.out
					.println("*******************************************");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		
		while (true) {
			try {
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
				BufferedReader in =
				        new BufferedReader(
				            new InputStreamReader(socket.getInputStream()));
				
					//Packet reading = (Packet) in.readObject();
					System.out.println("Client sent: " + in.readLine());
					
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