package game;

import engine.open2d.renderer.WorldRenderer;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
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
		/*
		//REALLY BAD UI CODE
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					while (!isInterrupted()) {
						Thread.sleep(10);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								textBox.setText(((Integer)render.getFPS()).toString());
							}
						});
					}
				} catch (InterruptedException e) {
				}
			}
		};

		t.start();
		*/
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