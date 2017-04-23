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
import structure.TriggerProperties;
import android.util.Log;
import android.view.MotionEvent;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import engine.open2d.texture.AnimatedTexture.Playback;
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
	public static int FRAME_BUFFER_COUNT = 5;
	
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
	
	protected int gameFrame;
	
	protected GameLogic gameLogic;
	
	protected ActionData currentAction;
	protected GameObjectLogic currentLogic;

	protected InteractionProperties interProperties;
	
	//protected Plane display;
	protected HashMap<GameObjectState, Integer> animationRef;
	protected String name;
	protected boolean initSpeed;
	protected boolean resetAnim;
	public boolean selected;
	protected boolean hitActive;	//determines if hitbox is available
	protected boolean hitAvailable; //used to disable hit box on next update
	protected Gesture input;
	protected Gesture saveHoldState;
	protected float frameBufferCount;
	//protected Gesture gesture;
	
	protected int hitStopFrames;
	
	protected int hitStunFrames;
	protected int hitTotalStunFrames;
	protected boolean inHitStun;
	
	protected LinkedHashMap<String,GameObject> gameObjects;
	protected LinkedHashMap<GameObjectState, ActionData> actionData;
	
	public GameObject(GameLogic logic, List<ActionData> actionData, GameObjectState initState, float x, float y){
		this.x = x;
		this.y = y;
		
		this.xVelocity = 0;
		this.yVelocity = 0;
		this.zVelocity = 0;

		this.xAccel = 0;
		this.yAccel = 0;
		this.zAccel = 0;
		
		this.gameLogic = logic;
		this.gameObjects = logic.gameObjects;
		this.actionData = new LinkedHashMap<GameObjectState, ActionData>();
		this.setupAnimRef();
		this.mapActionData(actionData);

		currentAction = this.actionData.get(initState);
		currentLogic = new GameObjectLogic();
		input = Gesture.NONE;
		saveHoldState = Gesture.HOLD_RELEASE;
		this.frameBufferCount = 0;

		this.width = currentAction.getPlaneData().getWidth();
		this.height = currentAction.getPlaneData().getHeight();
		this.hitActive = false;
		this.initSpeed = false;
		this.resetAnim = false;
		this.hitStopFrames = 0;
		this.hitStunFrames = 0;
		this.hitTotalStunFrames = 0;
		this.inHitStun = false;
	}
	
	
	public abstract void setupAnimRef();
	public abstract void mapActionData(List<ActionData> actionData);
	
	public abstract void updateState();
	public abstract void updateLogic();
	public abstract void updateDisplay();
	public abstract void updateAfterDisplay();
	public abstract void passTouchEvent(MotionEvent e, WorldRenderer worldRenderer);
	public abstract void passDoubleTouchEvent(GestureListener g, WorldRenderer worldRenderer);
	
	public abstract void setStateUsingTotalName(String state);
	
	public void updateDrawData(WorldRenderer worldRenderer){
		currentAction.updateDrawData(worldRenderer,this);
	}
	
	public void prepareGameObject(WorldRenderer worldRenderer){
		loadAnimIntoRenderer(worldRenderer);
		prepareBoxData();
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
	
	public void prepareBoxData(){
		for(ActionData data : actionData.values()){
			//offset box data to middle of frame
			for(HitBox box : data.getHitBoxes()){
				box.getBoxData().offset(-data.getPlaneData().getWidth()/2, 0);
			}

			for(HurtBox box : data.getHurtBoxes()){
				box.getBoxData().offset(-data.getPlaneData().getWidth()/2, 0);
			}
		}
	}

	//Currently not used
	public void incrementGameFrame(){
		int totalFrames = currentAction.getAnimation().getTotalFrame();
		
		gameFrame++;
		if(gameFrame >= totalFrames){
			gameFrame = 0;
			currentAction.getAnimation().setPlayed(true);
		} else if(gameFrame < 0) {
			gameFrame = totalFrames - 1;
			currentAction.getAnimation().setPlayed(true);
		}
	}
	
	public void switchAction(GameObjectState actionToSwitch){
		//Log.d(this.name, actionToSwitch.toString());
		gameFrame = 0;
		setHitAvailable(true);
		
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
		} else if(actProperties.hasModifier(ActionDataTool.CONT_SPEED)){
			interProperties = null;
			currentLogic.buildContSpeedLogic(this, actProperties);
		}else{
			interProperties = null;
			currentLogic.buildActInitSpeedLogic(actProperties);
		}
	}
	
	public void update() {
		updateState();
		updateLogic();
		updateDisplay();
		updateAfterDisplay();
	}

	protected void executeTriggers(){
		if(currentLogic.hasTrigger(ActionDataTool.WALL_TRIGGER)){
			if(isAtWall()){ 
				String state = currentLogic.getTrigger(ActionDataTool.WALL_TRIGGER);
				setStateUsingTotalName(state);
				interProperties = null;
				initSpeed = true;
			}
		}
		
		if(currentLogic.hasTrigger(ActionDataTool.GROUND_TRIGGER)){
			if(isOnGround() && !initSpeed){
				String state = currentLogic.getTrigger(ActionDataTool.GROUND_TRIGGER);
				setStateUsingTotalName(state);
				interProperties = null;
				initSpeed = true;
			}
		}
		
		if(currentLogic.hasTrigger(ActionDataTool.PLAYED_TRIGGER)){
			if(currentAction.getAnimation().isPlayed()){
				//Log.d("rising_debug", "played trigger "+currentAction.getAnimation().getFrame());
				String state = currentLogic.getTrigger(ActionDataTool.PLAYED_TRIGGER);
				setStateUsingTotalName(state);
				interProperties = null;
				initSpeed = true;
			}
		}

		if(currentLogic.hasTrigger(ActionDataTool.STOPPED_X_TRIGGER)){
			if(isXStopped()){
				String state = currentLogic.getTrigger(ActionDataTool.STOPPED_X_TRIGGER);
				setStateUsingTotalName(state);
				interProperties = null;
				initSpeed = true;
			}
		}
		
		if(currentLogic.hasTrigger(ActionDataTool.STOPPED_Y_TRIGGER)){
			if(isYStopped()){
				String state = currentLogic.getTrigger(ActionDataTool.STOPPED_Y_TRIGGER);
				setStateUsingTotalName(state);
				interProperties = null;
				initSpeed = true;
			}
		}
		
		if(currentLogic.hasTrigger(ActionDataTool.CONTINUOUS_TRIGGER)){
			String state = currentLogic.getTrigger(ActionDataTool.CONTINUOUS_TRIGGER);
			setStateUsingTotalName(state);
			interProperties = null;
			initSpeed = true;
		}
		
		if(currentAction.getActionProperties().hasTriggerProperties(ActionDataTool.RANDOM_TRIGGER)){
			double rand = Math.random();
			TriggerProperties randomTriggers = currentAction.getActionProperties().getTriggerProperties(ActionDataTool.RANDOM_TRIGGER);
			double currentPercent = 0.0;
			for(int i = 0; i < randomTriggers.value.size(); ++i){
				double percent = randomTriggers.value.get(i);
				if((currentPercent <= rand) && (rand < currentPercent+percent)){
					String state = randomTriggers.state.get(i);
					setStateUsingTotalName(state);
					interProperties = null;
					initSpeed = true;
					break;
				}
				currentPercent += percent;
			}
		}
	}
	
	public void executeLogic(){
		if(currentAction.getActionProperties().hasModifier(ActionDataTool.ACTIVE_AFTER)){
			if(currentAction.getAnimation().getFrame() < currentAction.getActionProperties().getModifier(ActionDataTool.ACTIVE_AFTER)){
				return;
			}
		}

		if(currentAction.getActionProperties().hasModifier(ActionDataTool.ACTIVE_BEFORE)){
			if(currentAction.getAnimation().getFrame() > currentAction.getActionProperties().getModifier(ActionDataTool.ACTIVE_BEFORE)){
				return;
			}
		}
		
		if(initSpeed){
			float initxSpeed = currentLogic.getxInitSpeed();
			float initySpeed = currentLogic.getyInitSpeed();
			float xAccel = currentLogic.getxAccel();
			float yAccel = currentLogic.getyAccel();
			
			if(currentAction.getActionProperties().hasModifier(ActionDataTool.REVERSE_X)){
				initxSpeed = -1*initxSpeed;
			}
			
			boolean cont_x_speed = 	 currentAction.getActionProperties().hasModifier(ActionDataTool.CONT_SPEED) && 
									(currentAction.getActionProperties().getModifier(ActionDataTool.CONT_SPEED) == ActionDataTool.CONT_SPEED_X_DIR ||
									 currentAction.getActionProperties().getModifier(ActionDataTool.CONT_SPEED) == ActionDataTool.CONT_SPEED_BOTH_DIR);
			if(!cont_x_speed){//if not continuing speed set init speed in correct direction
				if(direction == Direction.RIGHT){
					initxSpeed = initxSpeed;
				} else if(direction == Direction.LEFT){
					initxSpeed = -1*initxSpeed;
				}
			}
			
			xAccel = getAccelFromSpeed(initxSpeed, xAccel);

			initXPhys(initxSpeed, xAccel);
			initYPhys(initySpeed, yAccel);
			initSpeed = false;

			//executeXPhys();
			//executeYPhys();
			//return;
		}

		/*
		if(isXStopped() || isAtWall()){
			initXPhys(0, 0);
		} else {
			executeXPhys();
		}
		*/
		if(!currentLogic.hasTrigger(ActionDataTool.STOPPED_X_TRIGGER) && isXStopped()){
			initXPhys(0, 0);
		} else if(!currentLogic.hasTrigger(ActionDataTool.WALL_TRIGGER) && isAtWall()){
			initXPhys(0, 0);
		} else {
			executeXPhys();
		}
		
		
		if(currentAction.getActionProperties().hasModifier(ActionDataTool.SNAP_TO_FLOOR)){
			if(!currentLogic.hasTrigger(ActionDataTool.GROUND_TRIGGER) && isOnGround()){
				y = GameLogic.FLOOR;
				initYPhys(0, 0);
			}
		}

		executeYPhys();
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

	public void setGesture(Gesture gesture){
		//if(inputList.size() <= INPUT_LIST_SIZE){
			input = gesture;
			frameBufferCount = Math.max(10, currentAction.getActionProperties().getCancelFrame().size());
		//}
	}
	
	public void saveHoldState(Gesture gesture){
		if(gesture.isHold() || gesture.isRelease())
			saveHoldState = gesture;
	}
	
	public boolean isInAir(){
//		List<HurtBox> hurtBoxes = currentAction.getHurtBoxes();
//		float lowest = y + height;
//		for(HurtBox box : hurtBoxes){
//			if(y+box.getBoxData().bottom <= lowest)
//				lowest = y+box.getBoxData().bottom;
//		}
//		
//		if(lowest > GameLogic.FLOOR)
//			return true;
//
//		if(hurtBoxes.isEmpty()){
//			//hard code offset bad bad bad
//			if(y+0.01f <= GameLogic.FLOOR){
//			//if(y <= GameLogic.FLOOR){
//				return true;
//			}
//		}
		
		if(getOffsetY() > GameLogic.FLOOR)
			return true;

		return false;
	}
	
	public boolean isOnGround(){
		//check using lowest hurt box
		//List<HurtBox> hurtBoxes = currentAction.getHurtBoxes();
		//for(HurtBox box : hurtBoxes){
		//	if(y+box.getBoxData().bottom <= GameLogic.FLOOR)
		//		return true;
		//}

		//if(hurtBoxes.isEmpty())
		if(getOffsetY() <= GameLogic.FLOOR){
			return true;
		}
		//}

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
		}
		
		if(hurtBoxes.isEmpty()){
			if(xVelocity > 0 && x+width/2 >= GameLogic.WALL_RIGHT)
				return true;
			
			if(xVelocity < 0 && x-width/2 <= GameLogic.WALL_LEFT)
				return true;
		}
		
		return false;
	}
	
	public boolean isXStopped(){
		if(xAccel > 0 && xVelocity > 0){
			return true;
		}else if(xAccel < 0 && xVelocity < 0){
			return true;
		} else if(xAccel == 0 && xVelocity == 0){
			return true;
		}
		
		return false;
	}
	
	public boolean isYStopped(){
		if(yAccel > 0 && yVelocity > 0){
			return true;
		}else if(yAccel < 0 && yVelocity < 0){
			return true;
		} else if(yAccel == 0 && yVelocity == 0){
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
	
	public float getOffsetX(){
		return x+currentAction.getActionProperties().getxPtOffset();
	}
	
	public float getOffsetY(){
		return y+currentAction.getActionProperties().getyPtOffset();
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


	public boolean isHitAvailable() {
		return hitAvailable;
	}


	public void setHitAvailable(boolean hitAvailable) {
		this.hitAvailable = hitAvailable;
	}


	public int getHitStopFrames() {
		return hitStopFrames;
	}


	public void activateHitStop(int hitStopFrames) {
		this.hitStopFrames = hitStopFrames;
		if(hitStopFrames > 0)
			currentAction.getAnimation().setPlayback(Playback.PAUSE);
	}

	public void deactivateHitStop() {
		this.hitStopFrames = 0;
		Playback defaultPlayback = currentAction.getPlaneData().getPlayback();
		currentAction.getAnimation().setPlayback(defaultPlayback);
	}
	
	public int getHitStunFrames(){
		return this.hitStunFrames;
	}
	
	public void activateHitStun(int hitstun){
		this.hitStunFrames = hitstun;
		this.hitTotalStunFrames = hitstun;
		this.inHitStun = true;
		if(hitStunFrames > 0)
			currentAction.getAnimation().setPlayback(Playback.PAUSE);
	}
	
	public void deactivateHitStun() {
		this.hitStunFrames = 0;
		this.hitTotalStunFrames = 0;
		this.inHitStun = false;
		Playback defaultPlayback = currentAction.getPlaneData().getPlayback();
		currentAction.getAnimation().setPlayback(defaultPlayback);
	}
	
	public int getTotalHitStunFrames(){
		return this.hitTotalStunFrames;
	}
	
	public void setTotalHitStunFrames(int hitstun){
		this.hitTotalStunFrames = hitstun;
	}
	

	public boolean isInHitStun() {
		return inHitStun;
	}


	public void setInHitStun(boolean inHitStun) {
		this.inHitStun = inHitStun;
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
