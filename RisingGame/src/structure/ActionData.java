package structure;

import java.util.LinkedList;
import java.util.List;

import android.graphics.RectF;
import object.GameObject;
import object.Player.PlayerState;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import game.open2d.R;

public class ActionData {
	private List<HitBox> hitBox;
	private List<HitBox> hurtBox;
	
	int frames;
	Plane animation;
	GameObject pairedObj;
	

	public ActionData(String name,GameObject pairedObj){
		this.pairedObj = pairedObj;
		hitBox = new LinkedList<HitBox>();
	}
	
	public void addHitBox(float x, float y, float width, float height){
		hitBox.add(new HitBox(x,y,width,height));
	}
	
	public void updateDrawData(WorldRenderer worldRenderer){
		for(HitBox box : hitBox){
			worldRenderer.updateDrawObject(box.getDrawBox(), pairedObj.getX(), pairedObj.getY(),pairedObj.getZ()+0.01f);
			box.getDrawBox().drawEnable();
		}
	}
	
	public void loadAnimIntoRenderer(WorldRenderer worldRenderer){
		for(HitBox box : hitBox){
			worldRenderer.addDrawShape(box.getDrawBox());
		}
	}
	
	public void unloadAnimFromRenderer(WorldRenderer worldRenderer){
		for(HitBox box : hitBox){
			worldRenderer.removeDrawShape(box.getDrawBox());
		}
	}
}
