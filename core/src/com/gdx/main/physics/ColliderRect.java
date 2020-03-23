package com.gdx.main.physics;

// A stationary rectangle
public class ColliderRect extends ColliderAABB {

	public ColliderRect(int x, int y, boolean active) {
		super(x, y, active);
	}
	
	@Override
	public boolean collideAABB(ColliderAABB other) {
		if(other instanceof ColliderRect) return false;
		
		Vector2D[] md = minDifference(other);
		Vector2D penetration = new Vector2D(md[0].x, 0);
		double minDistance = Math.abs(md[0].x);
		boolean collided = md[0].x <= 0 &&
			    			md[1].x >= 0 &&
			    			md[0].y <= 0 &&
			    			md[1].y >= 0;
		
		if(collided) {
			if(Math.abs(md[1].x) < minDistance) {
				minDistance = Math.abs(md[1].x);
		        penetration = new Vector2D(md[1].x, 0);
		    }
		    if(Math.abs(md[1].y) < minDistance) {
		    	minDistance = Math.abs(md[1].y);
		        penetration = new Vector2D(0, md[1].y);
		    }
		    if(Math.abs(md[0].y) < minDistance) {
		    	minDistance = Math.abs(md[0].y);
		        penetration = new Vector2D(0, md[0].y);
		    }

		    if(penetration.magnitude() != 0) {
		    	other.getParent().collisionResponse(penetration);
		    }
		}
		return collided;
	}
}
