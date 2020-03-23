package com.gdx.main.entities;

import com.gdx.main.Game;
import com.gdx.main.graphics.Animator;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.main.physics.*;

public abstract class Object extends Entity {
	public static final float MAX_SPEED = 1.5f;
	public static final float DECEL = 0.2f;
	public static final float MAX_FALL = 10.0f;
	public static final Vector2D GRAVITY = new Vector2D(0, -0.1f);
	
	protected boolean isDead;
	protected int health, baseHealth;
	protected float maxSpeed, decel;
	protected Vector2D velocity, accel;
	protected Animator dead;
	
	public Object(boolean active, int x, int y, String file, int width, int height, int count, int delay, int basehealth, boolean playing, boolean loop, Animator dead) {
		super(active, x, y, width, height, file, count, delay, playing, loop, new ColliderAABB(width, height, active));
		this.dead = dead;
		baseHealth = basehealth;
		health = baseHealth;
		maxSpeed = MAX_SPEED;
		decel = DECEL;
		
		velocity = new Vector2D();
		accel = new Vector2D();
		isDead = false;
	}
	
	public Object(boolean active, int x, int y, String file, int width, int height, int count, int delay, int basehealth, boolean playing, boolean loop, Animator dead, Collider collider) {
		super(active, x, y, width, height, file, count, delay, playing, loop, collider);
		this.dead = dead;
		baseHealth = basehealth;
		health = baseHealth;
		maxSpeed = MAX_SPEED;
		decel = DECEL;
		
		velocity = new Vector2D();
		accel = new Vector2D();
		isDead = false;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getBaseHealth() {
		return baseHealth;
	}
	
	public Vector2D getVelocity() {
		return velocity;
	}
	
	public void resetSpeed() {
		maxSpeed = MAX_SPEED;
	}
	
	public void move(float movement) {
		accel.x += movement;
	}
	
	public void move(Vector2D movement) {
		velocity = velocity.add(movement);
	}
	
	public void takeDamage(int damage) {
		if(!isDead)
			health -= damage;
		
		isDead = health <= 0;
	}
	
	public void clampPosition() {
		if(position.y + getHeight() < 0) {
			isDead = true;
			destroy();
		}
		else if (position.y  + getHeight() / 2.0f > Game.getWorld().getTotalMapSize().y) {
			destroy();
		}
		
		if (position.x < 0) {
			velocity.x = Math.max(0, velocity.x);
			position.x = 0;
		}
		else if (position.x + getWidth() > Game.getWorld().getTotalMapSize().x) {
			velocity.x = Math.min(0, velocity.x);
			position.x = Game.getWorld().getTotalMapSize().x - getWidth();
		}
	}
	
	public void collisionResponse(Vector2D penetration) {
		super.collisionResponse(penetration);
		
		if(penetration.x > 0) {
			velocity.x = Math.max(velocity.x, 0);
		}
		else if(penetration.x < 0) {
			velocity.x = Math.min(velocity.x, 0);
		}
		
		if(penetration.y > 0) {
			velocity.y = Math.max(velocity.y, 0);
		}
		else if(penetration.y < 0) {
			velocity.y = Math.min(velocity.y, 0);
		}
	}
	
	// Main Methods
	public void update() {
		super.update();
		
		if(accel.x != 0) {
			velocity.x += accel.x;
		}
		else velocity.x = velocity.lerpZeroX(decel);
		
		velocity = velocity.add(GRAVITY);
		velocity.x = Collider.clamp(velocity.x, -maxSpeed, maxSpeed);
		velocity.y = Collider.clamp(velocity.y, -MAX_FALL, MAX_FALL + MAX_SPEED);
		position = position.add(velocity);
		accel = new Vector2D();

		if(isDead)
			animation = dead;
		clampPosition();
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(animation.getSprite(), Math.round(getPositionX()), Math.round(getPositionY()), getWidth(), getHeight());
	}
	
	public void postUpdate() {
		clampPosition();
	}
	
	public void dispose() {
		super.dispose();
		
		if(dead != null)
			dead.dispose();
	}
}
