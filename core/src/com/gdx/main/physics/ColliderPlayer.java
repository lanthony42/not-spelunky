package com.gdx.main.physics;

public class ColliderPlayer extends ColliderAABB {

	public ColliderPlayer() {
		super(14, 14, true);
	}

	public void update() {
		oldPosition.x = parent.getPositionX() + 1;
		oldPosition.y = parent.getPositionY();
	}
	
	public int getPositionX() {
		return Math.round(parent.getPositionX()) + 1;
	}
}
