package com.gdx.main.entities.hazard;

import com.gdx.main.Game;
import com.gdx.main.graphics.Animator;
import com.gdx.main.physics.Vector2D;

public class WalkEnemy extends Hazard {
	public static final float SPEED = 0.1f;
	public static final int DELAY = 30;
	
	private int dir, timer;
	private Animator right;

	public WalkEnemy(boolean active, int x, int y, int width, int height) {
		super(active, x, y, 15, 12, "hazard/walker/left", width, height, 2, 10);
		
		right = new Animator("hazard/walker/right", 2, 10, true, true);
		dir = 1;
	}

	public void update() {
		super.update();
		
		if(!isDead && Math.abs(Game.getPlayer().getPositionY() - this.getPositionY()) < Game.TROOM_HEIGHT && Math.abs(Game.getPlayer().getPositionX() - this.getPositionX()) < Game.TROOM_WIDTH) {
			move(SPEED * dir);
			maxSpeed = MAX_SPEED / 4;
			
			if(dir == 1) {
				animation = right;
			}
			else{
				animation = defaultAnimation;
			}
			
			if(timer < DELAY)
				++timer;
		}
	}
	
	public void collisionResponse(Vector2D penetration) {
		super.collisionResponse(penetration);
		
		if(timer >= DELAY) {
			if(penetration.x > 0) {
				dir = 1;				
				timer = 0;
			}
			else if(penetration.x < 0) {
				dir = -1;				
				timer = 0;
			}
			
			if(!isDead) {
				move(new Vector2D(0, 0.5f));
			}
		}
	}
	
	public void dispose() {
		super.dispose();
		
		right.dispose();
	}
}
