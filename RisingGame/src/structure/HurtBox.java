package structure;

import engine.open2d.draw.Plane;

public class HurtBox extends DataBox{
	private float red = 0.0f;
	private float green = 0.0f;
	private float blue = 1.0f;
	private float alpha = 0.5f;
	private String type = "hurtbox";
	
	public HurtBox(float left, float top, float right, float bottom, int activeFrame) {
		super(left, top, right, bottom, activeFrame);
		drawBox = new Plane(type,right-left,top-bottom,red,green,blue,alpha);
	}

}
