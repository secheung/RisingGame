package structure;

import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import android.graphics.RectF;
import android.util.Log;

public class HitBox extends DataBox{
	float red = 1.0f;
	float green = 0.0f;
	float blue = 0.0f;
	float alpha = 0.5f;
	String type = "hitbox";
	
	public HitBox(float left, float top, float right, float bottom){
		super(left,top,right,bottom);
		drawBox = new Plane(type,right-left,top-bottom,red,green,blue,alpha);
	}
	
}
