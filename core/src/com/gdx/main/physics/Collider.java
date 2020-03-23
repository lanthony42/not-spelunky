package com.gdx.main.physics;

import com.gdx.main.Component;

public abstract class Collider extends Component{
	protected Vector2D oldPosition;
	
	public Collider(int x, int y, boolean active) {
		super(x, y, active);
		oldPosition = new Vector2D();
	}
	
	public void update() {
		oldPosition.x = getPositionX();
		oldPosition.y = getPositionY();
	}
	
	public abstract boolean collidePoint(ColliderPoint other);
	
	public abstract boolean collideAABB(ColliderAABB other);
	
	public abstract Vector2D[] getPoints();
	
	public static float clamp(float value, float min, float max) {
		return Math.max(min, Math.min(max, value));
	}
	
	public static boolean inRange(double value, double min, double max) {
		return value >= min && value <= max;
	}
}
