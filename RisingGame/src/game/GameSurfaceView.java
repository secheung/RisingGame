package game;

import engine.open2d.renderer.WorldRenderer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

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

		worldRenderer = new WorldRenderer(context);
		worldRenderer.setTextureQuality(TEXTURE_QUALITY);
		worldRenderer.setBackground(0.5f, 0.5f, 0.5f, 1.0f);
		worldRenderer.setTrackFPS(false);
		setRenderer(worldRenderer);
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		worldRenderer.setCamera(GameLogic.CAM_X_DEFAULT, GameLogic.CAM_Y_DEFAULT, GameLogic.CAM_Z_DEFAULT);//init view matrix
		worldRenderer.setupFrustrum(width,height);//init projection matrix
		
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
		gameLogic.passTouchEvents(e,gestureListener);

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