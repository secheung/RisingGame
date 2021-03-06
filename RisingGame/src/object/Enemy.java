package object;

import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import engine.open2d.texture.AnimatedTexture.Playback;
import game.GameLogic;
import game.GameTools;
import game.GestureListener;
import game.GameTools.Gesture;
import game.open2d.R;
import junit.framework.Assert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import object.GameObject.Direction;
import object.GameObject.GameObjectState;
import object.Player.PlayerState;
import structure.ActionData;
import structure.ActionDataTool;
import structure.HitBox;
import structure.HurtBox;
import structure.InteractionProperties;
import structure.TriggerProperties;

public class Enemy extends GameObject {
	public static enum EnemyState implements GameObjectState{
		TEMP("temp"),
		BEHAVIOUR("behaviour"),//special state for frame that gets triggered set state using logic in game
		STAND("stand"),
		STRIKE1("strike"),
		STRUCK("struck"),
		STAGGER1("stagger1"),
		AIR_GRABBED("air_grabbed"),
		ATTACK1("attack1"),
		NFINISH1("n_finish1"),
		KNOCK_BACK("knock_back"),
		KNOCK_DOWN("knock_down"),
		KNOCK_DOWN_FORWARD("knock_down_forward"),
		TRIP_FORWARD("trip_forward"),
		KNOCK_UP("knock_up"),
		HOVER("hover"),
		WALL_BOUNCE("wall_bounce"),
		RUN("run"),
		WALK("walk"),
		JUMP_BACK("jump_back"),
		CROSS_ROLL("cross_roll"),
		DODGE("dodge"),
		FREEZE("freeze"),
		DEAD("dead");
		
		static String OBJECT = "enemy";
		String name;
		
		public static EnemyState getStateFromTotalName(String name){
			for(EnemyState enemyState : EnemyState.values()){
				if(name.equals(enemyState.getTotalName())){
					return enemyState;
				}
			}
			return null;
		}
		
		EnemyState(String n){
			name = n;
		}
		
		public String getTotalName(){
			return OBJECT+"_"+name;
		}
		
		public String getName(){
			return name;
		}
	}
	
	private static String OBJNAME = "enemy";
	
	private static EnemyState INIT_STATE = EnemyState.STAND;
	
	private static final int TEMP_FRAME = 19;
	
	private static float RUN_SPEED = 0.16f;
	private static float WALK_SPEED = 0.08f;
	private static float JUMP_BACK_SPEED = 0.15f;
	private static float CROSS_ROLL_SPEED = 0.17f;
	private static float FAR_DIST_TO_PLAYER = 2.5f;
	private static float CLOSE_DIST_TO_PLAYER = 1.5f;
	private static float COLLISION_BUFFER = 1.0f;
	private static float KNOCK_BACK_SPEED = 1.0f;
	private static float KNOCK_BACK_DECEL = -0.1f;
	private static float JUMP_SPEED = 0.45f;
	public static float COLLISION_FRAME = 20.0f;
	
	private static int NEUTRAL_BEHAVIOUR_TICK_TRIGGER = 20;
	
	Player playerRef;
	EnemyState enemyState;
	
	boolean isDefence;//used for enemy dodges/defense
	
	public static int INIT_DEFENCE_MOVES = 1;
	public int defenciveMoves = INIT_DEFENCE_MOVES;
	
	public int neutralBehaviourTick = 0;
	public boolean disableBehaviour = false;
	public boolean disableDefence = false;
	
	//public Enemy(LinkedHashMap<String,GameObject> gameObjects, Player player, int index, float x, float y, float width, float height){
	public Enemy(GameLogic logic, List<ActionData> actionData, Player player, int index, float x, float y){
		super(logic,actionData,INIT_STATE,x,y);
		
		enemyState = INIT_STATE;
		this.name = OBJNAME+index;
		this.z = -1.0f/*+index*0.01f*/;
		this.playerRef = player;
		
		//display = animations.get(EnemyState.STAND);
		//this.currentAction = this.actionData.get(INIT_STATE);
		
		currentAction.drawEnable();
		this.direction = Direction.RIGHT;
		
		this.defenciveMoves = INIT_DEFENCE_MOVES;
		this.neutralBehaviourTick = 0;
		
		switchAction(enemyState);//initialze state
	}

