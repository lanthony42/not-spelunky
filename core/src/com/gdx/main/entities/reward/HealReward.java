package com.gdx.main.entities.reward;

public class HealReward extends Reward {
	private int healAmount;
	
	public HealReward(boolean active, int x, int y, int heal) {
		super(active, x, y, 15, "reward/heal", 16, 16, 2);
		
		healAmount = heal;
	}

	public int getHeal() {
		return healAmount;
	}
}
