package publish.subscribe.string;

public interface ClientInterface {
	public void start();
	public void setPossibleStates(double[] possibleStates);
	public void setSelectedStates(double selectedState);
	public void sendPossibleStates();
	public void sendSelectedState();
}
