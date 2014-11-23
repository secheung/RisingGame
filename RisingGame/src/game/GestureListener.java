package game;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
	float doubleTapX = 0;
	float doubleTapY = 0;
	boolean dTapped = false;
	
	float longPressX = 0;
	float longPressY = 0;
	boolean longPress = false;
	
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
    
    @Override
    public void onLongPress(MotionEvent e){
    	longPressX = e.getX();
        longPressY = e.getY();
    	longPress = true;
    }
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
    	//Log.d("rising_debug", "flung at: (" + velocityX + "," + velocityY + ")");
    	/*
    	if(e1.getAction() == MotionEvent.ACTION_DOWN && e2.getAction() == MotionEvent.ACTION_UP){
    		return true;
    	} else {
    		return false;
    	}
    	*/
    	return false;
    }
    
    // event when double tap occurs
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        doubleTapX = e.getX();
        doubleTapY = e.getY();
        dTapped = true;

        //Log.d("rising_debug", "Tapped at: (" + doubleTapX + "," + doubleTapY + ")");
        //Log.d("rising_debug",e.toString());
        return true;
    }
    
    public boolean isdTapped() {
		return dTapped;
	}

	public void setdTapped(boolean dTapped) {
		this.dTapped = dTapped;
	}

	public float getDoubleTapX(){
    	return doubleTapX;
    }
    
    public float getDoubleTapY(){
    	return doubleTapY;
    }

	public boolean isLongPress() {
		return longPress;
	}

	public void setLongPress(boolean longPress) {
		this.longPress = longPress;
	}
	public float getLongPressX() {
		return longPressX;
	}

	public float getLongPressY() {
		return longPressY;
	}
}