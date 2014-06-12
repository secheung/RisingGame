package game;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
	float doubleTapX = 0;
	float doubleTapY = 0;
	
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    // event when double tap occurs
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        doubleTapX = e.getX();
        doubleTapY = e.getY();

        //Log.d("Double Tap", "Tapped at: (" + doubleTapX + "," + doubleTapY + ")");

        return true;
    }
    
    public float getDoubleTapX(){
    	return doubleTapX;
    }
    
    public float getDoubleTapY(){
    	return doubleTapY;
    }
}