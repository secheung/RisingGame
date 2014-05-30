package structure;

public class PlaneData {
	private float width;
	private float height;
	private int rows;
	private int columns;
	
	public PlaneData(float width, float height, int rows, int columns){
		this.width = width;
		this.height = height;
		this.rows = rows;
		this.columns = columns;
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
	
}
