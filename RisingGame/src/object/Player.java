package object;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import object.Enemy.EnemyState;
import object.GameObject.Direction;
import object.GameObject.GameObjectState;
import structure.ActionData;
import structure.ActionDataTool;
import structure.HitBox;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import engine.open2d.texture.AnimatedTexture.Playback;
import game.GameLogic;
import game.GameLogic.CONTROL_TYPE;
import game.GameTools;
import game.GestureListener;
import game.GameTools.Gesture;
import game.open2d.R;

public class Player extends GameObject{
	public static enum PlayerState implements GameObjectState{
		TEMP("temp"),
		STAND("stand"),
		RUN("run"),
		JUMP("jump_startup"),
		ARC("jump_arc"),
		LAND("jump_land"),
		DASH("dash"),
		DODGE("dodge"),
		DEAD("dead"),
		NTAP("n_tap"),
		NFSWIPECOMBO1("n_fswipe_combo1"),
		NFSWIPECOMBO2("n_fswipe_combo2"),
		NFSWIPE("n_fswipe"),
		NUSWIPE("n_uswipe"),
		NDSWIPE("n_dswipe"),
		AFSWIPE("a_fswipe"),
		AUSWIPE("a_uswipe"),
		ADSWIPE("a_dswipe"),
		DFSWIPE("d_fswipe"),
		DUSWIPE("d_uswipe"),
		DDSWIPE("d_dswipe"),
		DDSWIPEFOLLWUP("d_dswipe_followup");
		
		static String OBJECT = "jack";
		String name;
		
		public static PlayerState getStateFromTotalName(String name){
			for(PlayerState playerState : PlayerState.values()){
				if(name.equals(playerState.getTotalName())){
					return playerState;
				}
			}
			return null;
		}
		
		PlayerState(String n){
			name = n;
		}
		
		public String getTotalName(){
			return OBJECT+"_"+name;
		}
		
		public String getName(){
			return name;
		}
	}

	public static String OBJNAME = "player";
	private static PlayerState INIT_STATE = PlayerState.STAND;

	private static final int TEMP_FRAME = 5;

	private static float WALK_SPEED = 0.2f;
	private static float STRIKE_SPEED = 0.1f;
	private static float NFSWIPE_SPEED = 0.2f;
	private static float DODGE_SPEED = 0.23f;
	private static float BUFFER = 0.4f;
	private static float COLLISION_BUFFER = 1.0f;
	public static float CANCEL_STRIKE_FRAMES = 8;
	private static float JUMP_SPEED = 0.75f;
	
	public static float SCREEN_HEIGHT_PERCENTAGE = 0.60f;
	public static float SCREEN_WIDTH_PERCENTAGE = 0.80f;
	
	private Enemy struckEnemy;
	private PlayerState playerState;
	private float moveToX;
	private float moveToY;
	
	CONTROL_TYPE controlType; 
	
	private float cursorX;
	private float cursorY;
	
	private float unprojectedX;
	private float unprojectedY;
	
	private boolean inputHold;
	
	private int punchIndex;
	private int finishIndex;
	private int counterIndex;
	
	public Player(LinkedHashMap<String,GameObject> gameObjects, List<ActionData> actionData, float x, float y, CONTROL_TYPE controlType){
		super(gameObjects,actionData,INIT_STATE,x,y);
		
		playerState = INIT_STATE;
		this.name = OBJNAME;
		
		this.moveToX = x;
		this.moveToY = y;
		this.z = -1.0f;
		
		this.controlType = controlType;
		
		this.punchIndex = 1;
		this.finishIndex = 1;
		this.counterIndex = 1;
		
		//this.currentAction = this.actionData.get(INIT_STATE);
		this.currentAction.drawEnable();
		this.direction = Direction.LEFT;
	}
	
