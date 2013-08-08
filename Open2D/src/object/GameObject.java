package object;

import java.util.HashMap;

import android.view.MotionEvent;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;

public abstract class GameObject {
	public static enum Direction{
		LEFT,
		RIGHT,
		UP,
		DOWN
	}
	
	protected Direction direction;
	
	protected float x;
	protected float y;
	protected float z;
	
	protected float width;
	protected float height;
	
	protected Plane display;
	protected HashMap<String, Plane> animations;
	protected String name;
	
	public abstract void update();
	public abstract void updateState();
	public abstract void updateLogic();
	public abstract void updateDisplay();
	public abstract void passTouchEvent(float[] unprojectedPoint);
	
	public void draw(WorldRenderer worldRenderer){
		display.enable();
		worldRenderer.drawObject(display, x, y, z);
	}
	
	public void loadAnimIntoRenderer(WorldRenderer worldRenderer){
		for(Plane animation : animations.values()){
			worldRenderer.addDrawShape(animation);
		}
	}
	
	public void switchAnimation(String animToSwitch){
		display.disable();
		display = animations.get(animToSwitch);
		display.enable();
	}
	
	public Plane getDisplay(){
		return display;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
}
