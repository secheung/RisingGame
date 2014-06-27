package structure;

import java.util.LinkedHashMap;

public class ActionProperties {
	protected float xInitSpeed;
	protected float yInitSpeed;
	protected float xAccel;
	protected float yAccel;
	
	LinkedHashMap<String,String> triggerChange;
	LinkedHashMap<String, Integer> modifiers;
	
	public ActionProperties(){
		xInitSpeed = 0;
		yInitSpeed = 0;
		xAccel = 0;
		yAccel = 0;

		triggerChange = new LinkedHashMap<String, String>();
		modifiers = new LinkedHashMap<String, Integer>();
	}

	public float getxInitSpeed() {
		return xInitSpeed;
	}

	public void setxInitSpeed(float xInitSpeed) {
		this.xInitSpeed = xInitSpeed;
	}

	public float getyInitSpeed() {
		return yInitSpeed;
	}

	public void setyInitSpeed(float yInitSpeed) {
		this.yInitSpeed = yInitSpeed;
	}

	public float getxAccel() {
		return xAccel;
	}

	public void setxAccel(float xAccel) {
		this.xAccel = xAccel;
	}

	public float getyAccel() {
		return yAccel;
	}

	public void setyAccel(float yAccel) {
		this.yAccel = yAccel;
	}

	public void addTrigger(String trigger, String nextState){
		triggerChange.put(trigger, nextState);
	}
	
	public String getTriggerState(String trigger){
		return triggerChange.get(trigger);
	}

	public LinkedHashMap<String,String> getTriggerChange(){
		return triggerChange;
	}

	public void addModifier(String modifier, Integer value){
		modifiers.put(modifier, value);
	}

	public boolean hasModifier(String modifier){
		return modifiers.containsKey(modifier);
	}
	
	public Integer getModifier(String modifier){
		return modifiers.get(modifier);
	}

	public LinkedHashMap<String,Integer> getModifiers(){
		return modifiers;
	}
	
}
