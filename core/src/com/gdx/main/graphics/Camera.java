package com.gdx.main.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;

import com.gdx.main.Game;
import com.gdx.main.Component;
import com.gdx.main.entities.Entity;
import com.gdx.main.physics.*;

public class Camera extends Component {
	private static final int LENIENCY_X = 50;
	private static final int LENIENCY_Y = 30;
	
	private float scale;
	private Vector2D position;
	private OrthographicCamera camera;	
	
	public Camera(float s, boolean active) {
		super(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT, active);
		position = new Vector2D();
		scale = s;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false);
		camera.position.set(position.toVector3D(1).toVector3());
		camera.zoom = scale;
		camera.update();
	}
	
	public void update() {
		float newX = parent.getPositionX() + parent.getWidth() / 2.0f;
		float newY = parent.getPositionY() + parent.getHeight() / 2.0f;
		
		if(Math.abs(newX - position.x) > LENIENCY_X)
			position.x = newX > position.x ? newX - LENIENCY_X : newX + LENIENCY_X;
		if(Math.abs(newY - position.y) > LENIENCY_Y)
			position.y = newY > position.y ? newY - LENIENCY_Y : newY + LENIENCY_Y;
				
		position = position.clampBounds(getWidth(), getHeight(), Game.getWorld().getTotalMapSize(), true);

		camera.position.set(position.toVector3D(1).toVector3());
		camera.zoom = scale;
		camera.update();
	}
	
	public void setParent(Entity parent) {
		super.setParent(parent);
		update();
	}
	
	public int getPositionX() {
		if(parent != null) {
			return (int)position.round().x;
			}
		return 0;
	}
	
	public int getPositionY() {
		if(parent != null) {
			return (int)position.round().y;
		}
		return 0;
	}
	
	public int getWidth() {
		return (int)(size.x * scale);
	}
	
	public int getHeight() {
		return (int)(size.y * scale);
	}
	
	public void setScale(float s) {
		scale = s;
	}
	
	public float getScale() {
		return parent != null ? scale : 1;
	}
	
	public Vector2D[] getPoints() {
		Vector2D[] out = new Vector2D[9]; 
		
		out[0] = new Vector2D(camera.position.x - getWidth() / 2.0f, camera.position.y - getHeight() / 2.0f);
		out[1] = new Vector2D(camera.position.x - getWidth() / 2.0f, camera.position.y + getHeight() / 2.0f);
		out[2] = new Vector2D(camera.position.x + getWidth() / 2.0f, camera.position.y - getHeight() / 2.0f);
		out[3] = new Vector2D(camera.position.x + getWidth() / 2.0f, camera.position.y + getHeight() / 2.0f);
		out[4] = new Vector2D(camera.position.x, camera.position.y - getHeight() / 2.0f);
		out[5] = new Vector2D(camera.position.x, camera.position.y + getHeight() / 2.0f);
		out[6] = new Vector2D(camera.position.x - getWidth() / 2.0f, camera.position.y);
		out[7] = new Vector2D(camera.position.x + getWidth() / 2.0f, camera.position.y);
		out[8] = new Vector2D(camera.position.x, camera.position.y);
		
		return out;
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
}
