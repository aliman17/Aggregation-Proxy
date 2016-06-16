package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import com.sun.corba.se.spi.ior.Writeable;

public class ClientCommunicator {
	
	private String serverName;
	private int portNumber;
	
	
	public ClientCommunicator(String serverName, int portNumber)
	{
		this.serverName = serverName;
		this.portNumber = portNumber;
	}
	
	public void write(String message){
		try {
			Socket socket = openSocket(this.serverName, this.portNumber);
			writeToAndReadFromSocket(socket, message);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void writeToAndReadFromSocket(Socket socket, String writeTo) throws Exception
	  {
	    try
	    {
	      // write text to the socket
	      BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	      bufferedWriter.write(writeTo);
	      bufferedWriter.flush();
	       
//	      // read text from the socket
//	      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//	      StringBuilder sb = new StringBuilder();
//	      String str;
//	      while ((str = bufferedReader.readLine()) != null)
//	      {
//	        sb.append(str + "\n");
//	      }
//	       
//	      // close the reader, and return the results as a String
//	      bufferedReader.close();
//	      return sb.toString();
	    } 
	    catch (IOException e) 
	    {
	      e.printStackTrace();
	      throw e;
	    }
	  }
	
	/**
	   * Open a socket connection to the given server on the given port.
	   * This method currently sets the socket timeout value to 10 seconds.
	   * (A second version of this method could allow the user to specify this timeout.)
	   */
	  private Socket openSocket(String server, int port) throws Exception
	  {
	    Socket socket;
	     
	    // create a socket with a timeout
	    try
	    {
	      InetAddress inteAddress = InetAddress.getByName(server);
	      SocketAddress socketAddress = new InetSocketAddress(inteAddress, port);
	   
	      // create a socket
	      socket = new Socket();
	   
	      // this method will block no more than timeout ms.
	      int timeoutInMs = 10*1000;   // 10 seconds
	      socket.connect(socketAddress, timeoutInMs);
	       
	      return socket;
	    } 
	    catch (SocketTimeoutException ste) 
	    {
	      System.err.println("Timed out waiting for the socket.");
	      ste.printStackTrace();
	      throw ste;
	    }
	  }
}
