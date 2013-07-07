package engine.open2d.draw;

import java.util.LinkedHashMap;

import engine.open2d.texture.Texture;

public class Plane {
	public final static int POSITION_DATA_SIZE = 3;
	public final static int COLOR_DATA_SIZE = 4;
	public final static int NORMAL_DATA_SIZE = 3;
	public final static int TEXTURE_DATA_SIZE = 2;
	
	Texture texture;
	String currentTexture;
	
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
	};;

    protected float translationX;
    protected float translationY;
    protected float translationZ;

    protected float rotationX;
    protected float rotationY;
    protected float rotationZ;

    protected float scaleX;
    protected float scaleY;
    protected float scaleZ;
    
    private float frameWidth;
    private float frameHeight;
    
	public Plane(float[] position, float[] color, float[] normal){
		this.positionData = position;
		this.colorData = color;
		this.normalData = normal;
	}
    
	public Plane(float frameWidth, float frameHeight, int referenceId, int rows, int columns){
		float[] box = {
			 frameWidth, frameHeight, -2.0f,
			 0.0f,   	 frameHeight, -2.0f,
			 0.0f,   	 0.0f,   	 -2.0f,
			 0.0f,   	 0.0f,   	 -2.0f,
			 frameWidth, 0.0f,   	 -2.0f,
			 frameWidth, frameHeight, -2.0f
		};
		
		this.positionData = box;
		
		prepareTexture(referenceId,rows,columns);
	}
	

	public void prepareTexture(int referenceId, int rows, int columns){

		frameWidth = 1.0f/rows;
		frameHeight = 1.0f/columns;
		
		float[] textureCoord = {
			frameHeight,0.0f,
			0.0f, 		0.0f,
			0.0f, 		frameWidth,
			0.0f, 		frameWidth,
			frameHeight,frameWidth,
			frameHeight,0.0f
		};
		
		texture = new Texture(referenceId, textureCoord);
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
