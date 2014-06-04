package game;

import engine.open2d.renderer.WorldRenderer;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.EditText;

public class GameSurfaceView extends GLSurfaceView{

	private static int TEXTURE_QUALITY = 1;
	
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private WorldRenderer worldRenderer;
    private GameLogic gameLogic;
    
    public EditText editBox;

    public GameSurfaceView(Context context){
		super(context);

		setEGLContextClientVersion(2);

		//WorldRenderer = WorldRenderer.getInstance();
		worldRenderer = new WorldRenderer(context);
		worldRenderer.setTextureQuality(TEXTURE_QUALITY);
		worldRenderer.setBackground(1.0f, 1.0f, 1.0f, 1.0f);
		setRenderer(worldRenderer);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		
		gameLogic = new GameLogic(context,worldRenderer);
		gameLogic.execute();
		
		editBox = new EditText(context);
		editBox.setText("Hello Matron");
    }

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		//requestRender();
		worldRenderer.passTouchEvents(e);
		gameLogic.passTouchEvents(e);

		return true;
	}
}