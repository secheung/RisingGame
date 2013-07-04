package game.open2d;

import android.content.Context;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;

public class GameLogic {
	WorldRenderer worldRenderer;
	Context context;

	public GameLogic(Context context, WorldRenderer worldRenderer){
		this.worldRenderer = worldRenderer;
		this.context = context;

		worldRenderer.addCustomShader(	WorldRenderer.WORLD_SHADER,
										R.raw.vertex_shader_texture,
										R.raw.fragment_shader_texture,
										new String[]{"a_Position","a_Color","a_Normal","a_TexCoordinate"}
									);

		float[] positionData = {
	            // X, Y, Z,
			7.0f,  3.0f, 0.0f,
			3.0f,  3.0f, 0.0f,
			3.0f, -1.0f, 0.0f,
			3.0f, -1.0f, 0.0f,
			7.0f, -1.0f, 0.0f,
			7.0f,  3.0f, 0.0f
		};

		/*
		float[] positionData = {
	            // X, Y, Z,
			5.0f,  -5.0f, 0.0f,
			5.0f,  5.0f, 0.0f,
			-5.0f, 5.0f, 0.0f,
			-5.0f, 5.0f, 0.0f,
			-5.0f, -5.0f, 0.0f,
			5.0f,  -5.0f, 0.0f
		};
		*/

		float[] colorData = {
		    // R, G, B, A
			1.0f, 0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 1.0f, 1.0f,
			0.0f, 0.0f, 0.0f, 1.0f,
			1.0f, 0.0f, 0.0f, 1.0f
		};

		float[] normalData = {
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 0.0f, 1.0f
		};

		
		float[] textureData = {
			0.1f, 0.0f,
			0.0f, 0.0f,
			0.0f, 0.07f,
			0.0f, 0.07f,
			0.1f, 0.07f,
			0.1f, 0.0f
		};

		Plane plane = new Plane(positionData,colorData,normalData);
		plane.addTexture("texture", textureData,R.drawable.stand);
		plane.setTranslationX(-5.0f);
		plane.setTranslationY(0.0f);
		plane.setTranslationZ(-3.01f);

		Plane plane2 = new Plane(positionData,colorData,normalData);
		plane2.setTranslationX(-3.0f);
		plane2.setTranslationY(1.0f);
		plane2.setTranslationZ(-3.0f);
		
		worldRenderer.addDrawShape("myPlane", plane);
		worldRenderer.addDrawShape("myPlane2", plane2);
	}
}
