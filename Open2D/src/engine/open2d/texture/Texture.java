package engine.open2d.texture;

public class Texture {
	private int resourceId;
	private int compiledTexture;
	private float[] textureCoord;
	
	public Texture(int resourceId, float[] textureCoord){
		this.resourceId = resourceId;
		this.textureCoord = textureCoord;
	}

	public float[] getTextureCoord() {
		return textureCoord;
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