	@Override
	public void setupAnimRef() {
		animationRef = new HashMap<GameObjectState, Integer>();
		animationRef.put(EnemyState.TEMP, R.drawable.enemy_strike1);
		animationRef.put(EnemyState.STAND, R.drawable.enemy_stance);
		animationRef.put(EnemyState.FREEZE, R.drawable.enemy_stance);
		animationRef.put(EnemyState.RUN, R.drawable.enemy_run);
		animationRef.put(EnemyState.WALK, R.drawable.enemy_run);
		animationRef.put(EnemyState.JUMP_BACK, R.drawable.enemy_jump_back);
		animationRef.put(EnemyState.CROSS_ROLL, R.drawable.enemy_cross_roll);
		animationRef.put(EnemyState.DEAD, R.drawable.enemy_stance);
		animationRef.put(EnemyState.STRIKE1, R.drawable.enemy_strike1);
		animationRef.put(EnemyState.STRUCK, R.drawable.enemy_struck);
		animationRef.put(EnemyState.STAGGER1, R.drawable.enemy_stagger1);
		animationRef.put(EnemyState.AIR_GRABBED, R.drawable.enemy_air_grabbed);
		animationRef.put(EnemyState.NFINISH1, R.drawable.enemy_n_finish1);
		
		animationRef.put(EnemyState.KNOCK_BACK, R.drawable.enemy_knock_back);
		animationRef.put(EnemyState.KNOCK_DOWN, R.drawable.enemy_knock_down);
		animationRef.put(EnemyState.TRIP_FORWARD, R.drawable.enemy_trip_forward);
		animationRef.put(EnemyState.KNOCK_DOWN_FORWARD, R.drawable.enemy_knock_down_forward);
		animationRef.put(EnemyState.KNOCK_UP, R.drawable.enemy_knock_up);
		animationRef.put(EnemyState.HOVER, R.drawable.enemy_hover);
		animationRef.put(EnemyState.WALL_BOUNCE, R.drawable.enemy_wall_bounce);
		
		animationRef.put(EnemyState.STRIKE1, R.drawable.enemy_strike1);
		animationRef.put(EnemyState.ATTACK1, R.drawable.enemy_strike1);
		animationRef.put(EnemyState.DODGE, R.drawable.enemy_cross_roll);
	}

	@Override
	public void mapActionData(List<ActionData> actionData) {
		for(ActionData data : actionData){
			EnemyState state = EnemyState.getStateFromTotalName(data.getName());
			if(state != null){
				int refID = animationRef.get(state);
				data.createAnimation(refID);
				this.actionData.put(state, data);
			}
		}
	}

	@Override
	public void interactionPreCheck(){
		if(hitStopFrames > 0){
			return;
		}
		
		interactionHitObject = onHitCheck();
		if(getHitActive() && (interactionHitObject == playerRef)){
			interactionHitSnapShot = new InteractionProperties(currentAction.getInterProperties());
			onHit = true;
		}else{
			interactionHitSnapShot = null;
			onHit = false;
		}
		
		if(isHit(playerRef)){
			//executeGetHit();
			isHit = true;
			interactionHitterSnapShot = new InteractionProperties(playerRef.currentAction.getInterProperties());
			interactionHitterObject = playerRef;
			
			if(!disableDefence && 
				defenciveMoves > 0 && 
				currentLogic.hasTrigger(ActionDataTool.ON_DEFENSE_HIT_TRIGGER))
				isDefence = true;
		}else{
			isHit = false;
			interactionHitterSnapShot = null;
			interactionHitterObject = null;
		}
		
		
		if(currentAction.getActionProperties().hasModifier(ActionDataTool.GRAB_TYPE)){
			grab_flag = true;
			grabObject = playerRef;
		}else{
			grab_flag = false;
			grabObject = null;
		}
		
	}
	
	@Override
	public void updateState() {
		if(hitStopFrames > 0){
			return;
		}
		
		float checkX = getMidX();
		if(onHit){
			executeOnHit();
			if(hitStopFrames > 0)
				return;	
		}
		else if(isHit){
			if(isDefence && !disableDefence){
				executeDefenciveMove();
			}else{
				executeGetHit();
				
				//reset defencive moves on hit
				defenciveMoves = INIT_DEFENCE_MOVES;
			}
		}else{
			EnemyState prevState = enemyState;
			executeTriggers();
			if(enemyState == EnemyState.BEHAVIOUR){
				enemyState = prevState;//restore previous state from before behaviour state happen
				executeBehaviour();
			}
		}

		if(	currentAction != actionData.get(enemyState) || resetAnim){
			switchAction(enemyState);
			resetAnim = false;
		}
		
		//if(isHit){
		//	this.activateHitStop(interProperties.getHitStop());
		//	this.activateHitStun(interProperties.getHitStun());
		//}
		
		if(		enemyState != EnemyState.STAND && 
				enemyState != EnemyState.RUN && 
				enemyState != EnemyState.WALK){
			//Log.d(enemyState.toString(),"xVel "+xVelocity+" xAccel "+xAccel);
			//Log.d(enemyState.toString(),"yVel "+yVelocity+" yAccel "+yAccel);
		}
	}

