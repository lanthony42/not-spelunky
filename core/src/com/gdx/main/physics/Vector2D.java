package com.gdx.main.physics;

import java.lang.Math;
import com.badlogic.gdx.math.Vector2;

public class Vector2D {
	public float x, y;
	
	public Vector2D(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2D(float m, float d, boolean israd) {
		d = (float)(israd ? d : Math.toRadians(d));
		x = (float)(m * Math.cos(d));
		y = (float)(m * Math.sin(d));
	}
	
	public Vector2D() {
		x = y = 0;
	}

	public float magnitude() {
		return (float)Math.sqrt(x * x + y * y);
	}
	
	public float direction() {
		return (float)Math.atan2(y, x);
	}

	public Vector2D add(Vector2D v) {
		return new Vector2D(this.x + v.x, this.y + v.y);
	}

	public Vector2D sub(Vector2D v) {
		return new Vector2D(this.x - v.x, this.y - v.y);
	}
	
	public Vector2D mult(float v) {
		return new Vector2D(this.x * v, this.y * v);
	}
	
	public Vector2D div(float v) {
		return new Vector2D(this.x / v, this.y / v);
	}
	
	public Vector2D clamp(float mag) {
		mag = Math.min(magnitude(), mag);
		return new Vector2D(mag, direction(), true);
	}
	
	public Vector2D clampBounds(float width, float height, Vector2D bounds, boolean offset) {
		if(offset)
			return new Vector2D(Collider.clamp(this.x, width / 2, bounds.x - width / 2), Collider.clamp(this.y, height / 2, bounds.y - height / 2));
		return new Vector2D(Collider.clamp(this.x, 0, bounds.x - width), Collider.clamp(this.y, 0, bounds.y - height));
	}

	public Vector2D normalize() {
		Vector2D out = new Vector2D();
		float magnitude = magnitude();
		
		if (magnitude != 0) {
	        out.x = x / magnitude;
	        out.y = y / magnitude;
		}
	    return out;
	}

	public Vector2D lerpZero(float amount) {
		if(magnitude() - amount < 0) return new Vector2D();
		return new Vector2D(magnitude() - amount, direction(), true);
	}
	
	public float lerpZeroX(float amount) {
		if(Math.abs(x) - amount < 0) return 0;
		return Math.signum(x) * (Math.abs(x) - amount);
	}
	
	public double dotProduct (Vector2D v) {
		return x * v.x + y * v.y;
	}
	
	public Vector2D round() {
		return new Vector2D(Math.round(x), Math.round(y));
	}
	
	public boolean equals(Object o) {
		if(o instanceof Vector2D) return x == ((Vector2D)o).x && y == ((Vector2D)o).y;
		return false;
	}
	
	public Vector2 toVector2() {
		return new Vector2(x, y);
	}
	
	public Vector3D toVector3D(float z) {
		return new Vector3D(x, y, z);
	}
	
	public String toString() {
		return x + ", " + y;
	}
}
