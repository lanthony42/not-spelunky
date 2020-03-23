package com.gdx.main.entities.hazard;

import java.util.Random;

import com.gdx.main.Game;
import com.gdx.main.graphics.Animator;
import com.gdx.main.physics.Vector2D;

public class JumpEnemy extends Hazard {
	public static final float SPEED = 1f;
	public static final float JUMP_PWR = 2.8f;
	public static final int DELAY = 80;
	public static final int VAR = 40;
	
	private int dir, timer;
	private Animator right, jump;
	private boolean isJump, isSmart;
	private Random rand;

	public JumpEnemy(boolean active, int x, int y, int width, int height, int damage, boolean smart) {
		super(active, x, y, 20, damage, "hazard/jumper/left", width, height, 2, 10);
		rand = new Random();
		
		right = new Animator("hazard/jumper/right", 2, 10, true, true);
		jump = new Animator(smart ? "hazard/jumper/jump_smart" : "hazard/jumper/jump", 1, 10, false, false);
		dir = rand.nextBoolean() ? 1 : -1;
		isSmart = smart;
	}

	public void update() {
		super.update();
		
		if(!isDead && Math.abs(Game.getPlayer().getPositionY() - this.getPositionY()) < Game.TROOM_HEIGHT && Math.abs(Game.getPlayer().getPositionX() - this.getPositionX()) < Game.TROOM_WIDTH) {			
			if(velocity.y > 0) {
				animation = jump;
			}
			else if(dir == 1) {
				animation = right;
			}
			else if (dir == -1){
				animation = defaultAnimation;
			}

			if(isJump) {
				move(new Vector2D(0, JUMP_PWR));
				isJump = false;
			}
			else if(velocity.y > 0) {
				if(velocity.x == 0)
					if(!isSmart)
						dir = rand.nextBoolean() ? 1 : -1;
					else
						dir = (int)Math.signum(Game.getPlayer().getPositionX() - this.getPositionX());
				move(SPEED * dir);
			}
						
			if(timer < DELAY + rand.nextInt(VAR)) {
				++timer;
			}
			else {				
				isJump = true;
				timer = 0;
			}
		}
	}
	
	public void dispose() {
		super.dispose();
		
		right.dispose();
		jump.dispose();
	}
}
