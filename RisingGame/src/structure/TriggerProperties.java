package structure;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class TriggerProperties {
	public ArrayList<Double> value;//value to check
	public ArrayList<String> state;//state to change to
	public HashMap<String, String> cond_state;
	
	public TriggerProperties() {
		value = new ArrayList<Double>();
		state = new ArrayList<String>();
		cond_state = new HashMap<String,String>();
	}
}
