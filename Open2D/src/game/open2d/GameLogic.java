package game.open2d;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;

public class GameLogic extends AsyncTask<Void, Void, Void>{
	
	WorldRenderer worldRenderer;
	Context context;

	float index = 0.0f;
	float x1 = 0.0f;
	float y1 = 0.0f;
	float z1 = -1.0f;
	
	float x2 = -3.5f;
	float y2 = 0.0f;
	float z2 = -7.0f;
	
	float camX = 0.0f;
	float camY = 0.0f;
	float camZ = 0.0f;
	
	Plane plane;
	Plane plane2;
	Plane plane3;
	Plane plane4;
	
	public GameLogic(Context context, WorldRenderer worldRenderer){
		this.worldRenderer = worldRenderer;
		this.context = context;

		worldRenderer.addCustomShader(	WorldRenderer.WORLD_SHADER,
										R.raw.vertex_shader_texture,
										R.raw.fragment_shader_texture,
										new String[]{"a_Position","a_Color","a_Normal","a_TexCoordinate"}
									);

		plane = new Plane(R.drawable.stand, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 14, 10);
		plane = new Plane(R.drawable.stance, 3.5f, 3.5f, x1, y1, z1, 4, 7);
		plane2 = new Plane(R.drawable.stand, 2.5f, 3.5f, x1, y1, z1, 14, 10);
//		plane2 = new Plane(R.drawable.computer_look_back, 2.5f, 3.5f, 0.0f, 0.0f, -1.7f);
		plane3 = new Plane(R.drawable.walk, 2.5f, 3.5f, x2, y2, z2, 9, 6);
		plane4 = new Plane(R.drawable.walk, 2.5f, 3.5f, -4.0f, 0.0f, -1.8f, 9, 6);

//		asdf;//should consider alt load method
		worldRenderer.addDrawShape("myPlane4", plane4);
		worldRenderer.addDrawShape("myPlane3", plane3);
		worldRenderer.addDrawShape("myPlane2", plane2);
		worldRenderer.addDrawShape("myPlane", plane);
	}
	
	public void update(){
		worldRenderer.setCamera(camX, camY, camZ);
		
		plane.setTranslationX(x1);
		plane.setTranslationY(y1);
		plane.setTranslationZ(z1);
		plane.enable();
		worldRenderer.drawObject(plane);
		
		plane2.enable();
		worldRenderer.drawObject(plane2,x1-1,y1,z1-1);
		
		plane3.enable();
		worldRenderer.drawObject(plane3,x1-2,y1,z1-2);
		
		plane4.enable();
		worldRenderer.drawObject(plane4,x1-3,y1,z1-3);
		
		
	}

	public void passTouchEvents(MotionEvent e){
		if(e.getAction() == MotionEvent.ACTION_DOWN){
			Plane selected = worldRenderer.getSelectedObjection(e.getX(), e.getY());
			float[] unprojectedPoints = worldRenderer.getUnprojectedPoints(e.getX(), e.getY(), "myPlane");
	
			selected.flipTexture(!selected.isFlipped());
			
//			if(x1 < unprojectedPoints[0])
//				x1 += 1.0f;
//			else if(x1 > unprojectedPoints[0])
//				x1 -= 1.0f;
			
			if(selected != null)
				Log.d("Open2D", selected.getRefName());
		}
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		//TODO fix up later - find method for manual renders
	    int TICKS_PER_SECOND = 24;
	    int SKIP_TICKS = 1000 / TICKS_PER_SECOND;

	    long next_game_tick = System.currentTimeMillis();

	    boolean game_is_running = true;
	    while( game_is_running ) {
	        while( System.currentTimeMillis() > next_game_tick) {
	            update();

	            next_game_tick += SKIP_TICKS;
	        }
	    }
	    
	    return null;
	}
}
