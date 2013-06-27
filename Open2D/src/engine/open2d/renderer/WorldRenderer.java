package engine.open2d.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import engine.open2d.draw.Shape;
import engine.open2d.shader.Shader;
import engine.open2d.shader.ShaderTool;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

public class WorldRenderer implements GLSurfaceView.Renderer{
	private final static int BYTES_PER_FLOAT = 4;
	
	Context activityContext;
	RendererMatrix rendererMatrix;
	
	private int viewportWidth;
	private int viewportHeight;
	
	private int worldShaderProgram;
	private int lightShaderProgram;
	
	Map<String,Shader> shaders = null;
	LinkedHashMap<String,Shape> drawObjects;
	
	//singlton design pattern
    public WorldRenderer(final Context activityContext) { 
    	this.activityContext = activityContext;
    	rendererMatrix = new RendererMatrix();
    }

    /*
    private static class WorldRendererHolder { 
    	public static final WorldRenderer INSTANCE = new WorldRenderer();
    }

    public static WorldRenderer getInstance() {
    	return WorldRendererHolder.INSTANCE;
    }
    */
	//end singleton
    
    
	public void initSetup(){
		
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
	    Matrix.setLookAtM(rendererMatrix.viewMatrix, 0, 0.0f, 0.0f, -0.5f, 0.0f, 0.0f, -5.0f, 0.0f, 1.0f, 0.0f);
	    
	    buildShaders();
	    
	    //build textures
	}
	
	private void buildShaders(){
		ShaderTool shaderTool = new ShaderTool();
		
	    if(shaders.isEmpty())
			throw new RuntimeException("no shaders present");
	    
	    //TODO should organize the shaders better
	    worldShaderProgram = shaderTool.buildShaderProgram(shaders.get("worldShader"));
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		initSetup();
	}
	
	public void passTouchEvents(MotionEvent e){}

	@Override
	public void onDrawFrame(GL10 gl) {
		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUseProgram(worldShaderProgram);
        
		rendererMatrix.setHandles(worldShaderProgram);
        
		for(Shape shape : drawObjects.values()){
			/*TODO animated textures
			dataToload = intTextures.get(intObjectIndx)[levelCurrentAnims.get(intObjectIndx)];
			
			GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + intObjectIndx);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, dataToload);
        	GLES20.glUniform1i(mTextureUniformHandle,intObjectIndx);
        	*/
        	drawShape(shape);
		}
	}

	private void drawShape(Shape shape){
		float[] positionData = shape.getPositionData();
		float[] colorData = shape.getColorData();
		float[] normalData = shape.getNormalData();
		//TODO Textures
		
		
        FloatBuffer position = ByteBuffer.allocateDirect(positionData.length * BYTES_PER_FLOAT)
        								 .order(ByteOrder.nativeOrder())
        								 .asFloatBuffer();
        position.put(positionData).position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, Shape.POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, position);
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		
		
		FloatBuffer color = ByteBuffer.allocateDirect(colorData.length * BYTES_PER_FLOAT)
				 					  .order(ByteOrder.nativeOrder())
				 					  .asFloatBuffer();
		color.put(colorData).position(0);
		GLES20.glVertexAttribPointer(mColorHandle, Shape.COLOR_DATA_SIZE, GLES20.GL_FLOAT, false, 0, color);
		GLES20.glEnableVertexAttribArray(mColorHandle);
	    
		FloatBuffer normal = ByteBuffer.allocateDirect(normalData.length * BYTES_PER_FLOAT)
									   .order(ByteOrder.nativeOrder())
									   .asFloatBuffer();
	    normal.put(normalData).position(0);
	    GLES20.glVertexAttribPointer(mNormalHandle, Shape.NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, 0, normal);
	    GLES20.glEnableVertexAttribArray(mNormalHandle);
        
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);
		
		viewportWidth = width;
		viewportHeight = height;

		final float ratio = (float) width / height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 10.0f;

		Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);
	}
}
