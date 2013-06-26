package engine.open2d.renderer;

import java.util.Hashtable;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderHandler {
	private String vShader;
	private String fShader;
	private String attributes[];
	
	private int compiledShaderProgram;
	
	public ShaderHandler(String vShader,String fShader, String...attributes){
		this.vShader = vShader;
		this.fShader = fShader;
		this.attributes = attributes;
	}
	
	public int getCompiledShaderProgram(){
		return compiledShaderProgram;
	}
	
	public void buildShaderProgram(){
		int vertexShaderHandle = compileShader(GLES20.GL_VERTEX_SHADER,vShader);
	    int fragmentShaderHandle = compileShader(GLES20.GL_FRAGMENT_SHADER, fShader);
		
	    compiledShaderProgram = createAndLinkProgram(vertexShaderHandle,fragmentShaderHandle, attributes);
	}
	
	private int compileShader(int shaderType, String shaderProgram){
		int shaderHandle = GLES20.glCreateShader(shaderType);
		
		if (shaderHandle == 0) 
			throw new RuntimeException("Error creating shader.");
		
    	GLES20.glShaderSource(shaderHandle, shaderProgram);
    	GLES20.glCompileShader(shaderHandle);
    	
    	// Get the compilation status.
    	final int[] compileStatus = new int[1];
    	GLES20.glGetShaderiv(shaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
    	
        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0){
        	Log.e("LessonOpenGLESRenderer", "Error compiling shader: " + GLES20.glGetShaderInfoLog(shaderHandle));
            GLES20.glDeleteShader(shaderHandle);
            shaderHandle = 0;
        }
	    
	    return shaderHandle;
	}
	
	public int createAndLinkProgram(final int vertexShaderProgram, final int fragmentShaderProgram, final String[] attribute){
	    // Create a program object and store the handle to it.
	    int programHandle = GLES20.glCreateProgram();
	    
	    if(programHandle == 0)
	        throw new RuntimeException("Error creating program.");
	    
    	GLES20.glAttachShader(programHandle, vertexShaderProgram);
    	GLES20.glAttachShader(programHandle, fragmentShaderProgram);
    	
    	if(attribute != null) {
    		for(int i = 0; i < attribute.length; i++)
		    	GLES20.glBindAttribLocation(programHandle, i, attribute[i]);
    	}
    	
    	GLES20.glLinkProgram(programHandle);
    	
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);
     
        if (linkStatus[0] == 0) {
        	Log.e("LessonOpenGLESRenderer", "Error compiling shader: " + GLES20.glGetShaderInfoLog(programHandle));
            GLES20.glDeleteProgram(programHandle);
            programHandle = 0;
        }
	    
	    return programHandle;
	}
	
}
