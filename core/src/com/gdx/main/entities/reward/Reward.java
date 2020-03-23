package com.gdx.main.entities.reward;

import com.gdx.main.entities.Object;

public class Reward extends Object {
	private int reward;
	
	public Reward(boolean active, int x, int y, int reward, String file, int width, int height, int count) {
		super(active, x, y, file, width, height, count, 30, 100, true, true, null);

		this.reward = reward;
	}
	
	public int getReward() {
		return reward;
	}

	public void takeDamage(int damage) {
		
	}
}
