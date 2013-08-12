package object;

import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import game.open2d.R;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.view.MotionEvent;
import object.GameObject.Direction;
import object.GameObject.GameObjectState;
import object.Player.PlayerState;

public class Enemy extends GameObject {
	public static enum EnemyState implements GameObjectState{
		STAND("stand"),
		STRIKE("strike"),
		ENDSTRIKE("end strike"),
		RUN("run"),
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
	Player playerRef;
	EnemyState enemyState;
	
	public Enemy(LinkedHashMap<String,GameObject> gameObjects, Player player, int index, float x, float y, float width, float height){
		super(gameObjects,x,y,width,height);
		this.name = OBJNAME+index;
		this.z = -1.9f;
		this.playerRef = player;
		animations = new HashMap<GameObjectState, Plane>();
		animations.put(EnemyState.STAND, new Plane(R.drawable.enemy_stance, name+"_"+EnemyState.STAND.getName(), width, height, x, y, z, 4, 7));
//		animations.put(EnemyState.RUN, new Plane(R.drawable.rising_run, Enemy.NAME+"_"+EnemyState.RUN.getName(), width, height, x, y, z, 11, 3));
		animations.put(EnemyState.DEAD, new Plane(R.drawable.enemy_stance, name+"_"+EnemyState.DEAD.getName(), width, height, x, y, z, 4, 7));
		
		display = animations.get(EnemyState.STAND);
		enemyState = EnemyState.STAND;
		display.enable();
	}

	@Override
	public void updateState() {
		if(playerRef.getX() > this.x){
			direction = Direction.RIGHT;
		} else if(playerRef.getX() < this.x){
			direction = Direction.LEFT;
		}
		
		if(playerRef.getPlayerState() == PlayerState.FINISH){
			enemyState = EnemyState.DEAD;
		}
	}

	@Override
	public void updateLogic() {
		
	}

	@Override
	public void updateDisplay() {
		if(direction==Direction.RIGHT){
			display.flipTexture(false);
		} else if(direction==Direction.LEFT){
			display.flipTexture(true);
		}
		
		switchAnimation(enemyState);
		
		if(enemyState == EnemyState.DEAD){
			display.disable();
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


}
