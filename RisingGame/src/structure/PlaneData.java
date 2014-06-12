package structure;

import engine.open2d.texture.AnimatedTexture.Playback;

public class PlaneData {
	private float width;
	private float height;
	private int rows;
	private int columns;
	private Playback playback;
	
	public PlaneData(float width, float height, int rows, int columns,Playback playback){
		this.width = width;
		this.height = height;
		this.rows = rows;
		this.columns = columns;
		this.playback = playback;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public Playback getPlayback() {
		return playback;
	}

	public void setPlayback(Playback playback) {
		this.playback = playback;
	}
	
}
