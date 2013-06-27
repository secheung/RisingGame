package engine.open2d.draw;

public abstract class Shape {
	public final static int POSITION_DATA_SIZE = 3;
	public final static int COLOR_DATA_SIZE = 4;
	public final static int NORMAL_DATA_SIZE = 3;
	
    protected float[] positionData;
    protected float[] colorData;
    protected float[] normalData;
    
	public float[] getPositionData() {
		return positionData;
	}
	
	public float[] getColorData() {
		return colorData;
	}

	public float[] getNormalData() {
		return normalData;
	}
}
