package game;

import engine.open2d.renderer.WorldRenderer;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GameSurfaceView extends GLSurfaceView{

	private static int TEXTURE_QUALITY = 1;
	
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private WorldRenderer worldRenderer;

    private GestureDetector gestureDetector;
    private GestureListener gestureListener;

	private GameLogic gameLogic;

    public GameSurfaceView(Context context){
		super(context);

		setEGLContextClientVersion(2);

		//WorldRenderer = WorldRenderer.getInstance();
		worldRenderer = new WorldRenderer(context);
		worldRenderer.setTextureQuality(TEXTURE_QUALITY);
		worldRenderer.setBackground(1.0f, 1.0f, 1.0f, 1.0f);
		worldRenderer.setTrackFPS(true);
		setRenderer(worldRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

		gestureListener = new GestureListener();
		gestureDetector = new GestureDetector(context,gestureListener);

		gameLogic = new GameLogic(context,worldRenderer);
		gameLogic.execute();
    }

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		//requestRender();
		worldRenderer.passTouchEvents(e);
		gameLogic.passTouchEvents(e);

		if(gestureDetector.onTouchEvent(e)){
			gameLogic.passDoubleTouchEvents(gestureListener);
		}

		return true;
	}
	
    public WorldRenderer getWorldRenderer() {
		return worldRenderer;
	}

	public void setWorldRenderer(WorldRenderer worldRenderer) {
		this.worldRenderer = worldRenderer;
	}
}