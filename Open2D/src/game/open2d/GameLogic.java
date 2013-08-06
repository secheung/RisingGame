package game.open2d;

import object.Player;
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
	Plane plane5;
	Plane plane6;
	Plane plane7;
	Plane plane8;
	
	Player player;
	
	public GameLogic(Context context, WorldRenderer worldRenderer){
		this.worldRenderer = worldRenderer;
		this.context = context;

		worldRenderer.addCustomShader(	WorldRenderer.WORLD_SHADER,
										R.raw.vertex_shader_texture,
										R.raw.fragment_shader_texture,
										new String[]{"a_Position","a_Color","a_Normal","a_TexCoordinate"}
									);

		plane = new Plane(R.drawable.rising_stance, "plane", 3.5f, 3.5f, x1, y1, z1, 4, 7);
		plane2 = new Plane(R.drawable.stand, "plane2", 2.5f, 3.5f, x1, y1, z1, 14, 10);
		plane3 = new Plane(R.drawable.walk, "plane3", 2.5f, 3.5f, x2, y2, z2, 9, 6);
		plane4 = new Plane(R.drawable.walk, "plane4", 2.5f, 3.5f, -4.0f, 0.0f, -1.8f, 9, 6);
		plane5 = new Plane(R.drawable.walk, "plane5", 2.5f, 3.5f, -4.0f, 0.0f, -1.8f, 9, 6);
		plane6 = new Plane(R.drawable.walk, "plane6", 2.5f, 3.5f, -4.0f, 0.0f, -1.8f, 9, 6);
		plane7 = new Plane(R.drawable.walk, "plane7", 2.5f, 3.5f, -4.0f, 0.0f, -1.8f, 9, 6);
		plane8 = new Plane(R.drawable.stand, "plane8", 2.5f, 3.5f, 1.0f, 0.0f, -1.8f, 14, 10);

//		should consider alt load method
		worldRenderer.addDrawShape(plane8);
		worldRenderer.addDrawShape(plane7);
		worldRenderer.addDrawShape(plane6);
		worldRenderer.addDrawShape(plane5);
		worldRenderer.addDrawShape(plane4);
		worldRenderer.addDrawShape(plane3);
		worldRenderer.addDrawShape(plane2);
		worldRenderer.addDrawShape(plane);
		
		player = new Player(0.0f, 0.0f, 3.5f, 3.5f);
		player.loadAnimIntoRenderer(worldRenderer);
		
	}
	
	public void update(){
		worldRenderer.setCamera(camX, camY, camZ);
		worldRenderer.drawObject(player.getDisplay(), player.getX(), player.getY(), player.getZ());
//		plane.setTranslationX(x1);
//		plane.setTranslationY(y1);
//		plane.setTranslationZ(z1);
//		plane.enable();
//		worldRenderer.drawObject(plane);
//		
//		plane2.enable();
//		worldRenderer.drawObject(plane2,x1-2,y1,z1-1);
//		
//		plane3.enable();
//		worldRenderer.drawObject(plane3,x1-4,y1,z1-2);
//		
//		plane4.enable();
//		worldRenderer.drawObject(plane4,x1-6,y1,z1-3);
//
//		plane5.enable();
//		worldRenderer.drawObject(plane5,x1-8,y1,z1-3);
//		
//		plane6.enable();
//		worldRenderer.drawObject(plane6,x1-10,y1,z1-3);
//		
//		plane7.enable();
//		worldRenderer.drawObject(plane7,x1-12,y1,z1-3);
//		
//		plane8.enable();
//		worldRenderer.drawObject(plane8,1.0f,y1,z1-3);
		
	}

	public void passTouchEvents(MotionEvent e){
		if(e.getAction() == MotionEvent.ACTION_DOWN){
			Plane selected = worldRenderer.getSelectedPlane(e.getX(), e.getY());
			
			if(selected != null){
				float[] unprojectedPoints = worldRenderer.getUnprojectedPoints(e.getX(), e.getY(), selected);
				selected.flipTexture(!selected.isFlipped());
				Log.d("Open2D", selected.getRefName());
			}
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
