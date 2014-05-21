package structure;

import android.graphics.RectF;
import engine.open2d.draw.Plane;

public class DataBox {

	
	protected float red = 1.0f;
	protected float green = 0.0f;
	protected float blue = 0.0f;
	protected float alpha = 0.5f;
	protected String type;
	
	private RectF boxData;
	private Plane drawBox;
	
	public DataBox(float offsetX, float offsetY, float width, float height){
		boxData = new RectF(offsetX,offsetY,width,height);
		drawBox = new Plane(type,width,height,red,green,blue,alpha);
	}
	
	public RectF getBoxData() {
		return boxData;
	}

	public Plane getDrawBox() {
		return drawBox;
	}
	
}
