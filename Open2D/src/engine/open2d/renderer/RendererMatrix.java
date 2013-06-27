package engine.open2d.renderer;

import android.opengl.GLES20;

public class RendererMatrix { 
	public float[] modelMatrix = new float[16];
	public float[] viewMatrix = new float[16];
	public float[] projectionMatrix = new float[16];
	
	public float[] MVMatrix = new float[16];
	public float[] MVPMatrix = new float[16];
	
	//TODO should MOVE TO HANDLE OBJECT that is based on shader program
	int MVPMatrixHandle;
    int MVMatrixHandle;
    int LightPosHandle;
    int TextureUniformHandle;
    int PositionHandle;
    int ColorHandle;
    int NormalHandle;
    int TextureCoordinateHandle;
	
	public RendererMatrix(){
		
	}
	
	public void setHandles(int shaderProgram){
		MVPMatrixHandle = GLES20.glGetUniformLocation(shaderProgram, "u_MVPMatrix");
	    MVMatrixHandle = GLES20.glGetUniformLocation(shaderProgram, "u_MVMatrix");
	    LightPosHandle = GLES20.glGetAttribLocation(shaderProgram, "a_LightPos");
	    TextureUniformHandle = GLES20.glGetUniformLocation(shaderProgram, "u_Texture");
	    PositionHandle = GLES20.glGetAttribLocation(shaderProgram, "a_Position");
	    ColorHandle = GLES20.glGetAttribLocation(shaderProgram, "a_Color");
	    NormalHandle = GLES20.glGetAttribLocation(shaderProgram, "a_Normal");
	    TextureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgram, "a_TexCoordinate");
	}

	public int getMVPMatrixHandle() {
		return MVPMatrixHandle;
	}

	public void setMVPMatrixHandle(int mVPMatrixHandle) {
		MVPMatrixHandle = mVPMatrixHandle;
	}

	public int getMVMatrixHandle() {
		return MVMatrixHandle;
	}

	public void setMVMatrixHandle(int mVMatrixHandle) {
		MVMatrixHandle = mVMatrixHandle;
	}

	public int getLightPosHandle() {
		return LightPosHandle;
	}

	public void setLightPosHandle(int lightPosHandle) {
		LightPosHandle = lightPosHandle;
	}

	public int getTextureUniformHandle() {
		return TextureUniformHandle;
	}

	public void setTextureUniformHandle(int textureUniformHandle) {
		TextureUniformHandle = textureUniformHandle;
	}

	public int getPositionHandle() {
		return PositionHandle;
	}

	public void setPositionHandle(int positionHandle) {
		PositionHandle = positionHandle;
	}

	public int getColorHandle() {
		return ColorHandle;
	}

	public void setColorHandle(int colorHandle) {
		ColorHandle = colorHandle;
	}

	public int getNormalHandle() {
		return NormalHandle;
	}

	public void setNormalHandle(int normalHandle) {
		NormalHandle = normalHandle;
	}

	public int getTextureCoordinateHandle() {
		return TextureCoordinateHandle;
	}

	public void setTextureCoordinateHandle(int textureCoordinateHandle) {
		TextureCoordinateHandle = textureCoordinateHandle;
	}
}
