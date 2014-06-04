package structure;

import java.util.HashSet;

import engine.open2d.draw.Plane;

public class HurtBox extends DataBox{
	private float red = 0.0f;
	private float green = 0.0f;
	private float blue = 1.0f;
	private float alpha = 0.5f;
	private String type = "hurtbox";
	
	public HurtBox(float left, float top, float right, float bottom, HashSet<Integer> activeFrame) {
		super(left, top, right, bottom, activeFrame);
		drawBox = new Plane(type,right-left,top-bottom,red,green,blue,alpha);
	}

	public void setSelected(float colour){
		red = 0.0f;
		green = 1.0f;
		blue = colour;
		alpha = 0.5f;
		this.drawBox.setColor(red, green, blue, alpha);
	}
}
