package structure;

import java.util.HashSet;
import java.util.LinkedHashMap;

public class ActionProperties {
	protected String hitType;
	protected float xInitSpeed;
	protected float yInitSpeed;
	protected float xAccel;
	protected float yAccel;
	protected float xPtOffset;
	protected float yPtOffset;
	
	protected String snapTo;
	protected float xPtSnap;
	protected float yPtSnap;
	
	LinkedHashMap<String,String> triggerChange;
	LinkedHashMap<String,String> triggerCancel;
	HashSet<Integer> cancelFrame;

	LinkedHashMap<String, Integer> modifiers;
	
	LinkedHashMap<String,TriggerProperties> triggerPropertiesList;//key, property values - like triggerChange and trigger cancel, but has properties for logic and change
	
	public ActionProperties(){
		hitType = ActionDataTool.SINGLE_HIT;
		xInitSpeed = 0;
		yInitSpeed = 0;
		xAccel = 0;
		yAccel = 0;
		xPtOffset = 0.0f;
		yPtOffset = 0.0f;

		snapTo = "";
		xPtSnap = 0.0f;
		yPtSnap = 0.0f;
		
		triggerChange = new LinkedHashMap<String, String>();
		triggerCancel = new LinkedHashMap<String, String>();
		cancelFrame = new HashSet<Integer>();
		modifiers = new LinkedHashMap<String, Integer>();
		
		triggerPropertiesList = new LinkedHashMap<String,TriggerProperties>();
	}

	public String getHitType() {
		return hitType;
	}

	public void setHitType(String hitType) {
		this.hitType = hitType;
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

	public float getxPtOffset() {
		return xPtOffset;
	}

	public void setxPtOffset(float xPtOffset) {
		this.xPtOffset = xPtOffset;
	}

	public float getyPtOffset() {
		return yPtOffset;
	}

	public void setyPtOffset(float yPtOffset) {
		this.yPtOffset = yPtOffset;
	}

	public String getSnapTo(){
		return snapTo;
	}
	
	public void setSnapTo(String snapTo){
		this.snapTo = snapTo;
	}
	
	public float getxPtSnap() {
		return xPtSnap;
	}

	public void setxPtSnap(float xPtSnap) {
		this.xPtSnap = xPtSnap;
	}

	public float getyPtSnap() {
		return yPtSnap;
	}

	public void setyPtSnap(float yPtSnap) {
		this.yPtSnap = yPtSnap;
	}
	
	public void addTriggerChange(String trigger, String nextState){
		triggerChange.put(trigger, nextState);
	}
	
	public String getTriggerChange(String trigger){
		return triggerChange.get(trigger);
	}

	public boolean hasTriggerChange(String trigger){
		return triggerChange.containsKey(trigger);
	}
	
	public LinkedHashMap<String,String> getTriggerChange(){
		return triggerChange;
	}
	
	public void addCancel(String trigger, String nextState){
		triggerCancel.put(trigger, nextState);
	}
	
	public boolean hasCancel(String trigger){
		return triggerCancel.containsKey(trigger);
	}
	
	public String getCancel(String trigger){
		return triggerCancel.get(trigger);
	}

	public LinkedHashMap<String, String> getTriggerCancel() {
		return triggerCancel;
	}

	public void setTriggerCancel(LinkedHashMap<String, String> triggerCancel) {
		this.triggerCancel = triggerCancel;
	}

	public void addCancelFrame(int frame){
		cancelFrame.add(frame);
	}

	public boolean hasCancelFrame(int frame){
		return cancelFrame.contains(frame);
	}

	public HashSet<Integer> getCancelFrame(){
		return cancelFrame;
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
	
	public void addTriggerProperties(String trigger, TriggerProperties triggerProp){
		triggerPropertiesList.put(trigger, triggerProp);
	}
	
	public TriggerProperties getTriggerProperties(String trigger){
		return triggerPropertiesList.get(trigger);
	}

	public boolean hasTriggerProperties(String trigger){
		return triggerPropertiesList.containsKey(trigger);
	}
	
	public LinkedHashMap<String,TriggerProperties> getTriggerPropertyList(){
		return triggerPropertiesList;
	}
	
}
