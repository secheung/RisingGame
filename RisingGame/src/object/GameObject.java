package object;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import structure.ActionData;
import structure.ActionDataTool;
import structure.ActionProperties;
import structure.GameObjectLogic;
import structure.HitBox;
import structure.HurtBox;
import structure.InteractionProperties;
import android.util.Log;
import android.view.MotionEvent;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import game.GameTools.Gesture;
import game.GameLogic;
import game.GestureListener;

public abstract class GameObject {
	public interface GameObjectState{};
	
	public static enum Direction{
		LEFT,
		RIGHT,
		UP,
		DOWN
	}
	
	public static int INPUT_LIST_SIZE = 2;
	
	protected Direction direction;
	
	protected float x;
	protected float y;
	protected float z;
	
	protected float xVelocity;
	protected float yVelocity;
	protected float zVelocity;

	protected float xAccel;
	protected float yAccel;
	protected float zAccel;
	
	protected float width;
	protected float height;
	
	protected ActionData currentAction;
	protected GameObjectLogic currentLogic;

	protected InteractionProperties interProperties;
	
	//protected Plane display;
	protected HashMap<GameObjectState, Integer> animationRef;
	protected String name;
	protected boolean initSpeed;
	public boolean selected;
	protected boolean hitActive;
	protected LinkedList<Gesture> inputList;
	//protected Gesture gesture;
	
	protected int hitStopFrames;
	protected LinkedHashMap<String,GameObject> gameObjects;
	protected LinkedHashMap<GameObjectState, ActionData> actionData;
	
	public GameObject(LinkedHashMap<String,GameObject> gameObjects, List<ActionData> actionData, GameObjectState initState, float x, float y){
		this.x = x;
		this.y = y;
		
		this.xVelocity = 0;
		this.yVelocity = 0;
		this.zVelocity = 0;

		this.xAccel = 0;
		this.yAccel = 0;
		this.zAccel = 0;
		
		this.gameObjects = gameObjects;
		this.actionData = new LinkedHashMap<GameObjectState, ActionData>();
		this.setupAnimRef();
		this.mapActionData(actionData);

		currentAction = this.actionData.get(initState);
		currentLogic = new GameObjectLogic();
		//gesture = Gesture.NONE;
		inputList = new LinkedList<Gesture>();

		this.width = currentAction.getPlaneData().getWidth();
		this.height = currentAction.getPlaneData().getHeight();
		this.hitActive = false;
		this.initSpeed = false;
		this.hitStopFrames = 0;
	}
	
	
	public abstract void setupAnimRef();
	public abstract void mapActionData(List<ActionData> actionData);
	
	public abstract void updateState();
	public abstract void updateLogic();
	public abstract void updateDisplay();
	public abstract void updateAfterDisplay();
	public abstract void passTouchEvent(MotionEvent e, WorldRenderer worldRenderer);
	public abstract void passDoubleTouchEvent(GestureListener g, WorldRenderer worldRenderer);
	
	public void updateDrawData(WorldRenderer worldRenderer){
		currentAction.updateDrawData(worldRenderer,this);
	}
	
	public void loadAnimIntoRenderer(WorldRenderer worldRenderer){
		for(ActionData data : actionData.values()){
			data.loadAnimIntoRenderer(worldRenderer);
		}
	}
	
	public void unloadAnimFromRenderer(WorldRenderer worldRenderer){
		for(ActionData data : actionData.values()){
			data.unloadAnimFromRenderer(worldRenderer);
		}
	}
	
	public void switchAction(GameObjectState actionToSwitch){
		//Log.d(this.name, actionToSwitch.toString());
		currentAction.drawDisable();
		currentAction.getAnimation().resetAnimation();
		currentAction = actionData.get(actionToSwitch);
		currentAction.drawEnable();
		
		if(direction==Direction.RIGHT){
			currentAction.getAnimation().flipTexture(false);
		} else if(direction==Direction.LEFT){
			currentAction.getAnimation().flipTexture(true);
		}
		
		ActionProperties actProperties = currentAction.getActionProperties();
		currentLogic.reset();
		currentLogic.addTriggers(actProperties);
		if(interProperties != null){
			currentLogic.addTriggers(interProperties);
			currentLogic.buildInterInitSpeedLogic(interProperties, actProperties);
		}else{
			interProperties = null;
			currentLogic.buildContSpeedLogic(this, actProperties);
		}
		
		if(actProperties.hasModifier(ActionDataTool.ACTIVE_AFTER)){
			currentLogic.setActiveAfter(actProperties.getModifier(ActionDataTool.ACTIVE_AFTER));
		}
	}
	
