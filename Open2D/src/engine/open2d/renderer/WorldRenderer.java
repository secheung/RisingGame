package engine.open2d.renderer;

import java.util.Hashtable;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

public class WorldRenderer implements GLSurfaceView.Renderer{
	private final static int BYTESPERFLOATS = 4;
	
	Context activityContext;
	
	private float[] modelMatrix = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] projectionMatrix = new float[16];
	
	private float[] MVMatrix = new float[16];
	private float[] MVPMatrix = new float[16];
	
	ShaderHandlers shaders;
	
	public WorldRenderer(final Context activityContext) {
		this.activityContext = activityContext;
		
	}
	
	public void initSetup(){
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
	    Matrix.setLookAtM(viewMatrix, 0, 0.0f, 0.0f, -0.5f, 0.0f, 0.0f, -5.0f, 0.0f, 1.0f, 0.0f);
	    
	    shaders.buildShaderProgram();
	}
	
	public void passTouchEvents(MotionEvent e){}

	@Override
	public void onDrawFrame(GL10 gl) {}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {}
	
}
