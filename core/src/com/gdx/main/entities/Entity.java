package com.gdx.main.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.main.Game;
import com.gdx.main.graphics.Animator;
import com.gdx.main.physics.*;

public abstract class Entity {
	protected int uid;
	protected boolean active;
	protected Animator animation, defaultAnimation;
	protected Collider collision;
	protected Vector2D position;
	
	public Entity(boolean active, int x, int y, int width, int height, String file, int count, int delay, boolean playing, boolean loop, Collider collision) {
		this.uid = Game.getTotalEnt();
		this.active = active;
		
		position = new Vector2D(x, y);
		defaultAnimation = new Animator(file, count, delay, playing, loop);
		animation = defaultAnimation;
		this.collision = collision;
		this.collision.setParent(this);
		
		Game.getEntities().add(this);
		Game.increaseEnt();
	}
	
	public int getUID() {
		return uid;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public float getPositionX() {
		return position.x;
	}
	
	public float getPositionY() {
		return position.y;
	}
	
	public int getWidth() {
		return collision.getWidth();
	}
	
	public int getHeight() {
		return collision.getHeight();
	}
	
	public void collisionResponse(Vector2D penetration) {
		position = position.add(penetration);
	}
	
	public Collider getCollider() {
		return collision;
	}
	
	public void update() {
		animation.update();
		collision.update();
	}
	
	public void postUpdate() {		
		active = false;		
	}
	
	public boolean collide(Entity e) {
		Collider other = e.getCollider();
		
		if(other.getActive()) {
			if(other instanceof ColliderAABB) return collision.collideAABB((ColliderAABB)other);
			else if (other instanceof ColliderPoint) return collision.collidePoint((ColliderPoint)other);
		}
		
		return false;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(animation.getSprite(), getPositionX(), getPositionY(), getWidth(), getHeight());
	}
	
	// Dispose of textures, override in child classes
	public void dispose() {
		defaultAnimation.dispose();
	}
	
	public void destroy() {
		dispose();
		Game.getEntities().remove(this);
		active = false;
	}
}
