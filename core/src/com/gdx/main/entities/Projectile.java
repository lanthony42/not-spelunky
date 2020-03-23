package com.gdx.main.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.main.physics.*;

public class Projectile extends Entity {
	private static final int SIZE_W = 8;
	private static final int SIZE_H = 8;
	
	private static final float KNOCKBACK = 1.0f;
	
	private boolean isPlayer;
	private float distance, range, gravity;
	private int damage;
	private Vector2D velocity;
	
	public Projectile(boolean active, int x, int y, int damage, float speed, float direction, float range, float gravity, boolean player, String file, int count, int delay, boolean playing, boolean loop) {
		super(active, x, y, 0, 0, file, count, delay, playing, loop, new ColliderPoint(active));
		
		velocity = new Vector2D(speed, direction, true);
		this.damage = damage;
		this.gravity = gravity;
		this.range = range;
		isPlayer = player;
		distance = 0;
	}

	public void update() {
		super.update();
		
		velocity.y += gravity;
		position = position.add(velocity);
		distance += velocity.magnitude();
		
		if(distance >= range)
			destroy();
	}
	
	public void postUpdate() {		
		
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(animation.getSprite(), getPositionX() - SIZE_W / 2, getPositionY() - SIZE_H / 2, SIZE_W, SIZE_H);
	}
	
	public boolean isPlayer() {
		return isPlayer;
	}
	
	public int getDamage() {
		return damage;
	}
	
	public Vector2D getKnockback() {
		return new Vector2D(KNOCKBACK, velocity.direction(), true);
	}
	
	public void dispose(){
		super.dispose();
	}
	
	public void destroy() {
		super.destroy();
	}
}
