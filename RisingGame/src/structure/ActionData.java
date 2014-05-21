package structure;

import java.util.LinkedList;
import java.util.List;

import android.graphics.RectF;
import android.util.Log;
import object.GameObject;
import object.Player.PlayerState;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import game.open2d.R;

public class ActionData {
	private List<HitBox> hitBoxes;
	private List<HitBox> hurtBoxes;
	
	int frames;
	Plane animation;
	GameObject pairedObj;
	

	//public ActionData(String name,Plane animation,GameObject pairedObj){
	public ActionData(String name,GameObject pairedObj){
		this.pairedObj = pairedObj;
		//this.animation = animation;
		hitBoxes = new LinkedList<HitBox>();
	}
	
	public void addHitBox(float x, float y, float width, float height){
		hitBoxes.add(new HitBox(x,y,width,height));
	}
	
	public void updateDrawData(WorldRenderer worldRenderer){
		for(HitBox box : hitBoxes){
			//Log.d("box data", "left: "+box.getBoxData().left+" top: "+box.getBoxData().top);
			worldRenderer.updateDrawObject(box.getDrawBox(), pairedObj.getX(), pairedObj.getY(),pairedObj.getZ()+0.01f);
			box.getDrawBox().drawEnable();
		}
	}
	
	public void loadAnimIntoRenderer(WorldRenderer worldRenderer){
		for(HitBox box : hitBoxes){
			worldRenderer.addDrawShape(box.getDrawBox());
		}
	}
	
	public void unloadAnimFromRenderer(WorldRenderer worldRenderer){
		for(HitBox box : hitBoxes){
			worldRenderer.removeDrawShape(box.getDrawBox());
		}
	}

	public List<HitBox> getHitBoxes() {
		return hitBoxes;
	}

	public void setHitBoxes(List<HitBox> hitBoxes) {
		this.hitBoxes = hitBoxes;
	}

	public List<HitBox> getHurtBoxes() {
		return hurtBoxes;
	}

	public void setHurtBoxes(List<HitBox> hurtBoxes) {
		this.hurtBoxes = hurtBoxes;
	}
	
	
}
