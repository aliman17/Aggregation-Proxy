package json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import json.enums.MessageTypes;
import json.enums.SubscriptionPolicy;
import json.enums.Utility;
import json.messages.AggregateMessage;
import json.messages.PossibleStatesMessage;
import json.messages.RequestMessage;
import json.messages.SelectedStateMessage;
import json.messages.SubscriptionMessage;
import json.parser.JsonServerParser;


public class JsonGenerator {
	
	private InetSocketAddress srcIP = new InetSocketAddress("/127.23.23.3", 2323);		// this client
	private InetSocketAddress dstIP = new InetSocketAddress("/127.0.0.1", 5555);		// server
	
	public static void makeDirs() {
		File f1 = new File("jsonOutput");
		f1.mkdir();
		
		f1 = new File("jsonOutput/possibleStates");
		f1.mkdir();
		
		f1 = new File("jsonOutput/selectedStates");
		f1.mkdir();
		
		f1 = new File("jsonOutput/aggregates");
		f1.mkdir();
		
		f1 = new File("jsonOutput/requests");
		f1.mkdir();
		
		f1 = new File("jsonOutput/subscriptions");
		f1.mkdir();
	}
	
	public static void makeSubDirs(String filename) {
		File f = new File("jsonOutput/selectedStates/" + filename);
		f.mkdir();
	}
	
	public PossibleStatesMessage sendPossibleStatesMsg(String output) {
		PossibleStatesMessage psm = new PossibleStatesMessage(srcIP.toString(), dstIP.toString(), "CLIENT", "node-0");
		//psm.possibleStates = null;
		
		psm.possibleStates = new double[5];
//		for(int i = 0; i < 5; i++) {
//			double r=Math.random();
//            double min=0.0;
//            double max=1.0;
//            double x=r*max+min-r*min;
//			psm.possibleStates[i] = x;
//		}
		psm.possibleStates[0] = 0.1;
		psm.possibleStates[1] = 0.2;
		psm.possibleStates[2] = 0.3;
		psm.possibleStates[3] = 0.4;
		psm.possibleStates[4] = 0.5;
		psm.initState = psm.possibleStates[0];
		System.out.println(psm.toString());
		ObjectMapper om = new ObjectMapper();
		try {
			om.writeValue(new FileOutputStream(output), psm);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return psm;
	}
	
	public SelectedStateMessage sendSelectedStateMsg(String output, double selectedState) {
		SelectedStateMessage ssm = new SelectedStateMessage(srcIP.toString(), dstIP.toString(), "CLIENT", "node-0");
		ssm.selectedState = selectedState;
		
		System.out.println(ssm.toString());
		ObjectMapper om = new ObjectMapper();
		try {
			om.writeValue(new FileOutputStream(output), ssm);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ssm;
	}
	
	public RequestMessage sendRequestMessage(String output) {
		RequestMessage rm = new RequestMessage(srcIP.toString(), dstIP.toString(), "CLIENT", "node-0");
		
		rm.aggregationFunctions = new String[7];
		rm.aggregationFunctions[0] = "AVG";
		rm.aggregationFunctions[1] = "SUM";
		rm.aggregationFunctions[2] = "MIN";
		rm.aggregationFunctions[3] = "MAX";
		rm.aggregationFunctions[4] = "COUNT";
		rm.aggregationFunctions[5] = "SUMSQR";
		rm.aggregationFunctions[6] = "STDEV";		
		
		System.out.println(rm.toString());
		ObjectMapper om = new ObjectMapper();
		try {
			om.writeValue(new FileOutputStream(output), rm);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return rm;
	}
	
	public SubscriptionMessage sendSubscriptionMessage(String output) {
		SubscriptionMessage sm = new SubscriptionMessage(srcIP.toString(), dstIP.toString(), "CLIENT", "node-0");
		
		sm.subscriptionPolicy = SubscriptionPolicy.ON_UPDATE;
		sm.aggregationFunctions = new String[4];
		sm.aggregationFunctions[0] = "SUM";
		sm.aggregationFunctions[1] = "COUNT";
		sm.aggregationFunctions[2] = "MAX";
		sm.aggregationFunctions[3] = "SUMSQR";
		
		System.out.println(sm.toString());
		ObjectMapper om = new ObjectMapper();
		try {
			om.writeValue(new FileOutputStream(output), sm);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sm;
		
	}
	
	public void readFromFile(String fileName) {
		BufferedReader in = null;
		try {
			
			in = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line=in.readLine()) != null) {
				sb.append(line + "\n");
			}
			String json = sb.toString();
			
			ObjectMapper objectMapper  =  new ObjectMapper();
			JsonNode node = objectMapper.readValue(json, JsonNode.class);
			JsonNode typeNode = node.get("type");
			String type = typeNode.asText();
			MessageTypes mType = Utility.string2MessageTypes(type);
			
			switch(mType) {
			case SELECTED_STATE_MSG:
				SelectedStateMessage ssm = objectMapper.readValue(json, SelectedStateMessage.class);
				System.out.println(ssm.toString());
				//processSSM(ssm, clientIP);
				//TODO
				break;
			case POSSIBLE_STATES_MSG:
				PossibleStatesMessage psm = objectMapper.readValue(json, PossibleStatesMessage.class);
				System.out.println(psm.toString());
				//processPSM(psm, clientIP);
				//TODO
				break;
			case AGGREGATE_MSG:
				AggregateMessage am = objectMapper.readValue(json, AggregateMessage.class);
				System.out.println(am.toString());
				//processAM(am, clientIP);
				break;
			case SUBSCRIPTION_MSG:
				SubscriptionMessage sm = objectMapper.readValue(json, SubscriptionMessage.class);
				System.out.println(sm.toString());
				//processSM(sm, clientIP);
				break;
			case REQUEST_MSG:
				RequestMessage rm = objectMapper.readValue(json, RequestMessage.class);
				System.out.println(rm.toString());
				//processRM(rm, clientIP);
				break;
			default:
				// some other kind of message	
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		String psmPath = "jsonOutput/possibleStates/node-";
		String ssmPath = "jsonOutput/selectedStates/";
		String rmPath = "jsonOutput/requests/node-";
		String smPath = "jsonOutput/subscriptions/node-";
		String amPath = "jsonOutput/aggregates/node-";
		
		makeDirs();
		
		for(int i = 0; i < 1; i++) {
			JsonGenerator jg = new JsonGenerator();
			PossibleStatesMessage psm = jg.sendPossibleStatesMsg(psmPath + i + ".json");
			for(int j = 0; j < 5; j++) {
				makeSubDirs(j+"");
				double[] array = psm.possibleStates;
				if(array != null) {
					Random rand = new Random();
					int randomNum = rand.nextInt((4 - 0) + 1) + 0;				
					jg.sendSelectedStateMsg(ssmPath + j + "/node-"+ i + ".json", array[randomNum]);
				}
				else {
					jg.sendSelectedStateMsg(ssmPath + j + "/node-"+ i + ".json", -1.02);
				}
				
			}
			
			jg.sendRequestMessage(rmPath + i + ".json");
			jg.sendSubscriptionMessage(smPath + i + ".json");
			//jg.readFromFile(amPath + "0" + ".json");
		}
		
		
	}

}
