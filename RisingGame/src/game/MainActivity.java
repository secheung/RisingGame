package game;

import object.Player;
import engine.open2d.draw.Plane;
import engine.open2d.renderer.WorldRenderer;
import android.app.Activity;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.GestureDetector.OnDoubleTapListener;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private GameSurfaceView surfaceView;
    public TextView textBox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		surfaceView = new GameSurfaceView(this);
		setContentView(surfaceView);
		
		final WorldRenderer render = surfaceView.getWorldRenderer();
		
		textBox = new TextView(surfaceView.getContext());
		textBox.setText(""+render.getFPS());
		
		addContentView(textBox, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
    }
}