package com.gdx.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class Input {
	public static final int KEY_W = 0;
	public static final int KEY_A = 1;
	public static final int KEY_S = 2;
	public static final int KEY_D = 3;
	
	public static final int KEY_SPACE = 4;
	public static final int KEY_UP = 5;
	public static final int KEY_DOWN = 6;
	public static final int KEY_LEFT = 7;
	public static final int KEY_RIGHT = 8;
	
	public static final int KEY_R = 9;
	public static final int KEY_P = 10;
	public static final int KEY_ESC = 11;
	public static final int KEY_ENTER = 12;
	
	public static final int KEY_COUNT = 13;
	
	private boolean[] inputs, old;
	
	public Input() {
		inputs = new boolean[KEY_COUNT];
		old = inputs.clone();
	}
	
	public void update() {
		old = inputs.clone();
		
		inputs[KEY_W] = Gdx.input.isKeyPressed(Keys.W);
		inputs[KEY_A] = Gdx.input.isKeyPressed(Keys.A);
		inputs[KEY_S] = Gdx.input.isKeyPressed(Keys.S);
		inputs[KEY_D] = Gdx.input.isKeyPressed(Keys.D);
		
		inputs[KEY_SPACE] = Gdx.input.isKeyPressed(Keys.SPACE);
		inputs[KEY_UP] = Gdx.input.isKeyPressed(Keys.UP);
		inputs[KEY_DOWN] = Gdx.input.isKeyPressed(Keys.DOWN);
		inputs[KEY_LEFT] = Gdx.input.isKeyPressed(Keys.LEFT);
		inputs[KEY_RIGHT] = Gdx.input.isKeyPressed(Keys.RIGHT);
		
		inputs[KEY_R] = Gdx.input.isKeyPressed(Keys.R);
		inputs[KEY_P] = Gdx.input.isKeyPressed(Keys.P);
		inputs[KEY_ESC] = Gdx.input.isKeyPressed(Keys.ESCAPE);
		
		inputs[KEY_ENTER] = Gdx.input.isKeyPressed(Keys.ENTER);
	}
	
	public boolean isPressed(int key) {
		return inputs[key];
	}
	
	public boolean isNewPressed(int key) {
		return inputs[key] && !old[key];
	}
}
