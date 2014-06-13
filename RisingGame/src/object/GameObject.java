package object;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import structure.ActionData;
import android.R;
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
	
	protected Direction direction;
	
	protected float x;
	protected float y;
	protected float z;
	
	protected float xVelocity;
	protected float yVelocity;
	protected float zVelocity;

	protected float width;
	protected float height;
	
	protected ActionData currentAction;
	//protected Plane display;
	protected HashMap<GameObjectState, Integer> animationRef;
	protected String name;
	public boolean selected;
	protected boolean hitActive;
	protected Gesture gesture;
	
	protected int hitStopFrames;
	protected LinkedHashMap<String,GameObject> gameObjects;
	protected LinkedHashMap<GameObjectState, ActionData> actionData;
	
	public GameObject(LinkedHashMap<String,GameObject> gameObjects, List<ActionData> actionData, GameObjectState initState, float x, float y){
		this.x = x;
		this.y = y;
		
		this.xVelocity = 0;
		this.yVelocity = 0;
		this.zVelocity = 0;

		this.gameObjects = gameObjects;
		this.actionData = new LinkedHashMap<GameObjectState, ActionData>();
		this.setupAnimRef();
		this.mapActionData(actionData);
		
		currentAction = this.actionData.get(initState);

		this.width = currentAction.getPlaneData().getWidth();
		this.height = currentAction.getPlaneData().getHeight();
		this.hitActive = false;
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
	
	public void switchAnimation(GameObjectState animToSwitch){
		
		currentAction.drawDisable();
		currentAction.getAnimation().resetAnimation();
		currentAction = actionData.get(animToSwitch);
		currentAction.drawEnable();
		
		if(direction==Direction.RIGHT){
			currentAction.getAnimation().flipTexture(false);
		} else if(direction==Direction.LEFT){
			currentAction.getAnimation().flipTexture(true);
		}
	}
	
	public void update() {
		updateState();
		updateLogic();
		updateDisplay();
		updateAfterDisplay();
	}
	
	public void initYAccel(float speed){
		yVelocity = speed;
		y += yVelocity;
	}
	
	public void executeYAccel(float accel){
		yVelocity += accel;
		y += yVelocity;
	}

	public void initXAccel(float speed){
		xVelocity = speed;
		x += xVelocity;
		/*
		if(direction == Direction.RIGHT){
			x += xVelocity;
		}else if(direction == Direction.LEFT){
			x -= xVelocity;
		}
		*/
	}
	
	public void executeXAccel(float accel){
		xVelocity += accel;
		x += xVelocity;
		/*
		if(direction == Direction.RIGHT){
			x += xVelocity;
		}else if(direction == Direction.LEFT){
			x -= xVelocity;
		}
		*/
	}
	
	public void setGesture(Gesture gesture){
		this.gesture = gesture; 
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
	
}