	@Override
	public void setupAnimRef() {
		animationRef = new HashMap<GameObjectState, Integer>();
		//animationRef.put(PlayerState.STAND, new Plane(R.drawable.rising_stance, name+"_"+PlayerState.STAND.getName(), width, height, 4, 7));
		animationRef.put(PlayerState.TEMP, R.drawable.jack_n_combo2);
		animationRef.put(PlayerState.STAND, R.drawable.jack_stand);
		animationRef.put(PlayerState.DEAD, R.drawable.rising_stance);
		animationRef.put(PlayerState.RUN, R.drawable.jack_run);
		animationRef.put(PlayerState.JUMP, R.drawable.jack_jump_startup);
		animationRef.put(PlayerState.ARC, R.drawable.jack_jump_arc);
		animationRef.put(PlayerState.LAND, R.drawable.jack_jump_land);
		animationRef.put(PlayerState.DASH, R.drawable.jack_dash);
		animationRef.put(PlayerState.DODGE, R.drawable.rising_dodge);
		animationRef.put(PlayerState.NTAP, R.drawable.jack_n_tap);
		animationRef.put(PlayerState.NFSWIPE, R.drawable.jack_n_fswipe);
		animationRef.put(PlayerState.NFSWIPECOMBO1, R.drawable.jack_n_combo1);
		animationRef.put(PlayerState.NFSWIPECOMBO2, R.drawable.jack_n_combo2);
		animationRef.put(PlayerState.NUSWIPE, R.drawable.jack_n_uswipe);
		animationRef.put(PlayerState.NDSWIPE, R.drawable.jack_n_dswipe);
		animationRef.put(PlayerState.AFSWIPE, R.drawable.jack_a_fswipe);
		animationRef.put(PlayerState.AUSWIPE, R.drawable.jack_d_uswipe);
		animationRef.put(PlayerState.ADSWIPE, R.drawable.jack_a_dswipe);
		animationRef.put(PlayerState.DFSWIPE, R.drawable.jack_d_fswipe);
		animationRef.put(PlayerState.DUSWIPE, R.drawable.jack_d_uswipe);
		animationRef.put(PlayerState.DDSWIPE, R.drawable.jack_d_dswipe);
		animationRef.put(PlayerState.DDSWIPEFOLLWUP, R.drawable.jack_d_dswipe_followup);
	}
	
	@Override
	public void mapActionData(List<ActionData> actionData) {
		for(ActionData data : actionData){
			PlayerState state = PlayerState.getStateFromTotalName(data.getName());
			if(state != null){
				int refID = animationRef.get(state);
				data.createAnimation(refID);
				this.actionData.put(state, data);
			}
		}
		
	}
	
	public PlayerState getPlayerState() {
		return playerState;
	}

	public void setStateUsingTotalName(String state){
		StringBuffer buffer = new StringBuffer(PlayerState.OBJECT);
		buffer.append("_");
		buffer.append(state);
		if(playerState == PlayerState.getStateFromTotalName(buffer.toString())){
			if(playerState != PlayerState.RUN)//TODO: VERY TEMP SHOULD BE A CONTINUE ANIMATION IN MODIFIER
				resetAnim = true;
		}else{
			this.playerState = PlayerState.getStateFromTotalName(buffer.toString());
		}
	}
	
	public void setPlayerState(PlayerState playerState) {
		if(this.playerState == playerState){
			resetAnim = true;
		}else{
			this.playerState = playerState;
		}
	}
	
	@Override
	public void passTouchEvent(MotionEvent e, WorldRenderer worldRenderer){
		Plane display = currentAction.getAnimation();
		float[] unprojectedPoints = worldRenderer.getUnprojectedPoints(e.getX(), e.getY(), display);
		/*
		//if(playerState == PlayerState.RUN || playerState == PlayerState.STAND){
		if(inputList.isEmpty()){
			moveToX = unprojectedPoints[0];
			moveToY = unprojectedPoints[1];
		}
		*/
		
		cursorX = e.getX();
		cursorY = e.getY();
		
		unprojectedX = unprojectedPoints[0];
		unprojectedY = unprojectedPoints[1];
		
		display.unprojectDisable();
		
		if(playerState == PlayerState.TEMP){
			float[] coord = worldRenderer.getUnprojectedPoints(e.getX(), e.getY(), this.getDisplay());
			Log.i("rising_debug",coord[0] + " " + coord[1] + " " + coord[2] +" "+getMidX());
		}
	}

