package object;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.view.MotionEvent;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;

public abstract class GameObject {
	public interface GameObjectState{};
	
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
	protected HashMap<GameObjectState, Plane> animations;
	protected String name;
	
	protected LinkedHashMap<String,GameObject> gameObjects;
	
	public GameObject(LinkedHashMap<String,GameObject> gameObjects, float x, float y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		this.gameObjects = gameObjects;
	}
	
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
	
	public void switchAnimation(GameObjectState animToSwitch){
		display.disable();
		display = animations.get(animToSwitch);
		display.enable();
	}
	
	public void update() {
		updateState();
		updateLogic();
		updateDisplay();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
}
