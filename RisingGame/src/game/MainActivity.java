package game;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;

public class MainActivity extends Activity {

	private GameSurfaceView surfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		surfaceView = new GameSurfaceView(this);
		setContentView(surfaceView);
		
		addContentView(surfaceView.editBox, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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