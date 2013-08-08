package object;

import java.util.HashMap;

import android.view.MotionEvent;
import engine.open2d.draw.Plane;
import game.open2d.R;

public class Player extends GameObject{
	static enum PlayerState{
		STAND,
		ATTACK,
		ENDSTRIKE,
		MOVE,
		DODGE,
		DEAD
	} 
	
	private static String NAME = "player";
	private static String STAND = "stand";
	private static String RUN = "run";
	private static float WALK_SPEED = 0.2f;
	private static float BUFFER = 0.4f;
	
	private PlayerState playerState;
	private float moveToX;
	private float moveToY;
	
	public Player(float x, float y, float width, float height){
		playerState = PlayerState.STAND;
		
		this.x = x;
		this.y = y;
		this.moveToX = x;
		this.moveToY = y;
		this.z = -1.0f;
		this.width = width;
		this.height = height;
		
		animations = new HashMap<String, Plane>();
		animations.put("stand", new Plane(R.drawable.rising_stance, Player.NAME+"_"+Player.RUN, 3.5f, 3.5f, x, y, z, 4, 7));
		animations.put("run", new Plane(R.drawable.walk, Player.NAME+"_"+Player.STAND, 3.5f, 3.5f, x, y, z, 9, 6));
		
		this.display = animations.get("stand");
	}

	@Override
	public void passTouchEvent(float[] unprojectedPoint){
		moveToX = unprojectedPoint[0];
		moveToY = unprojectedPoint[1];
	}

	@Override
	public void update() {
		updateState();
		updateLogic();
		updateDisplay();
	}

	@Override
	public void updateState() {
		float checkX = x+width/2;
		
		if(moveToX > checkX){
			direction = Direction.RIGHT;
			playerState = PlayerState.MOVE;
		} else if(moveToX < checkX) {
			direction = Direction.LEFT;
			playerState = PlayerState.MOVE;
		} 
		
		if(moveToX > checkX - Player.BUFFER && moveToX < checkX + Player.BUFFER) {
			playerState = PlayerState.STAND;
		}
	}
	
	@Override
	public void updateLogic() {
		float checkX = x+width/2;
		
		if(playerState == PlayerState.MOVE){
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
		
		if(playerState == PlayerState.STAND){
			display.disable();
			display = animations.get(STAND);
			display.enable();
		} else if(playerState == PlayerState.MOVE){
			display.disable();
			display = animations.get(RUN);
			display.enable();
		}
		
	}
}
