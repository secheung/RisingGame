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
		
		float[] textureCoord = {
			frameHeight,0.0f,
			0.0f, 		0.0f,
			0.0f, 		frameWidth,
			0.0f, 		frameWidth,
			frameHeight,frameWidth,
			frameHeight,0.0f
		};
		
		this.textureCoord = textureCoord;
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

		textureCoord[0] = frameHeight*column + frameHeight;
		textureCoord[1] = frameWidth*row;
		
		textureCoord[2] = frameHeight*column;
		textureCoord[3] = frameWidth*row;
		
		textureCoord[4] = frameHeight*column;
		textureCoord[5] = frameWidth*row + frameWidth;
		
		textureCoord[6] = frameHeight*column;
		textureCoord[7] = frameWidth*row + frameWidth;
		
		textureCoord[8] = frameHeight*column + frameHeight;
		textureCoord[9] = frameWidth*row + frameWidth;
		
		textureCoord[10] = frameHeight*column + frameHeight;
		textureCoord[11] = frameWidth*row;
	}
}
