package structure;

import java.util.LinkedList;
import java.util.List;

import android.graphics.RectF;
import object.GameObject;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;

public class ActionData {
	private final static String LOG_PREFIX = "ACTION_DATA";
	private final static boolean HITBOX_DEBUG = true;
	private final static boolean POINT_DEBUG = true;
	
	private List<HitBox> hitBoxes;
	private List<HurtBox> hurtBoxes;
	private Plane pointBox;
	
	String name;
	int frames;
	Plane animation;
	PlaneData planeData;
	
	ActionProperties actionProperties;
	InteractionProperties interProperties;
	
	boolean flipped = false;
	//GameObject pairedObj;

	//public ActionData(String name,Plane animation,GameObject pairedObj){
	//public ActionData(String name,GameObject pairedObj){
	public ActionData(String name){
		//this.pairedObj = pairedObj;
		//this.animation = animation;
		this.name = name;
		hitBoxes = new LinkedList<HitBox>();
		hurtBoxes = new LinkedList<HurtBox>();
		
		pointBox = new Plane(name+"_pointBox",0.15f,0.15f,0.0f,1.0f,0.0f,0.5f);
		
		actionProperties = new ActionProperties();
		interProperties = new InteractionProperties();
	}
	
	public void createAnimation(int refID){
		animation = new Plane(refID, name, planeData.getWidth(), planeData.getHeight(), planeData.getRows(), planeData.getColumns());
		animation.setPlayback(planeData.getPlayback());
	}
	
	public void updateDrawData(WorldRenderer worldRenderer, GameObject pairedObj){
		//Log.d("debug", pairedObj.getX()+ " " + pairedObj.getY() + " " + pairedObj.getZ());
		//Log.d("debug",animation.name+" "+(animation.isDrawEnabled()));
		
		float offset_width = planeData.getWidth()/2;//draw in middle
		worldRenderer.updateDrawObject(animation, pairedObj.getX()-offset_width, pairedObj.getY(), pairedObj.getZ());
		
		if(POINT_DEBUG){
			float pointBox_width = 0.15f/2;
			worldRenderer.updateDrawObject(pointBox,pairedObj.getOffsetX()-pointBox_width,pairedObj.getOffsetY(),pairedObj.getZ()+0.01f);
		}
		
		if(HITBOX_DEBUG){
			float boxOffsetX = 0;
			float boxOffsetY = 0;
			
			for(HitBox box : hitBoxes){
				if(box.getActiveFrame().contains(animation.getFrame()) || box.getActiveFrame().isEmpty()){
					/*
					if(pairedObj.getDirection() == Direction.LEFT){
						boxOffsetX = pairedObj.getX() + box.getBoxData().left;
					} else {
						boxOffsetX = pairedObj.getX() + flipBoxCoordX(box.getBoxData());
					}
					*/
					boxOffsetX = pairedObj.getX() + box.getBoxData().left;
					boxOffsetY = pairedObj.getY() + box.getBoxData().bottom;
					worldRenderer.updateDrawObject(box.getDrawBox(), boxOffsetX, boxOffsetY,pairedObj.getZ()+0.01f);
					box.getDrawBox().drawEnable();
				} else {
					box.getDrawBox().drawDisable();
				}
			}
			
			for(HurtBox box : hurtBoxes){
				//if(box.getActiveFrame() == animation.getFrame() || box.getActiveFrame() == -1){
				if(box.getActiveFrame().contains(animation.getFrame()) || box.getActiveFrame().isEmpty()){
					/*
					if(pairedObj.getDirection() == Direction.LEFT){
						boxOffsetX = pairedObj.getX() + box.getBoxData().left;
					} else {
						boxOffsetX = pairedObj.getX() + flipBoxCoordX(box.getBoxData());
					}
					*/
					boxOffsetX = pairedObj.getX() + box.getBoxData().left;
					boxOffsetY = pairedObj.getY() + box.getBoxData().bottom;
					worldRenderer.updateDrawObject(box.getDrawBox(), boxOffsetX, boxOffsetY,pairedObj.getZ()+0.01f);
					box.getDrawBox().drawEnable();
				} else {
					box.getDrawBox().drawDisable();
				}
			}
		}
	}
	
	public void loadAnimIntoRenderer(WorldRenderer worldRenderer){
		worldRenderer.addDrawShape(animation);
		if(POINT_DEBUG){
			worldRenderer.addDrawShape(pointBox);
		}
			
		if(HITBOX_DEBUG){
			for(HitBox box : hitBoxes){
				worldRenderer.addDrawShape(box.getDrawBox());
			}
			
			for(HurtBox box : hurtBoxes){
				worldRenderer.addDrawShape(box.getDrawBox());
			}
		}
	}
	
