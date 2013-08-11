package game.open2d;

import object.GameObject;

public class GameTools {

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
}