	public void executeDefenciveMove(){
		isDefence = false;
		if(defenciveMoves > 0 && currentLogic.hasTrigger(ActionDataTool.ON_DEFENSE_HIT_TRIGGER)){
			--defenciveMoves;
			
			String state = currentLogic.getTrigger(ActionDataTool.ON_DEFENSE_HIT_TRIGGER);
			setStateUsingTotalName(state);
			interProperties = null;
			initSpeed = true;
			
			//random side move(should this be here?)
			double side_select = Math.random();
			if(side_select > 0.5)
				direction = Direction.RIGHT;
			else
				direction = Direction.LEFT;
		}
	}
	
	public void executeBehaviour(){
		if(disableBehaviour)
			return;
		
		neutralBehaviourTick++;
		
		boolean behaviourTick = (neutralBehaviourTick > NEUTRAL_BEHAVIOUR_TICK_TRIGGER);
		if(behaviourTick)
			neutralBehaviourTick = 0;
		
		//execute behaviour every at tick count
		//simple behaviour
		if(enemyState == EnemyState.STAND){
			if(behaviourTick){
				double rand = Math.random();
				double currentPercent = 0.1;
				if(currentPercent <= rand){
					String state = EnemyState.RUN.getName();
					setStateUsingTotalName(state);
					interProperties = null;
					initSpeed = true;
					//return;
				}
			}
		}
		
		if(enemyState == EnemyState.RUN){
			boolean change_to_stand = false;
			if(behaviourTick){
				double rand = Math.random();
				double currentPercent = 0.5;
				change_to_stand = (currentPercent <= rand);
			}
			
			double playerX = playerRef.getMidX();
			double currentX = getMidX();
			double distance = 1.25;
			
			if(Math.abs(playerX - currentX) < distance || change_to_stand){
				setStateUsingTotalName(EnemyState.STAND.getName());
				interProperties = null;
				initSpeed = true;
				//return;
			}	
		}
		
		if(enemyState == EnemyState.STAND || enemyState == EnemyState.RUN){
			//if(behaviourTick){
				double rand = Math.random();
				double currentPercent = 0.1;
				
				boolean close_to_player = Math.abs(playerRef.getMidX() - getMidX()) < 1.75; 
				if(/*currentPercent <= rand &&*/ close_to_player){
					String state = EnemyState.ATTACK1.getName();
					setStateUsingTotalName(state);
					interProperties = null;
					initSpeed = true;
					//return;
				}
			//}
		}
		
		/*
		if(currentAction.getActionProperties().hasTriggerProperties(ActionDataTool.DISTANCE_TRIGGER)){
			TriggerProperties triggerProps = currentAction.getActionProperties().getTriggerProperties(ActionDataTool.DISTANCE_TRIGGER);
			double playerX = playerRef.getMidX();
			double currentX = getMidX();
			for(int i = 0; i < triggerProps.value.size(); ++i){
				double distance = triggerProps.value.get(i);
				if(Math.abs(playerX - currentX) < distance){
					String state = triggerProps.state.get(i);
					setStateUsingTotalName(state);
					interProperties = null;
					initSpeed = true;
					break;
				}
			}	
		}
		*/
		
		//Executing Behaviour must end in state of behaviour  
		Assert.assertTrue(enemyState != EnemyState.BEHAVIOUR);
	}
	
