package structure;

import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import android.graphics.RectF;
import android.util.Log;

public class HitBox extends DataBox{
	private float red = 1.0f;
	private float green = 0.0f;
	private float blue = 0.0f;
	private float alpha = 0.5f;
	private String type = "hitbox";
	
	public HitBox(float left, float top, float right, float bottom, int activeFrame){
		super(left,top,right,bottom,activeFrame);
		drawBox = new Plane(type,right-left,top-bottom,red,green,blue,alpha);
	}
}
