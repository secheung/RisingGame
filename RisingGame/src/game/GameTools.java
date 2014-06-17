package game;

import android.graphics.RectF;
import android.util.Log;
import object.GameObject;

public class GameTools {
	public static enum Gesture {
		NONE,
		UP,
		UP_RIGHT,
		RIGHT,
		DOWN_RIGHT,
		DOWN,
		DOWN_LEFT,
		LEFT,
		UP_LEFT;
		
		float xDiffSize = 0;
		float yDiffSize = 0;
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
	
	public static final int SWIPE_LEFT = 30;
	public static final int SWIPE_RIGHT = -30;
	public static final int SWIPE_UP = 30;
	public static final int SWIPE_DOWN = -30;
	
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
		
		if(horizontal == Gesture.RIGHT && vertical == Gesture.UP)
			return Gesture.UP_RIGHT.setXDiffSize(horizontal.getXDiffSize()).setYDiffSize(vertical.getYDiffSize());
		else if(horizontal == Gesture.RIGHT && vertical == Gesture.DOWN)
			return Gesture.DOWN_RIGHT.setXDiffSize(horizontal.getXDiffSize()).setYDiffSize(vertical.getYDiffSize());
		else if(horizontal == Gesture.LEFT&& vertical == Gesture.UP)
			return Gesture.UP_LEFT.setXDiffSize(horizontal.getXDiffSize()).setYDiffSize(vertical.getYDiffSize());
		else if(horizontal == Gesture.LEFT&& vertical == Gesture.DOWN)
			return Gesture.DOWN_LEFT.setXDiffSize(horizontal.getXDiffSize()).setYDiffSize(vertical.getYDiffSize());
		
		if(horizontal == Gesture.NONE)
			return vertical;
		if(vertical == Gesture.NONE)
			return horizontal;
		
		return Gesture.NONE;
	}
	
	public static Gesture gestureDetectionHorizontal(float prevX, float curX){
		float xDiff = prevX - curX;
		if(xDiff > SWIPE_LEFT){
			return Gesture.LEFT.setXDiffSize(xDiff);
		} else if(xDiff < SWIPE_RIGHT){
			return Gesture.RIGHT.setXDiffSize(xDiff);
		}
		
		return Gesture.NONE;
	}
	
	public static Gesture gestureDetectionVertical(float prevY, float curY){
		float yDiff = prevY - curY;
		if(yDiff > SWIPE_UP){
			return Gesture.UP.setYDiffSize(yDiff);
		} else if(yDiff < SWIPE_DOWN){
			return Gesture.DOWN.setYDiffSize(yDiff);
		}
		
		return Gesture.NONE;
	}
	
	public static Gesture gestureBreakdownHorizontal(Gesture gesture){
		if(gesture == Gesture.RIGHT || gesture == Gesture.UP_RIGHT || gesture == Gesture.DOWN_RIGHT)
			return Gesture.RIGHT;
		else if(gesture == Gesture.LEFT || gesture == Gesture.UP_LEFT || gesture == Gesture.DOWN_LEFT)
			return Gesture.LEFT;

		return Gesture.NONE;
	}
	
	public static Gesture gestureBreakdownVertical(Gesture gesture){
		if(gesture == Gesture.UP || gesture == Gesture.UP_RIGHT || gesture == Gesture.UP_LEFT)
			return Gesture.UP;
		else if(gesture == Gesture.DOWN || gesture == Gesture.DOWN_LEFT || gesture == Gesture.DOWN_RIGHT)
			return Gesture.DOWN;

		return Gesture.NONE;
	}
}
