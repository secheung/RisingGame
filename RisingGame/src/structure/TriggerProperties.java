package structure;

import java.util.ArrayList;

public class TriggerProperties {
	public ArrayList<Double> value;//value to check
	public ArrayList<String> state;//state to change to
	
	public TriggerProperties() {
		value = new ArrayList<Double>();
		state = new ArrayList<String>();
	}
}
