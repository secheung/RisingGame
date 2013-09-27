package game;

import engine.open2d.renderer.WorldRenderer;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class GameSurfaceView extends GLSurfaceView{

	private static int TEXTURE_QUALITY = 1;
	
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private WorldRenderer worldRenderer;
    private GameLogic gameLogic;

    public GameSurfaceView(Context context){
		super(context);

		setEGLContextClientVersion(2);

		//WorldRenderer = WorldRenderer.getInstance();
		worldRenderer = new WorldRenderer(context);
		worldRenderer.setTextureQuality(TEXTURE_QUALITY);
		setRenderer(worldRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
		gameLogic = new GameLogic(context,worldRenderer);
		gameLogic.execute();
    }

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		//requestRender();
		worldRenderer.passTouchEvents(e);
		gameLogic.passTouchEvents(e);

		return true;
	}
}