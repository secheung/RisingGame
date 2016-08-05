package structure;

public class InteractionProperties extends ActionProperties {
	private int hitStop;
	private int hitStun;
	
	public InteractionProperties(){
		super();
		hitStop = 0;
		hitStun = 0;
	}
	
	public void copyActionProperties(ActionProperties properties){
		this.xInitSpeed = properties.xInitSpeed;
		this.yInitSpeed = properties.yInitSpeed;
		this.xAccel = properties.xAccel;
		this.yAccel = properties.yAccel;
		this.triggerChange = properties.triggerChange;
	}
	
	public int getHitStop() {
		return hitStop;
	}
	
	public void setHitStop(int hitStop) {
		this.hitStop = hitStop;
	}

	public int getHitStun() {
		return hitStun;
	}

	public void setHitStun(int hitStun) {
		this.hitStun = hitStun;
	}
}
