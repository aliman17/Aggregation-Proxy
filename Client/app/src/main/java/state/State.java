package state;

import android.content.Context;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class State {

	private String 	clientIP;
	private String 	clientID;
	private String 	serverIP;
	private String 	serverID;
	private int 	serverPort;
	private double 	initState;
	private double 	selectedState;
	private double[] possibleStates;

	public State(Context context) {
		// TODO
		clientIP = getMyIp();
		clientID = "ExampleClientID";
		serverIP = "localhost";
		serverID = "ExampleServerID";
		serverPort = 8080;
		initState = 1;
		selectedState = 2;
	}

	// GETTER FUNCTIONS
	public String getClientIP()		{ return this.clientIP;  }
	public String getClientID()		{ return this.clientID;  }
	public String getServerIP()		{ return this.serverIP;  }
	public int    getServerPort()	{ return this.serverPort;}
	public String getServerID()		{ return this.serverID;  }
	public double getInitState()	{ return this.initState; }
	public double   getSelectedState (){ return this.selectedState; }
	public double[] getPossibleStates(){ return this.possibleStates;}

	// SETTER FUNCTIONS
	public void setPossibleStates(double[] possibleStates) { this.possibleStates = possibleStates;}
	public void setSelectedStates(double   selectedState ) { this.selectedState = selectedState;  }

	// HELPER FUNCTIONS
	public static String getMyIp() {
		boolean useIPv4 = true;
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress();
						//boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						boolean isIPv4 = sAddr.indexOf(':')<0;

						if (useIPv4) {
							if (isIPv4)
								return sAddr;
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
								return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
							}
						}
					}
				}
			}
		} catch (Exception ex) { } // for now eat exceptions
		return "Can't get clientIP";
	}

}
