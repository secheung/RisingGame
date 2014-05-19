package structure;

import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import android.graphics.RectF;

public class HitBox {
	private static float RED = 1.0f;
	private static float GREEN = 0.0f;
	private static float BLUE = 0.0f;
	private static float ALPHA = 0.5f;
	
	private RectF boxData;
	private Plane drawBox;
	
	public HitBox(float offsetX, float offsetY, float width, float height){
		boxData = new RectF(offsetX,offsetY,width,height);
		drawBox = new Plane("hitbox",width,height,RED,GREEN,BLUE,ALPHA);
	}
	
	public RectF getBoxData() {
		return boxData;
	}

	public Plane getDrawBox() {
		return drawBox;
	}

}
