package engine.open2d.renderer;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import engine.open2d.draw.Plane;
import engine.open2d.draw.Plane;
import engine.open2d.shader.Shader;
import engine.open2d.shader.ShaderTool;
import engine.open2d.texture.Texture;
import engine.open2d.texture.TextureTool;

public class WorldRenderer implements GLSurfaceView.Renderer{
	private final static String LOG_PREFIX = "WORLD_RENDERER";
	private final static String ITEM_EXISTS_WARNING = "Item exists in world renderer.  No Item added.";
	private final static String NO_ITEM_EXISTS_WARNING = "No Item exists in ";
	
	public final static String WORLD_SHADER = "world_shader";

	Context activityContext;
	RendererTool rendererTool;
	
	ShaderTool shaderTool;
	TextureTool textureTool;

	private int viewportWidth;
	private int viewportHeight;

	LinkedHashMap<String,Shader> shaders;
	LinkedHashMap<String,Plane> drawObjects;

	//singlton design pattern
    public WorldRenderer(final Context activityContext) {
    	this.activityContext = activityContext;
    	rendererTool = new RendererTool();
    	shaderTool = new ShaderTool(activityContext);
    	textureTool = new TextureTool(activityContext);
    	
    	shaders = new LinkedHashMap<String,Shader>();
    	drawObjects = new LinkedHashMap<String,Plane>();
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

	public void addDrawShape(String ref, Plane shape){
		if(drawObjects.containsKey(ref)){
			Log.w(LOG_PREFIX, ITEM_EXISTS_WARNING+" [shape : "+ref+"]");
			return;
		}

    	drawObjects.put(ref, shape);
    }
	
    public void addCustomShader(String ref, int vertResourceId, int fragResourceId, String...attributes){
    	if(shaders.containsKey(ref)){
			Log.w(LOG_PREFIX, ITEM_EXISTS_WARNING+" [shader: "+ref+"]");
			return;
		}
    	
    	String vertShader = shaderTool.getShaderFromResource(vertResourceId);
    	String fragShader = shaderTool.getShaderFromResource(fragResourceId);
    	
    	Shader shader = new Shader(vertShader,fragShader,attributes);
    	
    	shaders.put(ref, shader);
    }

    /*
    public void addTexture(String ref, int resourceId){
    	if(textures.containsKey(ref)){
			Log.w(LOG_PREFIX, ITEM_EXISTS_WARNING+" [texture : "+ref+"]");
			return;
		}
    	Texture texture = new Texture(resourceId);
    	textures.put(ref, texture);
    }
    */

	public void initSetup(){

		GLES20.glClearColor(0.0f, 104.0f/255.0f, 55.0f/255.0f, 0.0f);

		GLES20.glEnable(GLES20.GL_CULL_FACE);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glEnable(GLES20.GL_BLEND);
		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

		rendererTool.setLookAt(0,
								 0.0f, 0.0f, 0.0f,
								 0.0f, 0.0f, -1.0f,
								 0.0f, 1.0f, 0.0f);

	    //TODO build textures
		buildShaders();
		buildObjectTextures();
	}

	private void buildShaders(){
	    if(shaders == null || shaders.isEmpty()){
			Log.w(LOG_PREFIX, NO_ITEM_EXISTS_WARNING +" shaders");
			return;
	    }

	    for(Shader shader : shaders.values())
	    	shaderTool.buildShaderProgram(shader);
	}
	
	private void buildObjectTextures(){
		if(drawObjects == null || drawObjects.isEmpty()){
			Log.w(LOG_PREFIX, NO_ITEM_EXISTS_WARNING+ " textures");
			return;
	    }
		
	    for(Plane shape : drawObjects.values()){
	    	LinkedHashMap<String,Texture> textures = shape.getTextures();
	    	if(!(textures == null || textures.isEmpty())){
		    	for(Texture texture : textures.values()){
		    		textureTool.loadTexture(texture);
		    	}
	    	}
	    }
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		initSetup();
	}

	public void passTouchEvents(MotionEvent e){}

	@Override
	public void onDrawFrame(GL10 gl) {
		int worldShaderProgram = shaders.get(WORLD_SHADER).getShaderProgram();

		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		GLES20.glUseProgram(worldShaderProgram);

		rendererTool.setHandles(shaders.get(WORLD_SHADER));

		for(Plane shape : drawObjects.values()){
        	drawShape(shape);
		}
	}

	private void drawShape(Plane plane){
		float[] positionData = plane.getPositionData();
		float[] colorData = plane.getColorData();
		float[] normalData = plane.getNormalData();

		//TODO Textures
		//TODO MAKE SO NOT HARDCODED
		Map<String,Integer> handles = rendererTool.getHandles();

		rendererTool.enableHandles("a_Position", positionData, Plane.POSITION_DATA_SIZE);
		rendererTool.enableHandles("a_Color", colorData, Plane.COLOR_DATA_SIZE);
		rendererTool.enableHandles("a_Normal", normalData, Plane.NORMAL_DATA_SIZE);

	    if(!(plane.getTextures() == null || plane.getTextures().isEmpty())){
	    	
	    	int textureUniformHandle = handles.get("u_Texture");
	    	
		    //TODO needs object index on active and uniform
		    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, plane.getCurrentTexture().getCompiledTexture());
	    	GLES20.glUniform1i(textureUniformHandle,0);
	    	
	    	Texture shapeTexture = plane.getCurrentTexture();
	    	float[] textureData = shapeTexture.getTextureData();
	    	rendererTool.enableHandles("a_TexCoordinate", textureData, Plane.TEXTURE_DATA_SIZE);
	    }
	    
	    
	    int mvMatrixHandle = handles.get("u_MVMatrix");
	    int mvpMatrixHandle = handles.get("u_MVPMatrix");
        rendererTool.translateModelMatrix(plane.getTranslationX(),plane.getTranslationY(),plane.getTranslationZ());
        
		GLES20.glUniformMatrix4fv(mvMatrixHandle, 1, false, rendererTool.getMVMatrix(), 0);
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, rendererTool.getMVPMatrix(), 0);

	    GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);

	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		viewportWidth = width;
		viewportHeight = height;

		final float ratio = (float) width/height;
		final float left = -ratio;
		final float right = ratio;
		final float bottom = -1.0f;
		final float top = 1.0f;
		final float near = 1.0f;
		final float far = 10.0f;

		rendererTool.setFrustum(0, left, right, bottom, top, near, far);
	}
}