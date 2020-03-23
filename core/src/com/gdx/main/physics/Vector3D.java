package com.gdx.main.physics;

import com.badlogic.gdx.math.Vector3;

public class Vector3D extends Vector2D {
	public float z = 0.0f;
	
	public Vector3D(float x, float y, float z) {
		super(x, y);
		this.z = z;
	}
	
	public Vector3 toVector3() {
		return new Vector3(x, y, z);
	}
}
