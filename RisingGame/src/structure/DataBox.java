package structure;

import android.graphics.RectF;
import engine.open2d.draw.Plane;

public class DataBox {

	
	protected float red;
	protected float green;
	protected float blue;
	protected float alpha;
	protected String type;
	
	protected RectF boxData;
	protected Plane drawBox;
	protected int activeFrame;
	
	
	public DataBox(float left, float top, float right,float bottom,int activeFrame){
		boxData = new RectF(left,top,right,bottom);
		this.activeFrame = activeFrame;
	}
	
	public RectF getBoxData() {
		return boxData;
	}

	public Plane getDrawBox() {
		return drawBox;
	}
	
	public int getActiveFrame() {
		return activeFrame;
	}
}
