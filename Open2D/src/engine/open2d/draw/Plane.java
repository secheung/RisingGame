package engine.open2d.draw;

import android.util.Log;
import engine.open2d.texture.AnimatedTexture;
import engine.open2d.texture.Texture;

public class Plane {
	public final static int POSITION_DATA_SIZE = 3;
	public final static int COLOR_DATA_SIZE = 4;
	public final static int NORMAL_DATA_SIZE = 3;
	public final static int TEXTURE_DATA_SIZE = 2;
	
	private final static float DEFAULT_Z_DISTANCE = -2.0f;
	
	Texture texture;
	public String name;
	
	private boolean draw;
	
    protected float[] positionData = {
            // X, Y, Z,
		1.0f,  1.0f, 0.0f,
		1.0f,  1.0f, 0.0f,
		1.0f, -1.0f, 0.0f,
		1.0f, -1.0f, 0.0f,
		1.0f, -1.0f, 0.0f,
		1.0f,  1.0f, 0.0f
	};
    
    protected float[] colorData = {
	    // R, G, B, A
		1.0f, 0.0f, 0.0f, 1.0f,
		0.0f, 1.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f, 1.0f,
		0.0f, 0.0f, 1.0f, 1.0f,
		0.0f, 0.0f, 0.0f, 1.0f,
		1.0f, 0.0f, 0.0f, 1.0f
	};

		
    protected float[] normalData= {
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f
	};

    protected float translationX;
    protected float translationY;
    protected float translationZ;

    protected float rotationX;
    protected float rotationY;
    protected float rotationZ;

    protected float scaleX;
    protected float scaleY;
    protected float scaleZ;

    public Plane(	int referenceId, 
    				float planeWidth, float planeHeight,
    				float x, float y, float z){
    	
    	initPlane(planeWidth, planeHeight, x,y,z);
		texture = new Texture(referenceId);
	}

	public Plane(	int referenceId, 
					float planeWidth, float planeHeight,
					float x, float y, float z,
					int rows, int columns){
		
		initPlane(planeWidth, planeHeight, x,y,z);
		texture = new AnimatedTexture(referenceId, rows, columns);
	}

	private void initPlane(	float planeWidth, float planeHeight,
						float x, float y, float z){
//		float[] box = {
//				planeWidth,  planeHeight,	DEFAULT_Z_DISTANCE,
//				0.0f,   	 planeHeight,	DEFAULT_Z_DISTANCE,
//				0.0f,   	 0.0f,			DEFAULT_Z_DISTANCE,
//				0.0f,   	 0.0f,			DEFAULT_Z_DISTANCE,
//				planeWidth,  0.0f,			DEFAULT_Z_DISTANCE,
//				planeWidth,  planeHeight,	DEFAULT_Z_DISTANCE
//		};
		float[] box = {
				planeWidth,  planeHeight,	DEFAULT_Z_DISTANCE,
				0.0f,   	 planeHeight,	DEFAULT_Z_DISTANCE,
				0.0f,   	 0.0f,			DEFAULT_Z_DISTANCE,
				0.0f,   	 0.0f,			DEFAULT_Z_DISTANCE,
				planeWidth,  0.0f,			DEFAULT_Z_DISTANCE,
				planeWidth,  planeHeight,	DEFAULT_Z_DISTANCE
		};
		positionData = box;

		setTranslationX(x);
		setTranslationY(y);
		setTranslationZ(z);
		
		draw = false;
	}
	
	public void update(){
		if(texture instanceof AnimatedTexture){
			((AnimatedTexture) texture).incrementFrame();
		}
	}
	
	public void setDraw(boolean enable){
		draw = enable;
	}
	
	public boolean isDrawEnabled(){
		return draw;
	}
	
	public Texture getTexture() {
		return texture;
	}

	public float[] getPositionData() {
		return positionData;
	}

	public float[] getColorData() {
		return colorData;
	}

	public float[] getNormalData() {
		return normalData;
	}

	public float getTranslationX() {
		return translationX;
	}

	public void setTranslationX(float translationX) {
		this.translationX = translationX;
	}

	public float getTranslationY() {
		return translationY;
	}

	public void setTranslationY(float translationY) {
		this.translationY = translationY;
	}

	public float getTranslationZ() {
		return translationZ;
	}

	public void setTranslationZ(float translationZ) {
		this.translationZ = translationZ;
	}

	public float getRotationX() {
		return rotationX;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public void setRotationZ(float rotationZ) {
		this.rotationZ = rotationZ;
	}

	public float getScaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public float getScaleZ() {
		return scaleZ;
	}

	public void setScaleZ(float scaleZ) {
		this.scaleZ = scaleZ;
	}
}