	public void passSwipeEvent(Gesture g, WorldRenderer worldRenderer){
		int screenWidth = worldRenderer.getScreenWidth();
		int screenHeight = worldRenderer.getScreenHeight();
		Plane display = currentAction.getAnimation();
		float[] unprojectedPoints = worldRenderer.getUnprojectedPoints(g.getxTap(), g.getyTap(), display);
		
		if(controlType == CONTROL_TYPE.RELATIVE){
			if(g == Gesture.SWIPE_UP || g == Gesture.SWIPE_UP_LEFT || g == Gesture.SWIPE_UP_RIGHT){
				if(unprojectedPoints[0] > this.getX()+this.getWidth()){
					inputList.add(Gesture.SWIPE_UP_RIGHT.setXDiffSize(g.getXDiffSize()).setYDiffSize(g.getYDiffSize()));
				} else if(unprojectedPoints[0] < this.getX()){
					inputList.add(Gesture.SWIPE_UP_LEFT.setXDiffSize(g.getXDiffSize()).setYDiffSize(g.getYDiffSize()));
				} else{
					inputList.add(Gesture.SWIPE_UP.setXDiffSize(g.getXDiffSize()).setYDiffSize(g.getYDiffSize()));
				}
			} else if(g == Gesture.SWIPE_DOWN || g == Gesture.SWIPE_DOWN_LEFT || g == Gesture.SWIPE_DOWN_RIGHT){
				if(unprojectedPoints[0] > this.getX()+this.getWidth()){
					inputList.add(Gesture.SWIPE_DOWN_RIGHT.setXDiffSize(g.getXDiffSize()).setYDiffSize(g.getYDiffSize()));
				} else if(unprojectedPoints[0] < this.getX()){
					inputList.add(Gesture.SWIPE_DOWN_LEFT.setXDiffSize(g.getXDiffSize()).setYDiffSize(g.getYDiffSize()));
				} else{
					inputList.add(Gesture.SWIPE_DOWN.setXDiffSize(g.getXDiffSize()).setYDiffSize(g.getYDiffSize()));
				}
			} else {
				inputList.add(g);
			}
		}else if(controlType == CONTROL_TYPE.FIXED){
			if(g == Gesture.SWIPE_UP || g == Gesture.SWIPE_UP_LEFT || g == Gesture.SWIPE_UP_RIGHT){
				if(g.getxTap() > screenWidth*SCREEN_WIDTH_PERCENTAGE){
					inputList.add(Gesture.SWIPE_UP_RIGHT.setXDiffSize(g.getXDiffSize()).setYDiffSize(g.getYDiffSize()));
				} else if(g.getxTap() < screenWidth*SCREEN_WIDTH_PERCENTAGE){
					inputList.add(Gesture.SWIPE_UP_LEFT.setXDiffSize(g.getXDiffSize()).setYDiffSize(g.getYDiffSize()));
				}	
			} else if(g == Gesture.SWIPE_DOWN || g == Gesture.SWIPE_DOWN_LEFT || g == Gesture.SWIPE_DOWN_RIGHT){
				if(g.getxTap() > screenWidth*SCREEN_WIDTH_PERCENTAGE){
					inputList.add(Gesture.SWIPE_DOWN_RIGHT.setXDiffSize(g.getXDiffSize()).setYDiffSize(g.getYDiffSize()));
				} else if(g.getxTap() < screenWidth*SCREEN_WIDTH_PERCENTAGE){
					inputList.add(Gesture.SWIPE_DOWN_LEFT.setXDiffSize(g.getXDiffSize()).setYDiffSize(g.getYDiffSize()));
				}
			} else {
				inputList.add(g);
			}
		}
	}
	
