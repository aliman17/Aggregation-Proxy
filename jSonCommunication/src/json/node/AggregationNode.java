package json.node;

import java.util.ArrayList;
import java.util.HashMap;

import json.enums.JsonTags;
import json.enums.SubscriptionPolicy;
import json.server.JsonServerInterface;
import json.server.JsonServerSide;

public class AggregationNode implements CommunicationListener {
	
	private HashMap<JsonTags, Double> aggregates;	
	private ArrayList<Double> possibleStates;
	private double selectedState;
	
	private JsonServerInterface server;
	
	private String myIPaddress = "/127.0.0.1:5555";
	private String myID = "node-0";
	
	public AggregationNode() {
		this.server = new JsonServerSide(myIPaddress, myID);
		this.server.addListener(this);
		this.init();
	}	
	
	private void init() {
		this.aggregates = new HashMap<JsonTags, Double>();
		this.possibleStates = new ArrayList<Double>();
		this.selectedState = 0;
	}
	
	public void startServer() {
		this.server.start();
	}
	
	private void setAggregate(JsonTags tag, double value) {
		if(this.aggregates.containsKey(tag)) {
			this.aggregates.remove(tag);
		}
		this.aggregates.put(tag, value);
	}
	
	private void addAggregate(JsonTags tag, double value) {
		if(!this.aggregates.containsKey(tag)) {
			return;
		}
		double old = this.aggregates.remove(tag);
		old += value;
		this.aggregates.put(tag, old);
	}
	
	
	@Override
	public void setPossibleStates(ArrayList<Double> list, double initState) {
		this.possibleStates = list;
		this.selectedState = initState;
		setAggregate(JsonTags.SUM, selectedState);
		setAggregate(JsonTags.AVG, selectedState);
		setAggregate(JsonTags.MAX, selectedState);
		setAggregate(JsonTags.MIN, selectedState);
		setAggregate(JsonTags.COUNT, 1);
		setAggregate(JsonTags.SUMSQR, selectedState*selectedState);
		setAggregate(JsonTags.STDEV, 0);
		this.printAggregates();
	}
	
	@Override
	public void setSelectedState(double state) {
		if(checkNewState(state)) {
			this.selectedState = state;
		}
		System.out.println("Selected state = " + this.selectedState);
	}
	
	@Override
	public HashMap<JsonTags, Double> getAllAggregates() {
		HashMap<JsonTags, Double> mostRecentAggregates = new HashMap<JsonTags, Double>();
		for(JsonTags tag : this.aggregates.keySet()) {
			mostRecentAggregates.put(tag, this.aggregates.get(tag));
		}		
		return mostRecentAggregates;
	}
	
	@Override
	public Double getAggregate(JsonTags tag) {
		return this.aggregates.get(tag);
	}
	
	@Override
	public void scheduleAnswer(JsonTags[] tags, String clientIP) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void subscribe(String IP, SubscriptionPolicy policy, int[] timeMillis, JsonTags[] aggregationFunctions) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void unsubscribe(String IP, JsonTags[] aggregationFunctions) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean checkNewState(double newstate) {
		for(Double state : possibleStates) {
			if(state == newstate) {
				return true;
			}
		}
		return false;
	}
	
	private void printAggregates() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t[" + System.lineSeparator());
		for(JsonTags tag : this.aggregates.keySet()) {
			sb.append("\t\t" + tag + " = " + this.aggregates.get(tag) + System.lineSeparator());
		}
		sb.append("\t]" + System.lineSeparator());
		System.out.println(sb.toString());		
	}
	
	
	
	public static void main(String[] args) {
		AggregationNode node = new AggregationNode();
		node.startServer();
	}
	
	
	

}
