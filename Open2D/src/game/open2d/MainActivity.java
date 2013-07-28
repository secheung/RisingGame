package game.open2d;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private GameSurfaceView surfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		surfaceView = new GameSurfaceView(this);
		setContentView(surfaceView);
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