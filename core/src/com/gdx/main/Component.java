package com.gdx.main;

import com.gdx.main.entities.Entity;
import com.gdx.main.physics.Vector2D;

public abstract class Component {
	protected boolean active;
	protected Entity parent;
	protected Vector2D size;
	
	public Component(int x, int y, boolean active) {
		parent = null;
		size = new Vector2D(x, y);
		this.active = active;
	}
	
	public void setParent(Entity parent) {
		this.parent = parent;
	}
	
	public Entity getParent() {
		return parent;
	}
		
	public int getPositionX() {
		return Math.round(parent.getPositionX());
	}
	
	public int getPositionY() {
		return Math.round(parent.getPositionY());
	}
	
	public int getWidth() {
		return (int)size.round().x;
	}
	
	public int getHeight() {
		return (int)size.round().y;
	}
	
	public boolean getActive() {
		return active;
	}
}
