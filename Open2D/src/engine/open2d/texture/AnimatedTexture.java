package engine.open2d.texture;

import android.util.Log;


public class AnimatedTexture extends Texture{
	public enum Playback{
		PLAY,
		REVERSE,
		ONCE,
		BOUNCE
	}
	
	int frameRate;
	Playback playback;
	
    private float frameWidth;
    private float frameHeight;
    private int rows;
    private int columns;
    private int totalFrames;
    private int currentFrame;
    private int frameIncrement;
	
	public AnimatedTexture(int resourceId, int rows, int columns){
		super(resourceId);
		
		this.rows = rows;
		this.columns = columns;
		this.frameWidth = 1.0f/rows;
		this.frameHeight = 1.0f/columns;
		this.totalFrames = rows*columns;
		this.currentFrame = 1;
		this.frameIncrement= 1;
	}
	
	public void incrementFrame(){
		currentFrame += frameIncrement;
		if(currentFrame >= totalFrames){
			currentFrame = 1;
		} else if(currentFrame <= 0) {
			currentFrame = totalFrames - 1;
		}
		updateTextureCoord(currentFrame);
	}
	
	public void setFrame(int frame){
		updateTextureCoord(frame);
	}
	
	private void updateTextureCoord(int frame){
		
		int row = ((int)frame / columns);
		int column = ((int)frame % columns);
		
		if(!flipped){
			float[] texCoord = {
					frameHeight*column + frameHeight,	frameWidth*row,
					frameHeight*column,					frameWidth*row,
					frameHeight*column,					frameWidth*row + frameWidth,
					frameHeight*column,					frameWidth*row + frameWidth,
					frameHeight*column + frameHeight,	frameWidth*row + frameWidth,
					frameHeight*column + frameHeight,	frameWidth*row
			};
			textureCoord = texCoord;
		} else {
			float[] texCoord = {
				frameHeight*column,					frameWidth*row,
				frameHeight*column + frameHeight,	frameWidth*row,
				frameHeight*column + frameHeight,	frameWidth*row + frameWidth,
				frameHeight*column + frameHeight,	frameWidth*row + frameWidth,
				frameHeight*column,					frameWidth*row + frameWidth,
				frameHeight*column,					frameWidth*row
			};
			textureCoord = texCoord;
		}
		
	}
}
