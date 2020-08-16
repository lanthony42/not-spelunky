package com.gdx.main.entities.hazard;

import java.util.Random;

import com.gdx.main.Game;
import com.gdx.main.entities.Projectile;
import com.gdx.main.graphics.Animator;
import com.gdx.main.physics.Vector2D;

public class ShootEnemy extends Hazard {
	public static final float SPEED = 1f;
	public static final float KNOCK = 1.5f;
	public static final int RANGE = 150;
	public static final int DELAY = 75;
	public static final int VAR = 20;
	
	private int timer;
	private double dir;
	private boolean isSmart;
	private Animator smartDef;
	private Random rand;

	public ShootEnemy(boolean active, int x, int y, int width, int height, int damage, boolean smart) {
		super(active, x, y, 25, damage, "hazard/shooter", width, height, 2, 15);

		smartDef = new Animator("hazard/shooter/smart", 2, 15, true, true);
		rand = new Random();
		dir = rand.nextBoolean() ? Math.PI : 0;
		isSmart = smart;
	}

	public void update() {
		super.update();
		
		if(!isDead) {			
			if(isSmart) {
				Vector2D diff = new Vector2D(Game.getPlayer().getPositionX(), Game.getPlayer().getPositionY()).sub(position);
				dir = Math.atan2(diff.y, diff.x);
				animation = smartDef;
			}
			
			if(timer < DELAY + rand.nextInt(VAR)) {
				++timer;
			}
			else {				
				new Projectile(true, (int)(getPositionX() + getWidth() / 2), (int)(getPositionY() + getHeight() / 2), damage, SPEED, (float)dir, RANGE, 0, false, "hazard/shooter/proj", 4, 10, true, true);
				move(new Vector2D(KNOCK, (float)(dir + Math.PI), true));
				timer = 0;
			}
		}
	}
	
	public void dispose() {
		super.dispose();
		
		smartDef.dispose();
	}
}