	@Override
	public void updateLogic() {
		if(isHitAvailable()){
			this.setHitActive(true);
		} else {
			this.setHitActive(false);
		}
		
		if(hitStopFrames > 0){
			hitStopFrames--;
			return;
		}

		if(hitStunFrames > 0){
			hitStunFrames--;
		}else{
			deactivateHitStun();
		}

		if(enemyState== EnemyState.TEMP){
			currentAction.getAnimation().setFrame(TEMP_FRAME - 1);
			direction = Direction.RIGHT;
		}
		
		//if(enemyState == EnemyState.STAND){
        //
		//}else if(isStrikeState()){
		//	
		//}else if(enemyState == EnemyState.RUN){
		//	if(direction == Direction.RIGHT)
		//		x += RUN_SPEED;
		//	else if(direction == Direction.LEFT)
		//		x -= RUN_SPEED;
		//}else if(enemyState == EnemyState.WALK){
		//	if(direction == Direction.RIGHT)
		//		x += WALK_SPEED;
		//	else if(direction == Direction.LEFT)
		//		x -= WALK_SPEED;
		//}else if(enemyState == EnemyState.JUMP_BACK){
		//	if(direction == Direction.RIGHT)
		//		x -= JUMP_BACK_SPEED;
		//	else if(direction == Direction.LEFT)
		//		x += JUMP_BACK_SPEED;
		//}else if(enemyState == EnemyState.CROSS_ROLL){
		//	if(direction == Direction.RIGHT)
		//		x += CROSS_ROLL_SPEED;
		//	else if(direction == Direction.LEFT)
		//		x -= CROSS_ROLL_SPEED;
		//}else {
		//	executeLogic();
		//	//Log.d(enemyState.toString(),"xVel"+xVelocity+" xAccel "+xAccel);
		//}
		
		if(currentAction.getActionProperties().hasModifier(ActionDataTool.FACE_PLAYER)){
			float checkX = getMidX();
			if(playerRef.getMidX() > checkX){
				direction = Direction.RIGHT;
			} else if(playerRef.getMidX() < checkX){
				direction = Direction.LEFT;
			}
			
			boolean follow = (currentAction.getActionProperties().getModifier(ActionDataTool.FACE_PLAYER) == ActionDataTool.FACE_PLAYER_FOLLOW);
			if(follow){//constantly update speed to follow direction of player
				initSpeed = true;
			}
		}
		
		
		
		executeLogic();
	}

	@Override
	public void updateDisplay() {
		if(hitStopFrames > 0){
			return;
		}else if(inHitStun){
			double percent = 1 - (double)hitStunFrames/hitTotalStunFrames; 
			int setFrame = (int) ((currentAction.getAnimation().getTotalFrame()-1)*percent);
			currentAction.getAnimation().setFrame(setFrame);
			currentAction.getAnimation().setPlayback(Playback.PAUSE);
		} else {
			Playback defaultPlayback = currentAction.getPlaneData().getPlayback();
			currentAction.getAnimation().setPlayback(defaultPlayback);
		}
		
		if(direction==Direction.RIGHT){
			currentAction.flipHorizontal(false);
		} else if(direction==Direction.LEFT){
			currentAction.flipHorizontal(true);
		}
		
		if(enemyState == EnemyState.DEAD){
			currentAction.drawDisable();
		}
		
		if(enemyState == EnemyState.FREEZE){
			currentAction.drawDisable();
		}
		
		//incrementGameFrame();
		//currentAction.getAnimation().setFrame(gameFrame);
	}

	@Override
	public void updateAfterDisplay() {
		Plane display = currentAction.getAnimation();
		if(!display.isPlayed())
			return;
		
		if(isStrikeState()){
			display.resetAnimation();
			enemyState = EnemyState.STAND;
		} else if(enemyState == EnemyState.JUMP_BACK){
			display.resetAnimation();
			enemyState = EnemyState.STAND;
		} else if(enemyState == EnemyState.CROSS_ROLL){
			display.resetAnimation();
			enemyState = EnemyState.STAND;
		} else if(enemyState == EnemyState.KNOCK_DOWN || enemyState == EnemyState.KNOCK_DOWN_FORWARD){
			//display.resetAnimation();
			//struck -= 1;
			//enemyState = EnemyState.STAND;
		}
	}
	
