package com.gdx.main.entities;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gdx.main.Game;
import com.gdx.main.graphics.Animator;
import com.gdx.main.physics.Collider;
import com.gdx.main.physics.ColliderPlayer;
import com.gdx.main.physics.Vector2D;

public class Player extends Object {
	public static final int BASE_HEALTH = 100;
	public static final float MOVE_PWR = 1.0f;
	public static final float AIR_PWR = 0.05f;
	public static final float JUMP_PWR = 4.5f;
	public static final float JUMP_CHARGE = 0.05f;
	public static final float JUMP_MIN = 3.0f;
	
	// Attack constants
	public static final float RANGE = 125;
	public static final float PROJ_SPEED = 3f;
	public static final int DAMAGE = 5;
	public static final int BASE_DELAY = 20;
	
	public static final float UP_RANGE = 20;
	public static final float UP_PROJ_SPEED = 0.2f;
	public static final int UP_DAMAGE = 2;
	public static final float UP_BASE_DELAY = -0.5f;
	
	// State constants
	public static final int STATIC = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	
	public static final int FLOOR = 0;
	public static final int FALLING = 1;
	public static final int NIL =  2;
	
	public static final int NORM = 0;
	public static final int PROTECT = 1;
	public static final int ATK_UP = 2;
	public static final int ATK_DOWN = 3;
	public static final int ATK_LEFT = 4;
	public static final int ATK_RIGHT = 5;
	
	private boolean isJump, isDuck, isDamage, collideDown;
	private int movementStateX, movementStateY, actionState;
	private float oldY, olderY, accelAmount, jumpAmount;
	private Animator left, right, duck, jump, protect;
	
	private int damage, weaponLevel;
	private float range, projSpeed, actionDelay, actionTimer;
	
	public Player(boolean active, int x, int y) {
		super(active, x, y, "player/def", 16, 16, 2, 30, BASE_HEALTH, true, true, new Animator("player/dead", 1, 30, false, false), new ColliderPlayer());
		right = new Animator("player/right", 3, 5, true, true);
		left = new Animator("player/left", 3, 5, true, true);
		duck = new Animator("player/duck", 4, 20, true, false);
		jump = new Animator("player/jump", 3, 5, true, false);
		protect = new Animator("player/protect", 1, 30, false, false);

		movementStateX = RIGHT;
		movementStateY = FALLING;
		actionState = NORM;
		jumpAmount = JUMP_MIN;
		
		range = RANGE;
		projSpeed = PROJ_SPEED;
		damage = DAMAGE;
		actionDelay = BASE_DELAY;
		
		oldY = position.y;
		olderY = oldY;
		weaponLevel = 1;
		accelAmount = 0;
		actionTimer = 0;
		collideDown = false;
		isJump = false;
		isDuck = false;
	}
	
	public void setMoveStateX(int state) {
		movementStateX = state;
	}
	
	public void setMoveStateY(int state) {
		movementStateY = state;
	}
	
	public void setActState(int state) {
		actionState = state;
	}
	
	public void setJump(boolean jump) {
		isJump = jump;
	}
	
	public void setDuck(boolean duck) {
		isDuck = duck;
	}
	
	public boolean isFloor() {
		return movementStateY != FALLING;
	}

	public boolean isJump() {
		return isJump;
	}

