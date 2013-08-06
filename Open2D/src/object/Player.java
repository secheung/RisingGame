package object;

import java.util.HashMap;

import engine.open2d.draw.Plane;
import game.open2d.R;

public class Player extends GameObject{
	private static String NAME = "player";
	private static String STAND = "stand";
	private static String RUN = "run";

	public Player(float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.z = -1.0f;
		this.width = width;
		this.height = height;
		
		animations = new HashMap<String, Plane>();
		animations.put("stand", new Plane(R.drawable.rising_stance, Player.NAME+"_"+Player.STAND, 3.5f, 3.5f, x, y, z, 4, 7));
		animations.put("run", new Plane(R.drawable.rising_stance, Player.NAME+"_"+Player.RUN, 3.5f, 3.5f, x, y, z, 4, 7));
		
		display = animations.get("stand");
	}

}
