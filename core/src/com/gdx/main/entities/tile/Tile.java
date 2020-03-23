package com.gdx.main.entities.tile;

import java.util.Random;

import com.gdx.main.entities.Entity;
import com.gdx.main.physics.ColliderAABB;

public abstract class Tile extends Entity {
	protected static final int VARIATION = 5;
	
	public Tile(boolean active, int x, int y, String file, int width, int height, int count, int delay, boolean playing, boolean loop, ColliderAABB collision) {
		super(active, x, y, width, height, file, count, delay, playing, loop, collision);

		animation.setFrame(new Random().nextInt(VARIATION));
	}
	
}
