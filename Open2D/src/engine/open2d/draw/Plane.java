package engine.open2d.draw;

import engine.open2d.texture.Texture;

public class Plane extends Shape{
	public Plane(float[] position, float[] color, float[] normal){
		this.positionData = position;
		this.colorData = color;
		this.normalData = normal;
	}
}
