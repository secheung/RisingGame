package game.open2d;

import java.util.LinkedHashMap;

import object.Enemy;
import object.GameObject;
import object.Player;
import object.Enemy.EnemyState;
import android.content.Context;
import android.os.AsyncTask;
import android.view.MotionEvent;
import engine.open2d.renderer.WorldRenderer;

public class GameLogic extends AsyncTask<Void, Void, Void>{
	
	WorldRenderer worldRenderer;
	Context context;
	
	float camX = 0.0f;
	float camY = 0.0f;
	float camZ = 0.0f;
	
	int enemyLimit = 2;
	
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
		if(gameObjects.size() < enemyLimit){
			Enemy enemy0 = new Enemy(gameObjects, (Player)gameObjects.get("player"), 0, 3.7f, -1.0f, 3.5f, 3.5f);
			gameObjects.put(enemy0.getName(), enemy0);
		}
		
		for(GameObject gameObject : gameObjects.values()){
			gameObject.update();
			if(gameObject instanceof Enemy){
				Enemy enemy = (Enemy)gameObject;
				if(enemy.getEnemyState() == EnemyState.DEAD){
					gameObjects.remove(gameObject.getName());
				}
			}
		}
	}
	
	public void draw(){
		worldRenderer.setCamera(camX, camY, camZ);
		
		for(GameObject gameObject : gameObjects.values()){
			gameObject.draw(worldRenderer);
		}
	}

	public void passTouchEvents(MotionEvent e){
		if(e.getAction() == MotionEvent.ACTION_DOWN){
			for(GameObject gameObject : gameObjects.values()){
				gameObject.passTouchEvent(e, worldRenderer);
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
