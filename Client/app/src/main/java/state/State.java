package state;

import android.content.Context;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clustering.KMeans;
import clustering.Point;
import database.DatabaseHandler;
import json.WriteJSON;
import json.messages.PossibleStatesMessage;
import json.messages.SelectedStateMessage;



public class State {

	private String clientIP;
	private String clientID;

	private String serverIP;
	private int serverPort;

	private String serverID;

	private double[] possibleStates;
	private double initState;

	private double selectedState;

	public State(Context context) {
		// TODO Get client's IP address
		clientIP = getMyIp();
		clientID = "ExampleClientID";
		serverIP = "localhost";
		serverID = "ExampleServerID";
		serverPort = 8080;

		// TODO: Set states
		//possibleStates = new double[4];
		//possibleStates[0] = 1;
		//possibleStates[1] = 2;
		//possibleStates[2] = 3;
		//possibleStates[3] = 4;
		initState = 1;
		selectedState = 2;
		initPossibleStates(context);
	}



	private void initPossibleStatesNervousnet(){
		//NervousnetRemote;
	}

	private void initPossibleStates(Context context){
		// Creating database and table
		DatabaseHandler db = new DatabaseHandler(context);
		ArrayList<Double> sensorValues = db.getAllSensorValues();
		ArrayList<Point> points = new ArrayList<>();
		for( Double d : sensorValues ) {
			points.add(new Point(d));
		}

		KMeans kmeans = new KMeans(points);
		kmeans.calculate();
		int len = kmeans.clusters.size();
		possibleStates = new double[len];
		for( int i = 0; i < len; i++ ) {
			possibleStates[i] = kmeans.clusters.get(i).centroid.getX();
		}
	}

	public void setPossibleStates(double[] possibleStates) {
		this.possibleStates = possibleStates;
	}

	public void setSelectedStates(double selectedState) {
		this.selectedState = selectedState;
	}

	public double[] getPossibleStates(){
		return this.possibleStates;
	}

	public double getSelectedState(){
		return this.selectedState;
	}

	public String getPossibleStatesMessage() {

		PossibleStatesMessage posStMsg = new PossibleStatesMessage(
				this.clientIP, this.serverIP, this.clientID,
				this.serverID, this.possibleStates, this.initState);

		return WriteJSON.serialize(posStMsg);
	}

	public String getSelectedStateMessage() {

		SelectedStateMessage selStMsg = new SelectedStateMessage(
				this.clientIP, this.serverIP, this.clientID,
				this.serverID, this.selectedState);

		return WriteJSON.serialize(selStMsg);
	}

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
