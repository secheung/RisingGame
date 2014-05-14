package structure;

import android.graphics.RectF;
import object.Player.PlayerState;
import engine.open2d.draw.Plane;
import game.open2d.R;

public class ActionData {
	private RectF hitBox;
	private RectF hurtBox;
	
	int frames;
	Plane animation;
	

	public ActionData(float left,float top, float right,float bottom){
		//new Plane(R.drawable.rising_stance, name+"_"+PlayerState.STAND.getName(), width, height, x, y, z, 4, 7);
		
		hitBox = new RectF(left,top,right,bottom);
		
		
	}
}