	public void passDoubleTouchEvent(GestureListener g,  WorldRenderer worldRenderer){
		int screenWidth = worldRenderer.getScreenWidth();
		int screenHeight = worldRenderer.getScreenHeight();
		Plane display = currentAction.getAnimation();
		float[] unprojectedPoints = worldRenderer.getUnprojectedPoints(g.getDoubleTapX(), g.getDoubleTapY(), display);
		
		if(controlType == CONTROL_TYPE.RELATIVE){
			//Log.d("rising_debug",g.toString()+" "+unprojectedPoints[1]+" "+(this.getY()+this.getHeight()));
			if(unprojectedPoints[1] > (this.getY()+this.getHeight())){
			//if(unprojectedPoints[1] > this.getMidY()){
				if(unprojectedPoints[0] >= this.getX()+this.getWidth()){
					inputList.add(Gesture.DTAP_UP_RIGHT);
				} else if(unprojectedPoints[0] < this.getX()){
					inputList.add(Gesture.DTAP_UP_LEFT);
				}else{
					inputList.add(Gesture.DTAP_UP);
				}
			} else if(unprojectedPoints[0] > this.getX()+this.getWidth()){
				inputList.add(Gesture.DTAP_RIGHT);
			} else if(unprojectedPoints[0] < this.getX()){
				inputList.add(Gesture.DTAP_LEFT);
			}
		} else if(controlType == CONTROL_TYPE.FIXED){
			if(g.getDoubleTapY() < screenHeight*SCREEN_HEIGHT_PERCENTAGE){
				if(g.getDoubleTapX() > screenWidth*SCREEN_WIDTH_PERCENTAGE){
					inputList.add(Gesture.DTAP_UP_RIGHT);
				} else if(g.getDoubleTapX() < screenWidth*SCREEN_WIDTH_PERCENTAGE){
					inputList.add(Gesture.DTAP_UP_LEFT);
				}
			} else if(g.getDoubleTapX() > screenWidth*SCREEN_WIDTH_PERCENTAGE){
				inputList.add(Gesture.DTAP_RIGHT);
			} else if(g.getDoubleTapX() < screenWidth*SCREEN_WIDTH_PERCENTAGE){
				inputList.add(Gesture.DTAP_LEFT);
			}
		}

		display.unprojectDisable();
	}

	public void updateHoldTouchEvent(boolean hold, Gesture g,  WorldRenderer worldRenderer){
		int width = worldRenderer.getScreenWidth();
		int height = worldRenderer.getScreenHeight();
		Plane display = currentAction.getAnimation();
		float[] unprojectedPoints = worldRenderer.getUnprojectedPoints(g.getxTap(), g.getyTap(), display);
		
		inputHold = hold;
		//Log.d("rising_debug",g.getxTap()+" "+g.getyTap());
		if(controlType == CONTROL_TYPE.RELATIVE){
			if(hold){
				moveToX = unprojectedPoints[0];
				if(unprojectedPoints[0] > this.getX()+this.getWidth()){
					//if(!inputList.contains(Gesture.HOLD_RIGHT)){
					if(inputList.isEmpty()){
						inputList.add(Gesture.HOLD_RIGHT);
					}
				} else if(unprojectedPoints[0] < this.getX()){
					//if(!inputList.contains(Gesture.HOLD_LEFT)){
					if(inputList.isEmpty()){
						inputList.add(Gesture.HOLD_LEFT);
					}
				}
			} else {
				if(!inputList.contains(Gesture.HOLD_RELEASE)){
					inputList.add(Gesture.HOLD_RELEASE);
				}
			}
		}else if(controlType == CONTROL_TYPE.FIXED){
			if(hold){
				if(g.getxTap() > width*SCREEN_WIDTH_PERCENTAGE){
					//if(!inputList.contains(Gesture.HOLD_RIGHT)){
					if(inputList.isEmpty()){
						inputList.add(Gesture.HOLD_RIGHT);
					}
				} else if(g.getxTap() < width*SCREEN_WIDTH_PERCENTAGE){
					//if(!inputList.contains(Gesture.HOLD_LEFT)){
					if(inputList.isEmpty()){
						inputList.add(Gesture.HOLD_LEFT);
					}
				}
			} else {
				if(!inputList.contains(Gesture.HOLD_RELEASE)){
					inputList.add(Gesture.HOLD_RELEASE);
				}
			}
		}
	}
	
