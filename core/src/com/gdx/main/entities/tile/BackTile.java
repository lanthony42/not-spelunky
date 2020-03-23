package com.gdx.main.entities.tile;

import com.gdx.main.Game;
import com.gdx.main.physics.ColliderAABB;

public class BackTile extends Tile {

	public BackTile(boolean active, int x, int y) {
		super(active, x, y, "tile/back", Game.TILE_WIDTH, Game.TILE_HEIGHT, VARIATION, 10, false, false, new ColliderAABB(Game.TILE_WIDTH, Game.TILE_HEIGHT, false));

	}

}
