package com.gdx.main.entities.tile;

import com.gdx.main.Game;
import com.gdx.main.physics.ColliderRect;

public class PlatformTile extends Tile {
	
	public PlatformTile(boolean active, int x, int y, String file) {
		super(active, x, y, file, Game.TILE_WIDTH, Game.TILE_HEIGHT, VARIATION, 10, false, false, new ColliderRect(Game.TILE_WIDTH, Game.TILE_HEIGHT, true));

		
	}

}
