package engine.open2d.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import engine.open2d.draw.Shape;
import engine.open2d.shader.Shader;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class RendererTool {
	private final static int BYTES_PER_FLOAT = 4;
	
	public float[] modelMatrix = new float[16];
	public float[] viewMatrix = new float[16];
	public float[] projectionMatrix = new float[16];

	Map<String,Integer> handles;

	public RendererTool(){
		handles = new HashMap<String,Integer>();
	}

	public float[] getModelMatrix() {
		return modelMatrix;
	}

	public void setModelMatrix(float[] modelMatrix) {
		this.modelMatrix = modelMatrix;
	}

	public float[] getViewMatrix() {
		return viewMatrix;
	}

	public void setViewMatrix(float[] viewMatrix) {
		this.viewMatrix = viewMatrix;
	}

	public float[] getProjectionMatrix() {
		return projectionMatrix;
	}

	public void setProjectionMatrix(float[] projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}

	public Map<String, Integer> getHandles() {
		return handles;
	}

	public void setHandles(Map<String, Integer> handles) {
		this.handles = handles;
	}

	public void setHandles(Shader shader){
		int shaderProgram = shader.getShaderProgram();

		//handles from shader
		for(String attribute:shader.getAttributes()){
			handles.put(attribute, GLES20.glGetAttribLocation(shaderProgram, attribute));
		}

		//handles for matrices
		handles.put("u_MVMatrix", GLES20.glGetUniformLocation(shaderProgram, "u_MVMatrix"));
		handles.put("u_MVPMatrix", GLES20.glGetUniformLocation(shaderProgram, "u_MVPMatrix"));
		handles.put("u_Texture", GLES20.glGetUniformLocation(shaderProgram, "u_Texture"));
	}

	public void enableHandles(String attribute, float[] data, int dataElementSize){
		int handle = handles.get(attribute);
        FloatBuffer buffer = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT)
        								 .order(ByteOrder.nativeOrder())
        								 .asFloatBuffer();
        buffer.put(data).position(0);
        GLES20.glVertexAttribPointer(handle, dataElementSize, GLES20.GL_FLOAT, false, 0, buffer);
		GLES20.glEnableVertexAttribArray(handle);
	}
	
	public void setLookAt(int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ){
		Matrix.setLookAtM(viewMatrix, rmOffset, eyeX, eyeY, eyeZ, 
												centerX, centerY, centerZ,
												upX, upY, upZ);
	}

	public void setFrustum(int offset, float left, float right, float bottom, float top, float near, float far){
		Matrix.frustumM(projectionMatrix, offset, left, right, bottom, top, near, far);
	}

	public void translateModelMatrix(float changeX, float changeY, float changeZ){
		Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, changeX, changeY, changeZ);
	}

	public float[] getMVMatrix(){
		float[] mvMatrix = new float[16];//TODO WILL THIS CAUSE PROBLEMS?????
		Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
		return mvMatrix;
	}

	public float[] getMVPMatrix(){
		float[] mvMatrix = new float[16];
		float[] mvpMatrix = new float[16];
		//TODO TWO MATRIX MULT COULD CAUSE SLOWDOWN
		Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0);
		Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0);

		return mvpMatrix;
	}
}