	public void unloadAnimFromRenderer(WorldRenderer worldRenderer){
		worldRenderer.removeDrawShape(animation);
		
		if(POINT_DEBUG){
			worldRenderer.removeDrawShape(pointBox);
		}
		
		if(HITBOX_DEBUG){
			for(HitBox box : hitBoxes){
				worldRenderer.removeDrawShape(box.getDrawBox());
			}
			
			for(HurtBox box : hurtBoxes){
				worldRenderer.removeDrawShape(box.getDrawBox());
			}
		}
	}
	
	public void drawDisable(){
		animation.drawDisable();
		
		if(POINT_DEBUG){
			pointBox.drawDisable();
		}
		
		for(HitBox box : hitBoxes){
			box.getDrawBox().drawDisable();
		}
		
		for(HurtBox box : hurtBoxes){
			box.getDrawBox().drawDisable();
		}
	}

	public void drawEnable(){
		animation.drawEnable();
		
		if(POINT_DEBUG){
			pointBox.drawEnable();
		}
		
		for(HitBox box : hitBoxes){
			box.getDrawBox().drawEnable();
		}
		
		for(HurtBox box : hurtBoxes){
			box.getDrawBox().drawEnable();
		}
	}
	
	public float flipBoxCoordX(RectF box){
		return planeData.getWidth() - box.width() - box.left;
	}

	public float flipBoxCoordY(RectF box){
		return planeData.getHeight() - box.height() - box.bottom;
	}
	
	public void flipHorizontal(boolean flip){
		if(flipped != flip){
			animation.flipTexture(flip);
			for(HitBox box : hitBoxes){
				RectF boxData = box.getBoxData();
				float left = (planeData.getWidth()/2 - boxData.width() - boxData.left) - planeData.getWidth()/2;
				float right = left + boxData.width();
				box.getBoxData().set(left, boxData.top, right, boxData.bottom);
			}
			
			for(HurtBox box : hurtBoxes){
				//RectF boxData = box.getBoxData(); 
				//box.getBoxData().set(planeData.getWidth() - boxData.width() - boxData.left, boxData.top, planeData.getWidth() - boxData.width() - boxData.right, boxData.bottom);
				RectF boxData = box.getBoxData(); 
				float left = (planeData.getWidth()/2 - boxData.width() - boxData.left) - planeData.getWidth()/2;
				float right = left + boxData.width();
				box.getBoxData().set(left, boxData.top, right, boxData.bottom);
			}
			flipped = flip;
		}
	}
	
	public boolean isHitBoxActive(){
		for(HitBox box : hitBoxes){
			if(box.getActiveFrame().contains(animation.getFrame()))
				return true;
		}
		return false;
	}

	public boolean isHurtBoxActive(){
		for(HurtBox box : hurtBoxes){
			if(box.getActiveFrame().contains(animation.getFrame()))
				return true;
		}
		return false;
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

	public ActionProperties getActionProperties() {
		return actionProperties;
	}

	public void setActionProperties(ActionProperties actionProperties) {
		this.actionProperties = actionProperties;
	}

	public InteractionProperties getInterProperties() {
		return interProperties;
	}

	public void setInterProperties(InteractionProperties interProperties) {
		this.interProperties = interProperties;
	}

	/*
	public void setHitstop(int hitstop) {
		this.hitstop = hitstop;
	}

	public float getxInitSpeed() {
		return xInitSpeed;
	}

	public void setxInitSpeed(float xInitSpeed) {
		this.xInitSpeed = xInitSpeed;
	}

	public float getyInitSpeed() {
		return yInitSpeed;
	}

	public void setyInitSpeed(float yInitSpeed) {
		this.yInitSpeed = yInitSpeed;
	}

	public float getxAccel() {
		return xAccel;
	}

	public void setxAccel(float xAccel) {
		this.xAccel = xAccel;
	}

	public float getyAccel() {
		return yAccel;
	}

	public void setyAccel(float yAccel) {
		this.yAccel = yAccel;
	}

	public void addActionChange(String state, String nextState){
		actionChange.put(state, nextState);
	}
	
	public String getActionChangeState(String state){
		return actionChange.get(state);
	}

	public LinkedHashMap<String,String> getActionChange(){
		return actionChange;
	}
	*/
	/*
	public GameObject getPairedObj() {
		return pairedObj;
	}

	public void setPairedObj(GameObject pairedObj) {
		this.pairedObj = pairedObj;
	}
	*/
	
	
}