	@Override
	public void updateState() {
		if(hitStopFrames > 0){
			return;
		}
		
		if(playerState == PlayerState.RUN || playerState == PlayerState.STAND){
			executeMovement();
		}

		//String state1 = playerState.toString();
		executeTriggers();
		executeInput();
		/*
		String state2 = playerState.toString();
		if(!state1.equals(state2)){
			Log.d("rising_debug_update_state", currentAction.getName()+" "+currentAction.getAnimation().isPlayed()+"");
			Log.d("rising_debug_update_state",state1 +" to "+state2);
		}
		*/

		if(	currentAction != actionData.get(playerState) || resetAnim){
			this.switchAction(playerState);
			resetAnim = false;
		}
		
	}
	
	@Override
	public void updateLogic() {
		if(currentAction.isHitBoxActive() && isHitAvailable()){
			this.setHitActive(true);
		} else {
			this.setHitActive(false);
		}
		
		String hitType = currentAction.getActionProperties().getHitType(); 
		if(hitType.equals(ActionDataTool.SINGLE_HIT)){
			
		}
		
		//should hitstop return from function?
		if(hitStopFrames > 0){
			hitStopFrames--;
			this.setHitActive(false);//disable hit box for attack won't come back till next attack
			return;
		}
		
		if(playerState == PlayerState.TEMP){
			currentAction.getAnimation().setFrame(TEMP_FRAME-1);
			direction = Direction.RIGHT;
			//currentAction.getAnimation().setPlayback(Playback.PAUSE);
		}

		if(playerState == PlayerState.RUN){
			if(direction == Direction.RIGHT)
				x += WALK_SPEED;
			else if(direction == Direction.LEFT)
				x -= WALK_SPEED;
		} else if(playerState == PlayerState.LAND){
			executeLogic();
			moveToX = getMidX();
			moveToY = getMidY();
		} else if(	playerState == PlayerState.NFSWIPE||
					playerState == PlayerState.NFSWIPECOMBO1||
					playerState == PlayerState.NFSWIPECOMBO2||
					playerState == PlayerState.NUSWIPE||
					playerState == PlayerState.AFSWIPE||
					playerState == PlayerState.NTAP||
					playerState == PlayerState.DFSWIPE||
					playerState == PlayerState.DDSWIPE||
					playerState == PlayerState.DDSWIPEFOLLWUP||
					playerState == PlayerState.DASH){
			executeLogic();
			moveToX = getMidX();
		} else {
			executeLogic();
		}

		if(playerState != PlayerState.STAND && playerState != PlayerState.RUN){
			//Log.d(playerState.toString(), "playerpos "+getMidX()+" tap "+unprojectedX);
			//Log.d(playerState.toString(), "xpos "+x+" xvelocity "+xVelocity+" xaccel "+xAccel);
			//Log.d(playerState.toString(), "ypos "+y+" yvelocity "+yVelocity+" yaccel "+yAccel);
		}
	}

	@Override
	public void updateDisplay() {
		if(hitStopFrames > 0){
			//Log.d("rising_debug updateDisplay",currentAction.getAnimation().getFrame()+"");
			//currentAction.getAnimation().setPlayback(Playback.PAUSE);
			return;
		} else {
			//Playback defaultPlayback = currentAction.getPlaneData().getPlayback();
			//currentAction.getAnimation().setPlayback(defaultPlayback);
			deactivateHitStop();
		}

		if(direction==Direction.RIGHT){
			currentAction.flipHorizontal(false);
		} else if(direction==Direction.LEFT){
			currentAction.flipHorizontal(true);
		}
		
		//incrementGameFrame();
		//currentAction.getAnimation().setFrame(gameFrame);
	}
	
	@Override
	public void updateAfterDisplay() {
		Plane display = currentAction.getAnimation();
		
		if(hitStopFrames > 0){
			return;
		}
		
		if(display.isPlayed()){
			if(	playerState==PlayerState.DODGE){
				//gesture = Gesture.NONE;
				display.resetAnimation();
				playerState = PlayerState.STAND;				
			}
			
			if(playerState==PlayerState.DEAD){
				display.resetAnimation();
				playerState = PlayerState.STAND;
			}
		}
	}

