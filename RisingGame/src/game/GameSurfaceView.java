package game;

import engine.open2d.renderer.WorldRenderer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.support.v4.view.MotionEventCompat;
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
		worldRenderer.setTrackFPS(true);
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
		
		//DoMultiTouchStuff(e);
		
		return true;
	}
	
	public void DoMultiTouchStuff(MotionEvent e){
		//multi touch changes
		int action = MotionEventCompat.getActionMasked(e);
		// Get the index of the pointer associated with the action.
		int index = MotionEventCompat.getActionIndex(e);
		int xPos = -1;
		int yPos = -1;

		//Log.d("Multitouch","The action is " + actionToString(action));

		if (e.getPointerCount() > 1) {
		    // The coordinates of the current screen contact, relative to
		    // the responding View or Activity.
			int size = MotionEventCompat.getPointerCount(e);
			for(int i = 0; i < size; ++i){
				xPos = (int)MotionEventCompat.getX(e, i);
				yPos = (int)MotionEventCompat.getY(e, i);
		    
				Log.d("Multitouch","Multi touch event: index: "+i+", size: "+MotionEventCompat.getPointerCount(e)+", xPos: "+xPos+", yPos: "+yPos);
			}
		} else {
		    // Single touch event
		    //Log.d("Multitouch","Single touch event");
		    xPos = (int)MotionEventCompat.getX(e, index);
		    yPos = (int)MotionEventCompat.getY(e, index);
		    
		    Log.d("Multitouch","Single touch event: index "+index+", xPos: "+xPos+", yPos "+yPos);
		}
	}
	
	// Given an action int, returns a string description
	public static String actionToString(int action) {
	    switch (action) {

	        case MotionEvent.ACTION_DOWN: return "Down";
	        case MotionEvent.ACTION_MOVE: return "Move";
	        case MotionEvent.ACTION_POINTER_DOWN: return "Pointer Down";
	        case MotionEvent.ACTION_UP: return "Up";
	        case MotionEvent.ACTION_POINTER_UP: return "Pointer Up";
	        case MotionEvent.ACTION_OUTSIDE: return "Outside";
	        case MotionEvent.ACTION_CANCEL: return "Cancel";
	    }
	    return "";
	}
	
    public WorldRenderer getWorldRenderer() {
		return worldRenderer;
	}

	public void setWorldRenderer(WorldRenderer worldRenderer) {
		this.worldRenderer = worldRenderer;
	}
}