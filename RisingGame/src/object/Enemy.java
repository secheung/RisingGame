package object;

import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import game.GameTools;
import game.GameTools.Gesture;
import game.open2d.R;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.util.Log;
import android.view.MotionEvent;
import object.GameObject.Direction;
import object.GameObject.GameObjectState;
import object.Player.PlayerState;
import structure.ActionData;

public class Enemy extends GameObject {
	public static enum EnemyState implements GameObjectState{
		STAND("enemy_stand"),
		STRIKE1("enemy_strike"),
		STRUCK1("enemy_struck1"),
		RUN("enemy_run"),
		WALK("enemy_walk"),
		JUMP_BACK("enemy_jump_back"),
		CROSS_ROLL("enemy_cross_roll"),
		DODGE("enemy_dodge"),
		FREEZE("enemy_freeze"),
		DEAD("enemy_dead");
		
		String name;
		public static EnemyState getStateFromName(String name){
			for(EnemyState enemyState : EnemyState.values()){
				if(name.equals(enemyState.getName())){
					return enemyState;
				}
			}
			return null;
		}
		
		EnemyState(String n){
			name = n;
		}
		
		public String getName(){
			return name;
		}
	}
	
	private static String OBJNAME = "enemy";
	
	private static EnemyState INIT_STATE = EnemyState.STAND;
	private static float RUN_SPEED = 0.16f;
	private static float WALK_SPEED = 0.08f;
	private static float JUMP_BACK_SPEED = 0.15f;
	private static float CROSS_ROLL_SPEED = 0.17f;
	private static float FAR_DIST_TO_PLAYER = 2.5f;
	private static float CLOSE_DIST_TO_PLAYER = 1.5f;
	private static float COLLISION_BUFFER = 1.0f;
	private static float KNOCK_BACK = 0.0f;
	public static float COLLISION_FRAME = 20.0f;
	
	Player playerRef;
	EnemyState enemyState;
	public int struck;
	public int unfreezeTimeCount;
	
	//public Enemy(LinkedHashMap<String,GameObject> gameObjects, Player player, int index, float x, float y, float width, float height){
	public Enemy(LinkedHashMap<String,GameObject> gameObjects, List<ActionData> actionData, Player player, int index, float x, float y){
		super(gameObjects,actionData,INIT_STATE,x,y);
		
		enemyState = INIT_STATE;
		this.name = OBJNAME+index;
		this.z = -0.9f+index*0.01f;
		this.playerRef = player;
		
		struck = 3;
		unfreezeTimeCount = 0;
		
		//display = animations.get(EnemyState.STAND);
		//this.currentAction = this.actionData.get(INIT_STATE);
		
	}

	@Override
	public void setupAnimRef() {
		animationRef = new HashMap<GameObjectState, Integer>();
		animationRef.put(EnemyState.STAND, R.drawable.enemy_stance);
		animationRef.put(EnemyState.FREEZE, R.drawable.enemy_stance);
		animationRef.put(EnemyState.RUN, R.drawable.enemy_run);
		animationRef.put(EnemyState.WALK, R.drawable.enemy_run);
		animationRef.put(EnemyState.JUMP_BACK, R.drawable.enemy_jump_back);
		animationRef.put(EnemyState.CROSS_ROLL, R.drawable.enemy_cross_roll);
		animationRef.put(EnemyState.DEAD, R.drawable.enemy_stance);
		animationRef.put(EnemyState.STRIKE1, R.drawable.enemy_strike1);
		animationRef.put(EnemyState.STRUCK1, R.drawable.enemy_struck1);
		
	}

	@Override
	public void mapActionData(List<ActionData> actionData) {
		for(ActionData data : actionData){
			EnemyState state = EnemyState.getStateFromName(data.getName());
			if(state != null){
				int refID = animationRef.get(state);
				data.createAnimation(refID);
				this.actionData.put(state, data);
			}
		}
	}
	
