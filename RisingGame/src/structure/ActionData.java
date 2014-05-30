package structure;

import java.util.LinkedList;
import java.util.List;

import android.graphics.RectF;
import android.util.Log;
import object.GameObject;
import object.Player.PlayerState;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import engine.open2d.texture.AnimatedTexture;
import game.open2d.R;

public class ActionData {
	private final static String LOG_PREFIX = "ACTION_DATA";
	
	private List<HitBox> hitBoxes;
	private List<HurtBox> hurtBoxes;
	
	String name;
	int frames;
	Plane animation;
	PlaneData planeData;
	//GameObject pairedObj;
	

	//public ActionData(String name,Plane animation,GameObject pairedObj){
	//public ActionData(String name,GameObject pairedObj){
	public ActionData(String name){
		//this.pairedObj = pairedObj;
		//this.animation = animation;
		this.name = name;
		hitBoxes = new LinkedList<HitBox>();
		hurtBoxes = new LinkedList<HurtBox>();
	}
	
	public void createAnimation(int refID){
		animation = new Plane(refID, name, planeData.getWidth(), planeData.getHeight(), planeData.getRows(), planeData.getColumns());
	}
	
	public void addHitBox(float left, float top, float right, float bottom,int activeFrame){
		hitBoxes.add(new HitBox(left,top,right,bottom,activeFrame));
	}
	
	public void updateDrawData(WorldRenderer worldRenderer, GameObject pairedObj){
		//Log.d("debug", pairedObj.getX()+ " " + pairedObj.getY() + " " + pairedObj.getZ());
		//Log.d("debug",""+(animation.isDrawEnabled()));
		worldRenderer.updateDrawObject(animation, pairedObj.getX(), pairedObj.getY(), pairedObj.getZ());
		
		for(HitBox box : hitBoxes){
			if(box.getActiveFrame() == animation.getFrame() || box.getActiveFrame() == -1){
				float boxOffsetX = box.getBoxData().left+pairedObj.getX();
				float boxOffsetY = box.getBoxData().bottom+pairedObj.getY();
				worldRenderer.updateDrawObject(box.getDrawBox(), boxOffsetX, boxOffsetY,pairedObj.getZ()+0.01f);
				box.getDrawBox().drawEnable();
			} else {
				box.getDrawBox().drawDisable();
			}
		}
		
		for(HurtBox box : hurtBoxes){
			if(box.getActiveFrame() == animation.getFrame() || box.getActiveFrame() == -1){
				float boxOffsetX = box.getBoxData().left+pairedObj.getX();
				float boxOffsetY = box.getBoxData().bottom+pairedObj.getY();
				worldRenderer.updateDrawObject(box.getDrawBox(), boxOffsetX, boxOffsetY,pairedObj.getZ()+0.01f);
				box.getDrawBox().drawEnable();
			} else {
				box.getDrawBox().drawDisable();
			}
		}
	}
	
	public void loadAnimIntoRenderer(WorldRenderer worldRenderer){
		worldRenderer.addDrawShape(animation);
		
		for(HitBox box : hitBoxes){
			worldRenderer.addDrawShape(box.getDrawBox());
		}
		
		for(HurtBox box : hurtBoxes){
			worldRenderer.addDrawShape(box.getDrawBox());
		}
	}
	
	public void unloadAnimFromRenderer(WorldRenderer worldRenderer){
		worldRenderer.removeDrawShape(animation);
		
		for(HitBox box : hitBoxes){
			worldRenderer.removeDrawShape(box.getDrawBox());
		}
		
		for(HurtBox box : hurtBoxes){
			worldRenderer.removeDrawShape(box.getDrawBox());
		}
	}
	
	public void drawDisable(){
		animation.drawDisable();
		
		for(HitBox box : hitBoxes){
			box.getDrawBox().drawDisable();
		}
		
		for(HurtBox box : hurtBoxes){
			box.getDrawBox().drawDisable();
		}
	}

	public void drawEnable(){
		animation.drawEnable();
		
		for(HitBox box : hitBoxes){
			box.getDrawBox().drawEnable();
		}
		
		for(HurtBox box : hurtBoxes){
			box.getDrawBox().drawEnable();
		}
	}
	
	public List<HitBox> getHitBoxes() {
		return hitBoxes;
	}

	public void setHitBoxes(List<HitBox> hitBoxes) {
		this.hitBoxes = hitBoxes;
	}

	public List<HurtBox> getHurtBoxes() {
		return hurtBoxes;
	}

	public void setHurtBoxes(List<HurtBox> hurtBoxes) {
		this.hurtBoxes = hurtBoxes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Plane getAnimation() {
		return animation;
	}

	public void setAnimation(Plane animation) {
		this.animation = animation;
	}

	public PlaneData getPlaneData() {
		return planeData;
	}

	public void setPlaneData(PlaneData planeData) {
		this.planeData = planeData;
	}

	/*
	public GameObject getPairedObj() {
		return pairedObj;
	}

	public void setPairedObj(GameObject pairedObj) {
		this.pairedObj = pairedObj;
	}
	*/
	
	
}
