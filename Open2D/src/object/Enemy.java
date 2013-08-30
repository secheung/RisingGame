package object;

import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import game.open2d.GameTools;
import game.open2d.R;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.util.Log;
import android.view.MotionEvent;
import object.GameObject.Direction;
import object.GameObject.GameObjectState;
import object.Player.PlayerState;

public class Enemy extends GameObject {
	public static enum EnemyState implements GameObjectState{
		STAND("stand"),
		STRIKE("strike"),
		STRUCK1("struck1"),
		ENDSTRIKE("end strike"),
		RUN("run"),
		WALK("walk"),
		DODGE("dodge"),
		DEAD("dead");
		
		String name;
		EnemyState(String n){
			name = n;
		}
		
		public String getName(){
			return name;
		}
	}
	
	private static String OBJNAME = "enemy";
	private static float RUN_SPEED = 0.16f;
	private static float WALK_SPEED = 0.08f;
	private static float FAR_DIST_TO_PLAYER = 2.5f;
	private static float CLOSE_DIST_TO_PLAYER = 1.5f;
	private static float COLLISION_BUFFER = 1.0f;
	
	Player playerRef;
	EnemyState enemyState;
	public int struck;
	
	public Enemy(LinkedHashMap<String,GameObject> gameObjects, Player player, int index, float x, float y, float width, float height){
		super(gameObjects,x,y,width,height);
		this.name = OBJNAME+index;
		this.z = -0.9f;
		this.playerRef = player;
		animations = new HashMap<GameObjectState, Plane>();
		animations.put(EnemyState.STAND, new Plane(R.drawable.enemy_stance, name+"_"+EnemyState.STAND.getName(), width, height, x, y, z, 4, 7));
		animations.put(EnemyState.RUN, new Plane(R.drawable.rising_run, name+"_"+EnemyState.RUN.getName(), width, height, x, y, z, 11, 3));
		animations.put(EnemyState.WALK, new Plane(R.drawable.rising_run, name+"_"+EnemyState.WALK.getName(), width, height, x, y, z, 11, 3));
		animations.put(EnemyState.DEAD, new Plane(R.drawable.enemy_stance, name+"_"+EnemyState.DEAD.getName(), width, height, x, y, z, 4, 7));
		animations.put(EnemyState.STRUCK1, new Plane(R.drawable.enemy_struck1, name+"_"+EnemyState.STRUCK1.getName(), width, height, x, y, z, 2, 7));
		
		struck = 3;
		
		display = animations.get(EnemyState.STAND);
		enemyState = EnemyState.STAND;
		display.drawEnable();
	}

	@Override
	public void updateState() {
		float checkX = getMidX();
		
		if(playerRef.getMidX() > checkX){
			direction = Direction.RIGHT;
		} else if(playerRef.getMidX() < checkX){
			direction = Direction.LEFT;
		}

		if(enemyState == EnemyState.STAND){
			if(Math.abs(checkX - playerRef.getMidX()) > CLOSE_DIST_TO_PLAYER){
				enemyState = EnemyState.WALK;
			}
			
			if(Math.abs(checkX - playerRef.getMidX()) > FAR_DIST_TO_PLAYER){
				enemyState = EnemyState.RUN;
			}
			
		} else if(enemyState == EnemyState.RUN || enemyState == EnemyState.WALK){
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
		
		if(playerRef.getPlayerState() == PlayerState.FINISH){
			enemyState = EnemyState.DEAD;
		} else if(playerRef.getPlayerState() == PlayerState.STRIKE1){
			enemyState = EnemyState.STRUCK1;
		} else if(playerRef.getPlayerState() == PlayerState.STRIKE2){
			enemyState = EnemyState.STRUCK1;
		} else if(playerRef.getPlayerState() == PlayerState.STRIKE3){
			enemyState = EnemyState.STRUCK1;
		}
	}

	@Override
	public void updateLogic() {
		if(enemyState == EnemyState.STRUCK1){
			selected = false;
		}
		
		if(enemyState == EnemyState.RUN){
			if(direction == Direction.RIGHT)
				x += RUN_SPEED;
			else if(direction == Direction.LEFT)
				x -= RUN_SPEED;
		}
		
		if(enemyState == EnemyState.WALK){
			if(direction == Direction.RIGHT)
				x += WALK_SPEED;
			else if(direction == Direction.LEFT)
				x -= WALK_SPEED;
		}
	}

	@Override
	public void updateDisplay() {
		switchAnimation(enemyState);
		
		if(direction==Direction.RIGHT){
			display.flipTexture(false);
		} else if(direction==Direction.LEFT){
			display.flipTexture(true);
		}
		
		if(enemyState == EnemyState.DEAD){
			display.drawDisable();
		}else if(display.isPlayed() && enemyState == EnemyState.STRUCK1){
			display.resetAnimation();
			struck -= 1;
			enemyState = EnemyState.STAND;
		}
	}

	@Override
	public void updateAfterDisplay() {
		
	}
	
	@Override
	public void passTouchEvent(MotionEvent e, WorldRenderer worldRenderer) {
		Plane selectedPlane = worldRenderer.getSelectedPlane(e.getX(), e.getY());
		
		if(animations.containsValue(selectedPlane)){
			selected = true;
		}
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


}
