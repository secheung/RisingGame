package structure;

import java.util.LinkedHashMap;

import android.util.Log;

import object.GameObject;

public class GameObjectLogic {
	private LinkedHashMap<String, String> triggers;

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
	}

	public void reset(){
		triggers.clear();
		xInitSpeed = 0;
		yInitSpeed = 0;
		xAccel = 0;
		yAccel = 0;
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
		int action_dir = actProperties.getModifier(ActionDataTool.CONT_SPEED);
		if(action_dir == ActionDataTool.CONT_SPEED_BOTH_DIR){
			xInitSpeed = gameObject.getxVelocity();
			yInitSpeed = gameObject.getyVelocity();
		}else if(action_dir == ActionDataTool.CONT_SPEED_X_DIR){
			xInitSpeed = gameObject.getxVelocity();
			yInitSpeed = actProperties.getyInitSpeed();
		}else if(action_dir == ActionDataTool.CONT_SPEED_Y_DIR){
			xInitSpeed = actProperties.getxInitSpeed();
			yInitSpeed = gameObject.getyVelocity();
		}
		
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
