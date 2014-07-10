package game;

import android.graphics.RectF;
import android.util.Log;
import object.GameObject;
import structure.DataBox;

public class GameTools {
	public static enum Gesture {
		NONE,
		SWIPE_UP,
		SWIPE_UP_RIGHT,
		SWIPE_RIGHT,
		SWIPE_DOWN_RIGHT,
		SWIPE_DOWN,
		SWIPE_DOWN_LEFT,
		SWIPE_LEFT,
		SWIPE_UP_LEFT,
		TAP,
		TAP_UP,
		TAP_RIGHT,
		TAP_DOWN,
		TAP_LEFT,
		DTAP_UP,
		DTAP_RIGHT,
		DTAP_DOWN,
		DTAP_LEFT;
		
		float xTap = 0;
		float yTap = 0;
		float xDiffSize = 0;
		float yDiffSize = 0;
		
		public float getxTap() {
			return xTap;
		}

		public Gesture setxTap(float xTap) {
			this.xTap = xTap;
			return this;
		}

		public float getyTap() {
			return yTap;
		}

		public Gesture setyTap(float yTap) {
			this.yTap = yTap;
			return this;
		}

		public Gesture setXDiffSize(float diff){
			xDiffSize = diff;
			return this;
		}
		
		public float getXDiffSize(){
			return xDiffSize;
		}
		
		public Gesture setYDiffSize(float diff){
			yDiffSize = diff;
			return this;
		}
		
		public float getYDiffSize(){
			return yDiffSize;
		}
	}
	
	public static final int SWIPE_LEFT_DIFF = 30;
	public static final int SWIPE_RIGHT_DIFF = -30;
	public static final int SWIPE_UP_DIFF = 30;
	public static final int SWIPE_DOWN_DIFF = -30;
	
	public static boolean boxContains(RectF box, float x, float y){
		if(	x >= box.left && 
			x < box.right &&
			y >= box.bottom &&
			y < box.top){
			return true;
		}
		
		return false;
	}
	
	public static boolean boxColDetect(RectF box1, GameObject object1, RectF box2, GameObject object2){
		float left1 = object1.getX() + box1.left;
		float right1 = object1.getX() + box1.right;
		float top1 = object1.getY() + box1.top;
		float bottom1 = object1.getY()+box1.bottom;
		
		float left2 = object2.getX() + box2.left;
		float right2 = object2.getX() + box2.right;
		float top2 = object2.getY() + box2.top;
		float bottom2 = object2.getY() + box2.bottom;
		
		if (bottom1 > top2) return false;
		if (top1 < bottom2) return false;
		if (right1 < left2) return false;
		if (left1 > right2) return false;
		
		return true;
		
		/*
		box1.set(object1.getX() + box1.left, object1.getY() + box1.top, object1.getX() + box1.right, object1.getY() + box1.bottom);
		box2.set(object2.getX() + box2.left, object2.getY() + box2.top, object2.getX() + box2.right, object2.getY() + box2.bottom);
		
		return RectF.intersects(box1, box2);
		*/
	}
	
	public static boolean boxColDetect(GameObject object1, GameObject object2){
		float left1 = object1.getX();
		float right1 = object1.getX() + object1.getWidth();
		float top1 = object1.getY() + object1.getHeight();
		float bottom1 = object1.getY();
		
		float left2 = object2.getX();
		float right2 = object2.getX() + object2.getWidth();
		float top2 = object2.getY() + object2.getHeight();
		float bottom2 = object2.getY();
		
		if (bottom1 > top2) return false;
		if (top1 < bottom2) return false;
		if (right1 < left2) return false;
		if (left1 > right2) return false;
		
		return true;
	}
	
	public static boolean boxColDetect(GameObject object1, GameObject object2, float buffer){
		float left1 = object1.getX() + buffer;
		float right1 = object1.getX() + object1.getWidth() - buffer;
		float top1 = object1.getY() + object1.getHeight() - buffer;
		float bottom1 = object1.getY() + buffer;
		
		float left2 = object2.getX() + buffer;
		float right2 = object2.getX() + object2.getWidth() - buffer;
		float top2 = object2.getY() + object2.getHeight() - buffer;
		float bottom2 = object2.getY() + buffer;
		
		if (bottom1 > top2) return false;
		if (top1 < bottom2) return false;
		if (right1 < left2) return false;
		if (left1 > right2) return false;
		
		return true;
	}
	