	@Override
	public void updateState() {
		float checkX = getMidX();
		
		if(!isStrikeState() && !isDodging()){
			if(playerRef.getMidX() > checkX){
				direction = Direction.RIGHT;
			} else if(playerRef.getMidX() < checkX){
				direction = Direction.LEFT;
			}
		}

		if(enemyState == EnemyState.STAND){
			if(Math.abs(checkX - playerRef.getMidX()) > CLOSE_DIST_TO_PLAYER){
				enemyState = EnemyState.WALK;
			}
			
			if(Math.abs(checkX - playerRef.getMidX()) > FAR_DIST_TO_PLAYER){
				enemyState = EnemyState.RUN;
			}
			/*
			if(GameTools.boxColDetect(this, playerRef, COLLISION_BUFFER) && Math.random() > 0.90){
				enemyState = EnemyState.STRIKE1;
			}
			*/
		} else if(enemyState == EnemyState.RUN || enemyState == EnemyState.WALK){
			executeMovement();
		}
		
		//otherAIMoveInteration();
		
		/*
		if(selected && GameTools.boxColDetect(this, playerRef, COLLISION_BUFFER) && !isDodging()){
			if(playerRef.isFinishState() || playerRef.isCounterState()){
				enemyState = EnemyState.DEAD;
			}else if(playerRef.getPlayerState() == PlayerState.STRIKE1){
				enemyState = EnemyState.STRUCK1;
			} else if(playerRef.getPlayerState() == PlayerState.STRIKE2){
				enemyState = EnemyState.STRUCK1;
			} else if(playerRef.getPlayerState() == PlayerState.STRIKE3){
				enemyState = EnemyState.STRUCK1;
			}
		}
		
		if(!selected){
			if((playerRef.isFinishState() || playerRef.isCounterState()) && enemyState != EnemyState.FREEZE){
				enemyState = EnemyState.FREEZE;
				unfreezeTimeCount = playerRef.getDisplay().getTotalFrame();
			}
		}
		*/
	}

	@Override
	public void updateLogic() {
		Plane display = currentAction.getAnimation();
		if(enemyState == EnemyState.STRUCK1){
			if(display.getFrame() < Player.CANCEL_STRIKE_FRAMES){
				selected = false;
			}
			if(direction == Direction.RIGHT)
				x -= KNOCK_BACK;
			else if(direction == Direction.LEFT)
				x += KNOCK_BACK;
		}else if(isStrikeState()){
			
		}else if(enemyState == EnemyState.RUN){
			if(direction == Direction.RIGHT)
				x += RUN_SPEED;
			else if(direction == Direction.LEFT)
				x -= RUN_SPEED;
		}else if(enemyState == EnemyState.WALK){
			if(direction == Direction.RIGHT)
				x += WALK_SPEED;
			else if(direction == Direction.LEFT)
				x -= WALK_SPEED;
		}else if(enemyState == EnemyState.JUMP_BACK){
			if(direction == Direction.RIGHT)
				x -= JUMP_BACK_SPEED;
			else if(direction == Direction.LEFT)
				x += JUMP_BACK_SPEED;
		}else if(enemyState == EnemyState.CROSS_ROLL){
			if(direction == Direction.RIGHT)
				x += CROSS_ROLL_SPEED;
			else if(direction == Direction.LEFT)
				x -= CROSS_ROLL_SPEED;
		}else if(enemyState == EnemyState.FREEZE){
			unfreezeTimeCount--;
		}
	}

	@Override
	public void updateDisplay() {
		Plane display = currentAction.getAnimation();
		if(currentAction != actionData.get(enemyState))
			switchAnimation(enemyState);
		
		if(direction==Direction.RIGHT){
			display.flipTexture(false);
		} else if(direction==Direction.LEFT){
			display.flipTexture(true);
		}
		
		if(enemyState == EnemyState.DEAD){
			display.drawDisable();
		}
		
		if(enemyState == EnemyState.FREEZE){
			display.drawDisable();
		}
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
		} else if(enemyState == EnemyState.STRUCK1){
			display.resetAnimation();
			struck -= 1;
			enemyState = EnemyState.STAND;
		} else if(enemyState == EnemyState.FREEZE){
			if(unfreezeTimeCount <= 0){
				display.resetAnimation();
				enemyState = EnemyState.STAND;
				unfreezeTimeCount = 0;
			}
			
		}
	}
	
	@Override
	public void passTouchEvent(MotionEvent e, WorldRenderer worldRenderer) {
		Plane display = currentAction.getAnimation();
		Plane selectedPlane = worldRenderer.getSelectedPlane(e.getX(), e.getY());

		float[] points = worldRenderer.getUnprojectedPoints(e.getX(), e.getY(), display);
		if(enemyState != EnemyState.FREEZE)
			selected = checkEnemySelection(points[0],points[1]);
		
//		if(animations.containsValue(selectedPlane)){
//			selected = true;
//		}
		
		if(gesture != Gesture.NONE){
			selected = false;
		}
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
						enemy.getEnemyState() != EnemyState.STRUCK1 &&
						!enemy.isDodging() &&
						this.getEnemyState() != EnemyState.STRUCK1){
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
	
	public int getStruck(){
		return struck;
	}

	public boolean isSelected() {
		return selected;
	}
}