	public void update() {
		updateState();
		updateLogic();
		updateDisplay();
		updateAfterDisplay();
	}

	public void initXPhys(float speed, float accel){
		xVelocity = speed;
		xAccel = accel;
	}

	public void initYPhys(float speed, float accel){
		yVelocity = speed;
		yAccel = accel;
	}
	
	public void executeXPhys(){
		xVelocity += xAccel;
		x += xVelocity;		
	}
	
	public void executeYPhys(){
		yVelocity += yAccel;
		y += yVelocity;		
	}

	public float getAccelFromSpeed(float speed, float accel){
		float setAccel = accel;
		if(accel < 0){
			if(speed < 0){
				setAccel = Math.abs(accel);
			} else if(speed > 0){
				setAccel = -Math.abs(accel);
			}
		} else if(accel > 0){
			if(speed > 0){
				setAccel = Math.abs(accel);
			} else if(speed < 0){
				setAccel = -Math.abs(accel);
			}
		}
		
		return setAccel;
	}

	public void addGesture(Gesture gesture){
		if(inputList.size() <= INPUT_LIST_SIZE){
			inputList.add(gesture);
		}
	}
	
	/*
	public void setGesture(Gesture gesture){
		this.gesture = gesture; 
	}
	*/
	
	public boolean isOnGround(){
		List<HurtBox> hurtBoxes = currentAction.getHurtBoxes();
		for(HurtBox box : hurtBoxes){
			if(y+box.getBoxData().bottom <= GameLogic.FLOOR)
				return true;
		}
		
		return false;
	}
	
	public boolean isAtWall(){
		//return (x+width >= GameLogic.WALL_RIGHT || x <= GameLogic.WALL_LEFT);
		List<HurtBox> hurtBoxes = currentAction.getHurtBoxes();
		for(HurtBox box : hurtBoxes){
			//if(x+box.getBoxData().right >= GameLogic.WALL_RIGHT || x+box.getBoxData().left <= GameLogic.WALL_LEFT)
			if(xVelocity > 0 && x+box.getBoxData().right >= GameLogic.WALL_RIGHT)
				return true;
			
			if(xVelocity < 0 && x+box.getBoxData().left <= GameLogic.WALL_LEFT)
				return true;
			
			if	(xVelocity == 0 && 
				(x+box.getBoxData().right >= GameLogic.WALL_RIGHT || x+box.getBoxData().left <= GameLogic.WALL_LEFT))
				return true;
		}
		
		return false;
	}
	
	public boolean isStopped(){
		if(xAccel > 0 && xVelocity > 0){
			return true;
		}else if(xAccel < 0 && xVelocity < 0){
			return true;
		} else if(xAccel == 0 && xVelocity == 0){
			return true;
		}
		
		return false;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Plane getDisplay(){
		return currentAction.getAnimation();
	}
	
	public Direction getDirection() {
		return direction;
	}


	public void setDirection(Direction direction) {
		this.direction = direction;
	}


	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getMidX(){
		return x+width/2;
	}
	
	public float getMidY(){
		return y+width/2;
	}
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public ActionData getCurrentAction() {
		return currentAction;
	}


	public void setCurrentAction(ActionData currentAction) {
		this.currentAction = currentAction;
	}


	public boolean getHitActive() {
		return hitActive;
	}

	public void setHitActive(boolean hitActive) {
		this.hitActive = hitActive;
	}


	public int getHitStopFrames() {
		return hitStopFrames;
	}


	public void setHitStopFrames(int hitStopFrames) {
		this.hitStopFrames = hitStopFrames;
	}


	public float getxVelocity() {
		return xVelocity;
	}


	public void setxVelocity(float xVelocity) {
		this.xVelocity = xVelocity;
	}


	public float getyVelocity() {
		return yVelocity;
	}


	public void setyVelocity(float yVelocity) {
		this.yVelocity = yVelocity;
	}


	public float getzVelocity() {
		return zVelocity;
	}


	public void setzVelocity(float zVelocity) {
		this.zVelocity = zVelocity;
	}
	
}
