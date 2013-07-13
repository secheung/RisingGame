package game.open2d;

import android.content.Context;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;

public class GameLogic{
	
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

		Plane plane = new Plane(R.drawable.stand, 2.5f, 3.5f, -1.0f, 0.0f, -1.5f, 14, 10);
		Plane plane2 = new Plane(R.drawable.computer_look_back, 2.5f, 3.5f, 0.0f, 0.0f, -1.7f);
		Plane plane3 = new Plane(R.drawable.walk, 2.5f, 3.5f, -1.5f, 0.0f, -1.8f, 9, 6);
		
		worldRenderer.addDrawShape("myPlane3", plane3);
		worldRenderer.addDrawShape("myPlane2", plane2);
		worldRenderer.addDrawShape("myPlane", plane);
		
		worldRenderer.drawObject("myPlane");
		worldRenderer.drawObject("myPlane2");
		worldRenderer.drawObject("myPlane3");
	}
	
	public void update(){
		worldRenderer.drawObject("myPlane");
		worldRenderer.drawObject("myPlane2");
		worldRenderer.drawObject("myPlane3");
	}
}
