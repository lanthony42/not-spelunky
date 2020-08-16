package com.gdx.main;

import java.util.*;
import com.gdx.main.entities.*;
import com.gdx.main.entities.tile.Tile;
import com.gdx.main.graphics.Camera;
import com.gdx.main.physics.Collider;
import com.gdx.main.physics.Vector2D;

public class World {
	public static final int[] ROOM_X = new int[] {3, 4, 4, 5, 5, 6, 6, 8, 8, 10, 10, 12, 12, 13, 14, 14, 14, 15, 16, 17, 18};
	public static final int[] ROOM_Y = new int[] {2, 2, 3, 3, 4, 4, 5, 5, 6,  6,  7,  7,  8,  8,  9, 10, 11, 12, 12, 12, 12};
	
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	public static final int[] DIR_X = new int[] {0, 1, 0, -1};
	public static final int[] DIR_Y = new int[] {-1, 0, 1, 0};
	
	
	private Room[][] world;
	private Vector2D size, totalMapSize, start, end;
	private int difficulty, pathCount;
	
	public World(int diff, ArrayList<Entity> e) {
		difficulty = diff;
		
		size = new Vector2D(ROOM_X[Math.max(0, (difficulty - 1) / 12)], ROOM_Y[Math.max(0, (difficulty - 1) / 12)]);
		totalMapSize = new Vector2D(size.x * Game.TROOM_WIDTH, size.y * Game.TROOM_HEIGHT);
		
		generateWorld(e);
	}
	
	// Generate world based on procgen algorithm
	public void generateWorld(ArrayList<Entity> e) {
		world = new Room[(int)size.x][(int)size.y];
		Random rand = new Random();
		int x = 0, y = rand.nextInt(world[0].length), dir = 0;
		pathCount = 1;
		
		start = new Vector2D(x, y);
		world[x][y] = new Room(x * Game.TROOM_WIDTH, y * Game.TROOM_HEIGHT, e);
		world[x][y].setOpening(LEFT, true);
		
		while(x != world.length - 1) {
			dir = rand.nextInt(3);
			x += DIR_X[dir];
			y += DIR_Y[dir];
			
			if(x >= world.length || x < 0 || y >= world[0].length || y < 0 || world[x][y] != null) {
				x -= DIR_X[dir];
				y -= DIR_Y[dir];
				continue;
			}
			world[x][y] = new Room(x * Game.TROOM_WIDTH, y * Game.TROOM_HEIGHT, e);
			world[x][y].setOpening((dir + 2) % 4, true);
			world[x - DIR_X[dir]][y - DIR_Y[dir]].setOpening(dir, true);
			++pathCount;
		}
		end = new Vector2D(x, y);
		world[x][y].setOpening(RIGHT, true);
		
		for(int i = 0; i < (int)size.x; ++i)
			for(int j = 0; j < (int)size.y; ++j) {
				if(world[i][j] == null)
					world[i][j] = new Room(i * Game.TROOM_WIDTH, j * Game.TROOM_HEIGHT, e);
				world[i][j].load();
			}
	}
	
	public Room getRoom(int x, int y) {
		return world[x][y];
	}
	
	public void setRoom(Room t, int x, int y) {
		world[x][y] = t;
	}
	
	public Vector2D getTotalMapSize() {
		return totalMapSize;
	}
	
	public Room getStart() {
		return world[(int)start.x][(int)start.y];
	}
	
	public Room getEnd() {
		return world[(int)end.x][(int)end.y];
	}
	
	public float getDifficulty() {
		int maxPath = ((int)size.x - 1) * (int)size.y + 1;
		return difficulty - difficulty / (maxPath - (int)size.x) * (pathCount - (int)size.x);
	}
	
	// Collisions
	public void clearEntList() {
		for(int i = 0; i < (int)size.x; ++i)
			for(int j = 0; j < (int)size.y; ++j)
				world[i][j].removeEntities();
	}
	
	public boolean isOutOfBounds(Vector2D v) {
		return !Collider.inRange(v.x, 0, (int)totalMapSize.x - 1) || !Collider.inRange(v.y, 0, (int)totalMapSize.y - 1);
	}
	
	public void placeEnt(Entity ent) {
		Vector2D[] points = ent.getCollider().getPoints();
		
		for(Vector2D v : points) {
			if(!isOutOfBounds(v)) {
				ArrayList<Entity> entList = world[(int)v.x / Game.TROOM_WIDTH][(int)v.y / Game.TROOM_HEIGHT].getEntities();
				if(!entList.contains(ent)) {
					entList.add(ent);
				}
			}
		}
	}
	
	public Entity[] findCollisions(Entity ent) {
		ArrayList<Entity> entOut = new ArrayList<Entity>();
		Vector2D[] points = ent.getCollider().getPoints();
		
		for(Vector2D v : points) {
			if(!isOutOfBounds(v)) {
				Room room = world[(int)v.x / Game.TROOM_WIDTH][(int)v.y / Game.TROOM_HEIGHT];
				ArrayList<Entity> entList = room.getEntities();
			
				for(Entity e : entList) {
					if(!entOut.contains(e) && !e.equals(ent))
						entOut.add(e);
				}
			
				for(int i = 0; i < Game.ROOM_WIDTH; ++i)
					for(int j = 0; j < Game.ROOM_HEIGHT; ++j) {
						Tile t = room.getTile(i, j);
						if(!entOut.contains(t) && !t.equals(ent))
							entOut.add(t);
					}
			}
		}
		
		Entity[] out = new Entity[entOut.size()];
		entOut.toArray(out);		
		return out;
	}
	
	public Entity[] findRenderable(Camera camera) {
		ArrayList<Entity> entOut = new ArrayList<Entity>();
		Vector2D[] points = camera.getPoints();
		
		for(Vector2D v : points) {
			if(!isOutOfBounds(v)) {
				Room room = world[(int)v.x / Game.TROOM_WIDTH][(int)v.y / Game.TROOM_HEIGHT];
				ArrayList<Entity> entList = room.getEntities();
			
				for(Entity e : entList) {
					if(!entOut.contains(e))
						entOut.add(e);
				}
			
				for(int i = 0; i < Game.ROOM_WIDTH; ++i)
					for(int j = 0; j < Game.ROOM_HEIGHT; ++j) {
						Tile t = room.getTile(i, j);
						if(!entOut.contains(t))
							entOut.add(t);
					}
			}
		}
		
		Entity[] out = new Entity[entOut.size()];
		entOut.toArray(out);		
		return out;
	}
	
	public void destroy() {
		for(int i = 0; i < (int)size.x; ++i)
			for(int j = 0; j < (int)size.y; ++j)
				world[i][j].destroy();
	}
}