	@Override
	public void passTouchEvent(MotionEvent e, WorldRenderer worldRenderer) {
		Plane display = currentAction.getAnimation();
		Plane selectedPlane = worldRenderer.getSelectedPlane(e.getX(), e.getY());

		float[] points = worldRenderer.getUnprojectedPoints(e.getX(), e.getY(), display);
		if(currentAction.getAnimation().equals(selectedPlane)){
			//Log.d("enemy", "selected");
			for(HurtBox playerHurtBox : playerRef.getCurrentAction().getHurtBoxes()){
				for(HurtBox ownHurtBox : currentAction.getHurtBoxes()){
					if(	GameTools.boxColDetect(ownHurtBox.getBoxData(), this, playerHurtBox.getBoxData(), playerRef)
						//&& GameTools.boxContains(ownHurtBox.getBoxData(), points[0] - this.getX(), points[1] - this.getY())
					){
						selected = true;
					}
				}
			}
		} else {
			selected = false;
		}
		
		if(enemyState == EnemyState.TEMP){
			//float[] coord = worldRenderer.getUnprojectedPoints(e.getX(), e.getY(), this.getDisplay());
			//Log.d("debug",coord[0] + " " + coord[1] + " " + coord[2]);
			float[] coord = worldRenderer.getUnprojectedPoints(e.getX(), e.getY(), this.getDisplay());
			Log.i("rising_debug","x: "+coord[0] + " y: " + coord[1] + " z: " + coord[2] +" "+getMidX());
		}
		
//		if(enemyState != EnemyState.FREEZE)
//			selected = checkEnemySelection(points[0],points[1]);
		
//		if(animations.containsValue(selectedPlane)){
//			selected = true;
//		}
		
//		if(gesture != Gesture.NONE){
//			selected = false;
//		}
	}

	public void passDoubleTouchEvent(GestureListener g,  WorldRenderer worldRenderer){

	}

	public void executeMovement(){
		float checkX = getMidX();
		
		if(Math.abs(checkX - playerRef.getMidX()) > CLOSE_DIST_TO_PLAYER){
			enemyState = EnemyState.WALK;
		}
		
		if(Math.abs(checkX - playerRef.getMidX()) > FAR_DIST_TO_PLAYER){
			enemyState = EnemyState.RUN;
		}
		
		if(GameTools.boxColDetect(this, playerRef, COLLISION_BUFFER)){
			enemyState = EnemyState.STAND;
		}
	}
	
	public void otherAIMoveInteration(){
		if(isDodging())
			return;
		
		int counter = 0;
		for(GameObject gameObject : gameObjects.values()){
			counter++;
			if(gameObject instanceof Enemy){
				Enemy enemy = (Enemy)gameObject;
				if(enemy != this && GameTools.boxColDetect(this, enemy, COLLISION_BUFFER)){
					if(	enemy.getEnemyState() != EnemyState.WALK &&
						enemy.getEnemyState() != EnemyState.RUN &&
						!enemy.isDodging()){
						if(counter % 2 == 1){
							enemyState = EnemyState.JUMP_BACK;
						} else {
							enemyState = EnemyState.CROSS_ROLL;
						}
					}
				}
			}
		}
	}
	
	public boolean isStrikingPlayer(){
		return ((GameTools.boxColDetect(	playerRef, -COLLISION_BUFFER, COLLISION_BUFFER, COLLISION_BUFFER, -COLLISION_BUFFER,
											this, -1.0f, 1.0f, 1.5f,0.1f) && direction == Direction.RIGHT) ||
				(GameTools.boxColDetect(	playerRef, -COLLISION_BUFFER, COLLISION_BUFFER, COLLISION_BUFFER, -COLLISION_BUFFER,
											this, -1.0f, 1.0f, -0.1f,-1.5f) && direction == Direction.RIGHT));
	}
	
	private boolean checkEnemySelection(float xPoint,float yPoint){
//		float top = y + height - COLLISION_BUFFER;
//		float bottom = y + COLLISION_BUFFER;
		float top = y + height;
		float bottom = y;
		float left = x + COLLISION_BUFFER;
		float right = x + width - COLLISION_BUFFER;

		return(xPoint > left && xPoint < right && yPoint > bottom && yPoint < top);	
	}
	
	public boolean isStrikeState(){
		if(enemyState == EnemyState.STRIKE1){
			return true;
		} 
		
		return false;
		
	}
	
	public boolean isDodging(){
		if(	enemyState == EnemyState.JUMP_BACK ||
			enemyState == EnemyState.CROSS_ROLL
		){
			return true;
		}
		
		return false;
	}
	
	public EnemyState getEnemyState() {
		return enemyState;
	}

	public void setEnemyState(EnemyState enemyState) {
		this.enemyState = enemyState;
	}
	
	public void setStateUsingTotalName(String state){
		StringBuffer buffer = new StringBuffer(EnemyState.OBJECT);
		buffer.append("_");
		buffer.append(state);
		EnemyState changeState = EnemyState.getStateFromTotalName(buffer.toString()); 
		if(enemyState == null){
			Log.w(EnemyState.OBJECT, "can't find state "+buffer.toString());
		}
		this.enemyState = changeState;
	}
	
	public String getStateName(){
		return enemyState.getName();
	}
	
	public boolean isSelected() {
		return selected;
	}
}
