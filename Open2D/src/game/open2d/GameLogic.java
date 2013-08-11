package game.open2d;

import java.util.LinkedHashMap;

import object.Enemy;
import object.GameObject;
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
	
	float camX = 0.0f;
	float camY = 0.0f;
	float camZ = 0.0f;
	
	LinkedHashMap<String,GameObject> gameObjects;
	
	public GameLogic(Context context, WorldRenderer worldRenderer){
		this.worldRenderer = worldRenderer;
		this.context = context;

		worldRenderer.addCustomShader(	WorldRenderer.WORLD_SHADER,
										R.raw.vertex_shader_texture,
										R.raw.fragment_shader_texture,
										new String[]{"a_Position","a_Color","a_Normal","a_TexCoordinate"}
									);
		
		gameObjects = new LinkedHashMap<String,GameObject>();
		
		Player player = new Player(gameObjects, 3.0f, -1.0f, 3.5f, 3.5f);
		player.loadAnimIntoRenderer(worldRenderer);
		
		Enemy enemy0 = new Enemy(gameObjects, player, 0, -3.7f, -1.0f, 3.5f, 3.5f);
		enemy0.loadAnimIntoRenderer(worldRenderer);
		
		gameObjects.put(player.getName(), player);
		gameObjects.put(enemy0.getName(), enemy0);
	}
	
	public void update(){
		for(GameObject gameObject : gameObjects.values()){
			gameObject.update();
		}
	}
	
	public void draw(){
		worldRenderer.setCamera(camX, camY, camZ);
		
		for(GameObject gameObject : gameObjects.values()){
			gameObject.draw(worldRenderer);
		}
	}

	public void passTouchEvents(MotionEvent e){
		for(GameObject gameObject : gameObjects.values()){
			float[] unprojectedPoints = worldRenderer.getUnprojectedPoints(e.getX(), e.getY(), gameObject.getDisplay());
			gameObject.passTouchEvent(unprojectedPoints);
		}
		
		if(e.getAction() == MotionEvent.ACTION_DOWN){
			
			Plane selected = worldRenderer.getSelectedPlane(e.getX(), e.getY());
			
			if(selected != null){
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
	            draw();
	            next_game_tick += SKIP_TICKS;
	        }
	    }
	    
	    return null;
	}
}
