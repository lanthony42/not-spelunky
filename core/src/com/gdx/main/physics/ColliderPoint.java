package com.gdx.main.physics;

public class ColliderPoint extends Collider {

	public ColliderPoint(boolean active) {
		super(0, 0, active);
	}
	
	@Override
	public boolean collidePoint(ColliderPoint other) {
		return getPositionX() == other.getPositionX() && getPositionY() == other.getPositionY();
	}

	@Override
	public boolean collideAABB(ColliderAABB other) {
		return Collider.inRange(getPositionX(), other.getPositionX(), other.getPositionX() + other.getWidth()) &&
				Collider.inRange(getPositionY(), other.getPositionY(), other.getPositionY() + other.getHeight());
	}
	
	@Override
	public Vector2D[] getPoints() {
		Vector2D[] out = new Vector2D[1];
		out[0] = new Vector2D(getPositionX(), getPositionY());
		
		return out;
	}
}
