package structure;

import java.util.LinkedHashMap;

import android.util.Log;

import object.GameObject;

public class GameObjectLogic {
	private LinkedHashMap<String, String> triggers;
	private GameObject fromObject;

	protected int activeAfter;
	
	protected float xInitSpeed;
	protected float yInitSpeed;
	protected float xAccel;
	protected float yAccel;
	
	
	public GameObjectLogic(){
		triggers = new LinkedHashMap<String, String>();
		xInitSpeed = 0;
		yInitSpeed = 0;
		xAccel = 0;
		yAccel = 0;
		activeAfter = -1;
	}

	public void reset(){
		triggers.clear();
		xInitSpeed = 0;
		yInitSpeed = 0;
		xAccel = 0;
		yAccel = 0;
		activeAfter = -1;
	}
	
	public void buildActInitSpeedLogic(ActionProperties actProperties){
		xInitSpeed = actProperties.getxInitSpeed();
		yInitSpeed = actProperties.getyInitSpeed();
		xAccel = actProperties.getxAccel();
		yAccel = actProperties.getyAccel();
	}
	
	public void buildInterInitSpeedLogic(InteractionProperties interProperties, ActionProperties actProperties){
		xInitSpeed = interProperties.getxInitSpeed();
		yInitSpeed = interProperties.getyInitSpeed();
		xAccel = actProperties.getxAccel();
		yAccel = actProperties.getyAccel();
	}

	public void buildContSpeedLogic(GameObject gameObject, ActionProperties actProperties){
		xInitSpeed = gameObject.getxVelocity();
		yInitSpeed = gameObject.getyVelocity();
		xAccel = actProperties.getxAccel();
		yAccel = actProperties.getyAccel();
	}
	
	public void buildTriggers(ActionProperties actProperties){
		addTriggers(actProperties);
	}
	
	public LinkedHashMap<String, String> getTriggers() {
		return triggers;
	}

	public void setTriggers(LinkedHashMap<String, String> triggers) {
		this.triggers = triggers;
	}
	
	public void addTriggers(ActionProperties properties){
		this.triggers.putAll(properties.getTriggerChange());
	}

	public boolean hasTrigger(String trigger) {
		return triggers.containsKey(trigger);
	}

	public void addTrigger(String trigger, String nextState){
		triggers.put(trigger, nextState);
	}

	public String getTrigger(String trigger){
		return triggers.get(trigger);
	}
	
	public GameObject getFromObject() {
		return fromObject;
	}

	public void setFromObject(GameObject fromObject) {
		this.fromObject = fromObject;
	}

	public int getActiveAfter() {
		return activeAfter;
	}

	public void setActiveAfter(int activeAfter) {
		this.activeAfter = activeAfter;
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
	
}
