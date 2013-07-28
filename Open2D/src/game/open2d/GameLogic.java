package game.open2d;

import android.content.Context;
import android.os.AsyncTask;
import android.view.MotionEvent;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;

public class GameLogic extends AsyncTask<Void, Void, Void>{
	
	WorldRenderer worldRenderer;
	Context context;

	int index = 0;
	
	public GameLogic(Context context, WorldRenderer worldRenderer){
		this.worldRenderer = worldRenderer;
		this.context = context;

		worldRenderer.addCustomShader(	WorldRenderer.WORLD_SHADER,
										R.raw.vertex_shader_texture,
										R.raw.fragment_shader_texture,
										new String[]{"a_Position","a_Color","a_Normal","a_TexCoordinate"}
									);

		Plane plane = new Plane(R.drawable.stand, 2.5f, 3.5f, 0.0f, 0.0f, -1.0f, 14, 10);
//		Plane plane = new Plane(R.drawable.stand, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 14, 10);
		Plane plane2 = new Plane(R.drawable.computer_look_back, 2.5f, 3.5f, 0.0f, 0.0f, -1.7f);
		Plane plane3 = new Plane(R.drawable.walk, 2.5f, 3.5f, -1.5f, 0.0f, -1.8f, 9, 6);
		Plane plane4 = new Plane(R.drawable.walk, 2.5f, 3.5f, -4.0f, 0.0f, -1.8f, 9, 6);
//		
		worldRenderer.addDrawShape("myPlane4", plane4);
		worldRenderer.addDrawShape("myPlane3", plane3);
		worldRenderer.addDrawShape("myPlane2", plane2);
		worldRenderer.addDrawShape("myPlane", plane);
	}
	
	public void update(){
		if(index == 1){
			worldRenderer.drawObject("myPlane");
			worldRenderer.drawObject("myPlane3");
			
			worldRenderer.undrawObject("myPlane2");
			worldRenderer.undrawObject("myPlane4");
		}else{
			worldRenderer.drawObject("myPlane2");
			worldRenderer.drawObject("myPlane4");
			
			worldRenderer.undrawObject("myPlane");
			worldRenderer.undrawObject("myPlane3");
		}
//		
//		index++;
//		
//		if(index > 20)
//			index = 0;
		
//		worldRenderer.drawObject("myPlane4");
	}

	public void passTouchEvents(MotionEvent e){
		if(e.getAction() == MotionEvent.ACTION_DOWN)
			if(index == 0){
				index = 1;
			} else {
				index = 0;
			}
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		//TODO fix up later - find method for manual renders
	    int TICKS_PER_SECOND = 30;
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
