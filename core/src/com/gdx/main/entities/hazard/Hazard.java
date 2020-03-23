package com.gdx.main.entities.hazard;

import com.gdx.main.entities.Object;
import com.gdx.main.graphics.Animator;

public class Hazard extends Object {
	private static final int DELAY = 30;
	protected int damage;
	private int timer;
	
	public Hazard(boolean active, int x, int y, int health, int damage, String file, int width, int height, int count, int delay) {
		super(active, x, y, file, width, height, count, delay, health, true, true, new Animator(file + "/dead", 1, 1, false, false));
		
		this.damage = damage;
		timer = 0;
	}
	
	public void update() {
		super.update();
			
		if(timer < DELAY)
			++timer;
	}

	public int getDamage() {
		if(timer >= DELAY) {
			timer = 0;
			return isDead ? 0 : damage;
		}
			
		return 0;
	}
}
