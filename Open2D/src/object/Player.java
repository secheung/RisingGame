package object;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.view.MotionEvent;
import engine.open2d.draw.Plane;
import game.open2d.GameTools;
import game.open2d.R;

public class Player extends GameObject{
	static enum PlayerState implements GameObjectState{
		STAND("stand"),
		STRIKE("strike"),
		FINISH("finish"),
		RUN("run"),
		DODGE("dodge"),
		DEAD("dead");
		
		String name;
		PlayerState(String n){
			name = n;
		}
		
		public String getName(){
			return name;
		}
	} 
	
	public static String OBJNAME = "player";
	
	private static float WALK_SPEED = 0.2f;
	private static float BUFFER = 0.4f;
	
	private PlayerState playerState;
	private float moveToX;
	private float moveToY;
	
	public Player(LinkedHashMap<String,GameObject> gameObjects, float x, float y, float width, float height){
		super(gameObjects,x,y,width,height);
		
		playerState = PlayerState.STAND;
		this.name = OBJNAME;
		
		this.moveToX = x;
		this.moveToY = y;
		this.z = -2.0f;
		
		animations = new HashMap<GameObjectState, Plane>();
		animations.put(PlayerState.STAND, new Plane(R.drawable.rising_stance2, name+"_"+PlayerState.STAND.getName(), width, height, x, y, z, 4, 7));
		animations.put(PlayerState.RUN, new Plane(R.drawable.rising_run2, name+"_"+PlayerState.RUN.getName(), width, height, x, y, z, 11, 3));
		animations.put(PlayerState.FINISH, new Plane(R.drawable.finish1, name+"_"+PlayerState.FINISH.getName(), width, height, x, y, z, 8, 5));
		
		this.display = animations.get(PlayerState.STAND);
	}

	@Override
	public void passTouchEvent(float[] unprojectedPoint){
		moveToX = unprojectedPoint[0];
		moveToY = unprojectedPoint[1];
	}

	@Override
	public void updateState() {
		float checkX = x+width/2;
		
		if(moveToX > checkX){
			direction = Direction.RIGHT;
			playerState = PlayerState.RUN;
		} else if(moveToX < checkX) {
			direction = Direction.LEFT;
			playerState = PlayerState.RUN;
		}
		
		if(moveToX > checkX - Player.BUFFER && moveToX < checkX + Player.BUFFER) {
			playerState = PlayerState.STAND;
		}
		
		for(GameObject gameObject : gameObjects.values()){
			if(gameObject instanceof Enemy){
				Enemy enemy = (Enemy)gameObject;
				if(GameTools.boxColDetect(this, enemy)){
					playerState = PlayerState.FINISH;
				}
			}
		}
	}
	
	@Override
	public void updateLogic() {
		float checkX = x+width/2;
		
		if(playerState == PlayerState.RUN){
			if(direction == Direction.RIGHT)
				x += WALK_SPEED;
			else if(direction == Direction.LEFT)
				x -= WALK_SPEED;
		}
	}

	@Override
	public void updateDisplay() {
		if(direction==Direction.RIGHT){
			display.flipTexture(false);
		} else if(direction==Direction.LEFT){
			display.flipTexture(true);
		}
		
		switchAnimation(playerState);
	}
}
