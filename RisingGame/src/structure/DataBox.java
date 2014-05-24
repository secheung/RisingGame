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
	
	public DataBox(float left, float top, float right,float bottom){
		boxData = new RectF(left,top,right,bottom);
	}
	
	public RectF getBoxData() {
		return boxData;
	}

	public Plane getDrawBox() {
		return drawBox;
	}
	
}