	public static boolean boxColDetect(	GameObject object1, float topOffset1, float bottomOffset1, float leftOffset1, float rightOffset1,
										GameObject object2, float topOffset2, float bottomOffset2, float leftOffset2, float rightOffset2){
		float left1 = object1.getX() + leftOffset1;
		float right1 = object1.getX() + object2.getWidth() +  rightOffset1;
		float top1 = object1.getY() + object2.getHeight() + topOffset1;
		float bottom1 = object1.getY() + bottomOffset1;
		
		float left2 = object2.getX() + leftOffset2;
		float right2 = object2.getX() + object2.getWidth() + rightOffset2;
		float top2 = object2.getY() + object2.getHeight() + topOffset2;
		float bottom2 = object2.getY() + bottomOffset2;
		
		if (bottom1 > top2) return false;
		if (top1 < bottom2) return false;
		if (right1 < left2) return false;
		if (left1 > right2) return false;
		
		return true;
	}
	
	public static Gesture gestureDetection(float prevX,float curX, float prevY, float curY){
		Gesture horizontal = gestureDetectionHorizontal(prevX, curX);
		Gesture vertical = gestureDetectionVertical(prevY,curY);
		
		if(horizontal == Gesture.SWIPE_RIGHT && vertical == Gesture.SWIPE_UP)
			return Gesture.SWIPE_UP_RIGHT.setXDiffSize(horizontal.getXDiffSize()).setYDiffSize(vertical.getYDiffSize());
		else if(horizontal == Gesture.SWIPE_RIGHT && vertical == Gesture.SWIPE_DOWN)
			return Gesture.SWIPE_DOWN_RIGHT.setXDiffSize(horizontal.getXDiffSize()).setYDiffSize(vertical.getYDiffSize());
		else if(horizontal == Gesture.SWIPE_LEFT&& vertical == Gesture.SWIPE_UP)
			return Gesture.SWIPE_UP_LEFT.setXDiffSize(horizontal.getXDiffSize()).setYDiffSize(vertical.getYDiffSize());
		else if(horizontal == Gesture.SWIPE_LEFT&& vertical == Gesture.SWIPE_DOWN)
			return Gesture.SWIPE_DOWN_LEFT.setXDiffSize(horizontal.getXDiffSize()).setYDiffSize(vertical.getYDiffSize());
		
		if(horizontal == Gesture.NONE)
			return vertical;
		if(vertical == Gesture.NONE)
			return horizontal;
		
		return Gesture.NONE;
	}
	
	public static Gesture gestureDetectionHorizontal(float prevX, float curX){
		float xDiff = prevX - curX;
		if(xDiff > SWIPE_LEFT_DIFF){
			return Gesture.SWIPE_LEFT.setXDiffSize(xDiff);
		} else if(xDiff < SWIPE_RIGHT_DIFF){
			return Gesture.SWIPE_RIGHT.setXDiffSize(xDiff);
		}
		
		return Gesture.NONE;
	}
	
	public static Gesture gestureDetectionVertical(float prevY, float curY){
		float yDiff = prevY - curY;
		if(yDiff > SWIPE_UP_DIFF){
			return Gesture.SWIPE_UP.setYDiffSize(yDiff);
		} else if(yDiff < SWIPE_DOWN_DIFF){
			return Gesture.SWIPE_DOWN.setYDiffSize(yDiff);
		}
		
		return Gesture.NONE;
	}
	
	public static Gesture gestureBreakdownHorizontal(Gesture gesture){
		if(gesture == Gesture.SWIPE_RIGHT || gesture == Gesture.SWIPE_UP_RIGHT || gesture == Gesture.SWIPE_DOWN_RIGHT)
			return Gesture.SWIPE_RIGHT;
		else if(gesture == Gesture.SWIPE_LEFT || gesture == Gesture.SWIPE_UP_LEFT || gesture == Gesture.SWIPE_DOWN_LEFT)
			return Gesture.SWIPE_LEFT;

		return Gesture.NONE;
	}
	
	public static Gesture gestureBreakdownVertical(Gesture gesture){
		if(gesture == Gesture.SWIPE_UP || gesture == Gesture.SWIPE_UP_RIGHT || gesture == Gesture.SWIPE_UP_LEFT)
			return Gesture.SWIPE_UP;
		else if(gesture == Gesture.SWIPE_DOWN || gesture == Gesture.SWIPE_DOWN_LEFT || gesture == Gesture.SWIPE_DOWN_RIGHT)
			return Gesture.SWIPE_DOWN;

		return Gesture.NONE;
	}
}
