package engine.open2d.texture;

public class Texture {
	private int resourceId;
	private int compiledTexture;
	private float[] textureData;
	
	public Texture(int resourceId, float[] texture){
		this.resourceId = resourceId;
		this.textureData = texture;
	}

	public float[] getTextureData() {
		return textureData;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public int getCompiledTexture() {
		return compiledTexture;
	}

	public void setCompiledTexture(int compiledTexture) {
		this.compiledTexture = compiledTexture;
	}
}
