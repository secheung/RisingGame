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

		//Plane plane = new Plane(positionData,colorData,normalData);
		//plane.addTexture(textureData,R.drawable.stand);
		Plane plane = new Plane(2.5f, 3.5f, R.drawable.stand, 14, 10);
		plane.setTranslationX(-1.0f);
		plane.setTranslationY(0.0f);
		plane.setTranslationZ(-1.5f);

		//Plane plane2 = new Plane(positionData,colorData,normalData);
		Plane plane2 = new Plane(2.5f, 3.5f, R.drawable.stand, 14, 10);
		//Plane plane2 = new Plane(2.5f, 3.5f, R.drawable.computer_look_back, 1, 1);
		//plane2.addTexture("texture2", textureData, R.drawable.stand);
		plane2.setTranslationX(0.0f);
		plane2.setTranslationY(0.0f);
		plane2.setTranslationZ(-1.7f);
		
		worldRenderer.addDrawShape("myPlane2", plane2);
		worldRenderer.addDrawShape("myPlane", plane);
	}
	
	public void update(){
		//worldRenderer.updateDrawObject("myPlane2", row, column);
	}
}
