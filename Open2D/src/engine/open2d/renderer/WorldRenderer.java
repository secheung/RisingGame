package engine.open2d.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import engine.open2d.draw.Shape;
import engine.open2d.shader.Shader;
import engine.open2d.shader.ShaderTool;
import engine.open2d.texture.Texture;
import engine.open2d.texture.TextureTool;

public class WorldRenderer implements GLSurfaceView.Renderer{
	private final static String LOG_PREFIX = "WORLD_RENDERER";
	private final static String ITEM_EXISTS_WARNING = "Item exists in world renderer.  No Item added.";
	private final static String NO_ITEM_EXISTS_WARNING = "No Item exists in ";
	
	public final static String WORLD_SHADER = "world_shader";

	private final static int BYTES_PER_FLOAT = 4;

	Context activityContext;
	RendererTool rendererMatrix;
	
	ShaderTool shaderTool;
	TextureTool textureTool;

	private int viewportWidth;
	private int viewportHeight;

	LinkedHashMap<String,Shader> shaders;
	LinkedHashMap<String,Shape> drawObjects;

	//singlton design pattern
    public WorldRenderer(final Context activityContext) {
    	this.activityContext = activityContext;
    	rendererMatrix = new RendererTool();
    	shaderTool = new ShaderTool(activityContext);
    	textureTool = new TextureTool(activityContext);
    	
    	shaders = new LinkedHashMap<String,Shader>();
    	drawObjects = new LinkedHashMap<String,Shape>();
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

	public void addDrawShape(String ref, Shape shape){
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

		rendererMatrix.setLookAt(0,
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
		
	    for(Shape shape : drawObjects.values()){
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

		rendererMatrix.setHandles(shaders.get(WORLD_SHADER));

		for(Shape shape : drawObjects.values()){
        	drawShape(shape);
		}
	}

	private void drawShape(Shape shape){
		float[] positionData = shape.getPositionData();
		float[] colorData = shape.getColorData();
		float[] normalData = shape.getNormalData();

		//TODO Textures
		//TODO MAKE SO NOT HARDCODED
		Map<String,Integer> handles = rendererMatrix.getHandles();

		int posHandle = handles.get("a_Position");
        FloatBuffer position = ByteBuffer.allocateDirect(positionData.length * BYTES_PER_FLOAT)
        								 .order(ByteOrder.nativeOrder())
        								 .asFloatBuffer();
        position.put(positionData).position(0);
        GLES20.glVertexAttribPointer(posHandle, Shape.POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, position);
		GLES20.glEnableVertexAttribArray(posHandle);

		int colorHandle = handles.get("a_Color");
		FloatBuffer color = ByteBuffer.allocateDirect(colorData.length * BYTES_PER_FLOAT)
				 					  .order(ByteOrder.nativeOrder())
				 					  .asFloatBuffer();
		color.put(colorData).position(0);
		GLES20.glVertexAttribPointer(colorHandle, Shape.COLOR_DATA_SIZE, GLES20.GL_FLOAT, false, 0, color);
		GLES20.glEnableVertexAttribArray(colorHandle);

		int normalHandle = handles.get("a_Normal");
		FloatBuffer normal = ByteBuffer.allocateDirect(normalData.length * BYTES_PER_FLOAT)
									   .order(ByteOrder.nativeOrder())
									   .asFloatBuffer();
	    normal.put(normalData).position(0);
	    GLES20.glVertexAttribPointer(normalHandle, Shape.NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, 0, normal);
	    GLES20.glEnableVertexAttribArray(normalHandle);

	    if(!(shape.getTextures() == null || shape.getTextures().isEmpty())){
	    	
	    	int textureUniformHandle = handles.get("u_Texture");
	    	
		    //TODO needs object index on active and uniform
		    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, shape.getCurrentTexture().getCompiledTexture());
	    	GLES20.glUniform1i(textureUniformHandle,0);
	    	
	    	Texture shapeTexture = shape.getCurrentTexture();
	    	float[] textureData = shapeTexture.getTextureData();
			int textureHandle = handles.get("a_TexCoordinate");
			FloatBuffer texture = ByteBuffer.allocateDirect(textureData.length * BYTES_PER_FLOAT)
										   	.order(ByteOrder.nativeOrder())
										   	.asFloatBuffer();
			texture.put(textureData).position(0);
		    GLES20.glVertexAttribPointer(textureHandle, Shape.TEXTURE_DATA_SIZE, GLES20.GL_FLOAT, false, 0, texture);
		    GLES20.glEnableVertexAttribArray(textureHandle);
	    }
	    
	    
	    int mvMatrixHandle = handles.get("u_MVMatrix");
	    int mvpMatrixHandle = handles.get("u_MVPMatrix");
        rendererMatrix.translateModelMatrix(shape.getTranslationX(),shape.getTranslationY(),shape.getTranslationZ());
		GLES20.glUniformMatrix4fv(mvMatrixHandle, 1, false, rendererMatrix.getMVMatrix(), 0);
		GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, rendererMatrix.getMVPMatrix(), 0);

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

		rendererMatrix.setFrustum(0, left, right, bottom, top, near, far);
	}
}