	private void executeInput(){
		Gesture gesture = Gesture.NONE;
		//if(!inputList.isEmpty())
		//	Log.d("rising_debug_execute_input", inputList.toString()+" "+ playerState + " "+currentAction.getAnimation().getFrame());
		
		/*
		if(hitStopFrames > 0){
			//Log.d("rising_debug", inputList.toString()+" "+currentAction.getAnimation().getFrame());
			return;
		}
		*/
		
		HashSet<Integer> cancelFrames = currentAction.getActionProperties().getCancelFrame();
		if(!cancelFrames.isEmpty()){
			if(!cancelFrames.contains(currentAction.getAnimation().getFrame())){
				return;
			}
		}

		if(!inputList.isEmpty()){
			gesture = inputList.getFirst();
			//Log.d("rising_debug", inputList.toString()+" "+direction.toString());
			Log.d(playerState.toString(), gesture.toString()+" "+direction.toString() + " xdiff "+gesture.getXDiffSize()+" ydiff "+gesture.getYDiffSize());
			inputList.removeFirst();
		}
		

		if(gesture == Gesture.TAP){
			if(currentAction.getActionProperties().hasCancel(ActionDataTool.TAP_TRIGGER)){
				for(GameObject gameObject : gameObjects.values()){
					if(gameObject instanceof Enemy){
						Enemy enemy = (Enemy) gameObject;
						if(enemy.isSelected()){
							String cancel = currentAction.getActionProperties().getCancel(ActionDataTool.TAP_TRIGGER);
							setStateUsingTotalName(cancel);
							initSpeed = true;
							enemy.selected = false;
						}
					}
				}
			}
			/*
			if(controlType == CONTROL_TYPE.RELATIVE){
				if(!initSpeed){
					moveToX = unprojectedX;
					moveToY = unprojectedY;
				}
	
				if(unprojectedX > getMidX()){
					this.direction = Direction.RIGHT;
				}else if(unprojectedX < getMidX()){
					this.direction = Direction.LEFT;
				}
			}
			*/
		}else if(gesture == Gesture.HOLD_RIGHT){
			if(currentAction.getActionProperties().hasCancel(ActionDataTool.HOLD_PRESS_TRIGGER)){
				String cancel = currentAction.getActionProperties().getCancel(ActionDataTool.HOLD_PRESS_TRIGGER);
				setStateUsingTotalName(cancel);
				initSpeed = true;
				
				this.direction = Direction.RIGHT;
				//moveToX = getMidX() + WALK_SPEED;
			}
		}else if(gesture == Gesture.HOLD_LEFT){
			if(currentAction.getActionProperties().hasCancel(ActionDataTool.HOLD_PRESS_TRIGGER)){
				String cancel = currentAction.getActionProperties().getCancel(ActionDataTool.HOLD_PRESS_TRIGGER);
				setStateUsingTotalName(cancel);
				initSpeed = true;
				
				this.direction = Direction.LEFT;
				//moveToX = getMidX() - WALK_SPEED;
			}			
		}else if(gesture == Gesture.HOLD_RELEASE){
			if(currentAction.getActionProperties().hasCancel(ActionDataTool.HOLD_RELEASE_TRIGGER)){
				String cancel = currentAction.getActionProperties().getCancel(ActionDataTool.HOLD_RELEASE_TRIGGER);
				setStateUsingTotalName(cancel);
				//initSpeed = true;
			}			
		} else if(gesture == Gesture.DTAP_UP || gesture == Gesture.DTAP_UP_LEFT || gesture == Gesture.DTAP_UP_RIGHT){
			if(currentAction.getActionProperties().hasCancel(ActionDataTool.DTAP_U_TRIGGER)){
				String cancel = currentAction.getActionProperties().getCancel(ActionDataTool.DTAP_U_TRIGGER);
				setStateUsingTotalName(cancel);
				initSpeed = true;
				
				if(gesture == Gesture.DTAP_UP_LEFT){
					this.direction = Direction.LEFT;
				} else if(gesture == Gesture.DTAP_UP_RIGHT){
					this.direction = Direction.RIGHT;
				}
			}
		} else if(gesture == Gesture.DTAP_LEFT || gesture == Gesture.DTAP_RIGHT){
			if(currentAction.getActionProperties().hasCancel(ActionDataTool.DTAP_F_TRIGGER)){
				String cancel = currentAction.getActionProperties().getCancel(ActionDataTool.DTAP_F_TRIGGER);
				setStateUsingTotalName(cancel);
				initSpeed = true;
			}
			
			if(gesture == Gesture.DTAP_LEFT){
				this.direction = Direction.LEFT;
			} else if(gesture == Gesture.DTAP_RIGHT){
				this.direction = Direction.RIGHT;
			}
		}
		
		if(Math.abs(gesture.getXDiffSize()) > Math.abs(gesture.getYDiffSize())){
			if(GameTools.gestureBreakdownHorizontal(gesture) == Gesture.SWIPE_LEFT){
				if(currentAction.getActionProperties().hasCancel(ActionDataTool.SWIPE_F_TRIGGER)){
					String cancel = currentAction.getActionProperties().getCancel(ActionDataTool.SWIPE_F_TRIGGER);
					setStateUsingTotalName(cancel);
					initSpeed = true;
					this.direction = Direction.LEFT;
				}
			} else if(GameTools.gestureBreakdownHorizontal(gesture) == Gesture.SWIPE_RIGHT) {
				if(currentAction.getActionProperties().hasCancel(ActionDataTool.SWIPE_F_TRIGGER)){
					String cancel = currentAction.getActionProperties().getCancel(ActionDataTool.SWIPE_F_TRIGGER);
					setStateUsingTotalName(cancel);
					initSpeed = true;
					this.direction = Direction.RIGHT;
				}
			}
		} else {
			if(GameTools.gestureBreakdownVertical(gesture) == Gesture.SWIPE_UP) {
				if(currentAction.getActionProperties().hasCancel(ActionDataTool.SWIPE_U_TRIGGER)){
					String cancel = currentAction.getActionProperties().getCancel(ActionDataTool.SWIPE_U_TRIGGER);
					setStateUsingTotalName(cancel);
					initSpeed = true;
				}
				
				if(GameTools.gestureBreakdownHorizontal(gesture) == Gesture.SWIPE_RIGHT){
					this.direction = Direction.RIGHT;
				} else if(GameTools.gestureBreakdownHorizontal(gesture) == Gesture.SWIPE_LEFT){
					this.direction = Direction.LEFT;
				}
				
				/*
				if(controlType == CONTROL_TYPE.RELATIVE){
					if(unprojectedX > getMidX()){
						this.direction = Direction.RIGHT;
					}else if(unprojectedX < getMidX()){
						this.direction = Direction.LEFT;
					}
				}else if(controlType == CONTROL_TYPE.FIXED){
					//asdf
				}
				*/
			} else if(GameTools.gestureBreakdownVertical(gesture) == Gesture.SWIPE_DOWN){
				if(currentAction.getActionProperties().hasCancel(ActionDataTool.SWIPE_D_TRIGGER)){
					String cancel = currentAction.getActionProperties().getCancel(ActionDataTool.SWIPE_D_TRIGGER);
					setStateUsingTotalName(cancel);
					initSpeed = true;
				}
				
				if(GameTools.gestureBreakdownHorizontal(gesture) == Gesture.SWIPE_RIGHT){
					this.direction = Direction.RIGHT;
				} else if(GameTools.gestureBreakdownHorizontal(gesture) == Gesture.SWIPE_LEFT){
					this.direction = Direction.LEFT;
				}
				
				/*
				if(controlType == CONTROL_TYPE.RELATIVE){
					if(unprojectedX > getMidX()){
						this.direction = Direction.RIGHT;
					}else if(unprojectedX < getMidX()){
						this.direction = Direction.LEFT;
					}
				}else if(controlType == CONTROL_TYPE.FIXED){
					//asdf
				}
				*/
			}
		}
		
		return;
	}

	private void executeMovement(){
		if(controlType == CONTROL_TYPE.RELATIVE){
			float checkX = getMidX();
			if(moveToX > checkX - Player.BUFFER && moveToX < checkX + Player.BUFFER) {
				//playerState = PlayerState.STAND;
				if(!inputList.contains(Gesture.HOLD_RELEASE)){
					inputList.add(Gesture.HOLD_RELEASE);
				}
			}
			

		} else if(controlType == CONTROL_TYPE.FIXED){
		}
	}
}
