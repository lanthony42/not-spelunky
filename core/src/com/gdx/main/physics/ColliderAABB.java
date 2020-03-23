package com.gdx.main.physics;

// Any Axis-Aligned Bounding Box
public class ColliderAABB extends Collider {

	public ColliderAABB(int x, int y, boolean active) {
		super(x, y, active);
	}
	
	@Override
	public boolean collidePoint(ColliderPoint other) {
		return other.collideAABB(this);
	}

	@Override
	public boolean collideAABB(ColliderAABB other) {
		if(other instanceof ColliderRect) return ((ColliderRect)other).collideAABB(this);
		Vector2D[] md = minDifference(other);
		return md[0].x <= 0 &&
			    md[1].x >= 0 &&
			    md[0].y <= 0 &&
			    md[1].y >= 0;
	}
	
	@Override
	public Vector2D[] getPoints() {
		Vector2D[] out = new Vector2D[4];
		
		out[0] = new Vector2D(getPositionX(), getPositionY());
		out[1] = out[0].add(new Vector2D(getWidth(), 0));
		out[2] = out[0].add(new Vector2D(0, getHeight()));
		out[3] = out[0].add(new Vector2D(getWidth(), getHeight()));
		
		return out;
	}
	
	public Vector2D[] minDifference(ColliderAABB other){
		Vector2D[] md = new Vector2D[2];
		md[0] = new Vector2D(getPositionX(), getPositionY()).sub(new Vector2D(other.getPositionX() + other.getWidth(), other.getPositionY() + other.getHeight()));
		md[1] = new Vector2D(getWidth(), getHeight()).add(new Vector2D(other.getWidth(), other.getHeight()));
		md[1] = md[0].add(md[1]);
		
		return md;
	}
}