	public void update() {
		olderY = oldY;
		oldY = position.y;
		super.update();
		resetSpeed();
		
		if(!isDead) {
			health = Math.min(BASE_HEALTH, health);
			
			// Determine air acceleration
			if(isFloor()) {
				accelAmount = MOVE_PWR;
			}
			else {
				accelAmount = AIR_PWR;
			}
		
			// Jumping and Ground Checks
			if(isJump && isFloor()) {
				velocity.y = jumpAmount;
				movementStateY = FALLING; 
			}
			// Charges jumps with ducking
			else {
				if(isDuck && isFloor()) {
					jumpAmount = Math.min(JUMP_PWR, jumpAmount + JUMP_CHARGE);
					maxSpeed = MAX_SPEED / 3;
				}
				else {
					jumpAmount = Math.max(JUMP_MIN, jumpAmount - JUMP_CHARGE);
				}
		
				if((position.y == oldY && oldY == olderY) || collideDown) {
					movementStateY = movementStateY == FALLING ? FLOOR : movementStateY;
				}
				else if(!isDuck){
					movementStateY = FALLING;
				}
			}
				
			// Main movements
			if(movementStateX == LEFT) {
				move(-accelAmount);
				animation = left;
			} 
			else if (movementStateX == RIGHT) {
				move(accelAmount);
				animation = right;
			}
		
			// Main actions
			if(actionState == PROTECT) {
				maxSpeed = MAX_SPEED / 5;
				velocity.y = Collider.clamp(velocity.y, -MAX_FALL, 0);
			} 
			else if (actionState == ATK_UP) {
				shoot((float)Math.PI / 2);
			}
			else if (actionState == ATK_DOWN) {
				shoot(-(float)Math.PI / 2);
			}
			else if (actionState == ATK_LEFT) {
				shoot((float)Math.PI);
			}
			else if (actionState == ATK_RIGHT) {
				shoot(0);
			}
			
			if(actionTimer < actionDelay)
				++actionTimer;
		}
		
		collideDown = false;
		
		// Animations
		if(isDead)
			animation = dead;
		else if(actionState == PROTECT)
			animation = protect;
		else if(isJump && !isFloor()) {
			animation = jump;
			animation.restart();
		}
		else if(isDuck) {
			animation = duck;
			animation.restart();
		}
		else if(movementStateX == LEFT)
			animation = left;
		else if (movementStateX == RIGHT) 
			animation = right;
		else 
			animation = defaultAnimation;
		
		duck.reset();
		jump.reset();
		left.reset();
		right.reset();
		protect.reset();
		defaultAnimation.reset();
		dead.reset();
	}
	
	public boolean isWin() {
		return position.x > Game.getWorld().getTotalMapSize().x - 1;
	}
	
	public boolean isDamage() {
		return isDamage;
	}
	
	public void notDamage() {
		isDamage = false;
	}
	
	public void setPosition(float x, float y) {
		position = new Vector2D(x, y);
	}
	
	public void setDead(boolean dead) {
		isDead = dead;
	}
	
	public int getWeaponLevel() {
		return weaponLevel;
	}
	
	public void incWeaponLevel() {
		++weaponLevel;
	}
	
	public void shoot(float dir) {
		if(actionTimer >= actionDelay) {
			new Projectile(true, (int)(getPositionX() + getWidth() / 2), (int)(getPositionY() + getHeight() / 2), damage, projSpeed, dir, range, 0, true, "projectile", 4, 10, true, true);
			actionTimer = 0;
		}
	}
	
	public void upgrade() {
		int r = new Random().nextInt(4);
		if(r == 0)
			range += UP_RANGE;
		else if(r == 1)
			projSpeed += UP_PROJ_SPEED;
		else if (r == 2)
			damage += UP_DAMAGE;
		else
			actionDelay += UP_BASE_DELAY;
		
		weaponLevel += 5;
	}
	
	public void takeDamage(int damage) {
		if(!isDead) {
			health -= actionState == PROTECT ? damage / 4 : damage;
		}
		if(damage > 0) {
			isDamage = true;
		}
		
		isDead = health <= 0;
		health = Math.min(BASE_HEALTH, health);
	}
	
	public void clampPosition() {
		if(position.y + getHeight() < 0) {
			isDead = true;
		}
		else if (position.y  + getHeight() / 2.0f > Game.getWorld().getTotalMapSize().y) {
			velocity.y = Math.min(0, velocity.y);
			position.y = Game.getWorld().getTotalMapSize().y - getHeight() / 2.0f;
		}
		
		if (position.x < 0) {
			velocity.x = Math.max(0, velocity.x);
			position.x = 0;
		}
	}
	
	public void collisionResponse(Vector2D penetration) {
		super.collisionResponse(penetration);
		
		if(penetration.y > 0) {
			collideDown = true;
		}
	}
	
	public void draw(SpriteBatch batch) {
		if(!isJump())
			batch.draw(animation.getSprite(), Math.round(getPositionX()), Math.round(getPositionY()), 16, 16);
		else
			batch.draw(animation.getSprite(), Math.round(getPositionX()), getPositionY(), 16, 16);
	}
	
	public void dispose(){
		super.dispose();
		
		left.dispose();
		right.dispose();
		duck.dispose();
		jump.dispose();
		protect.dispose();
	}
}
