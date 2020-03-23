package com.gdx.main;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import com.gdx.main.entities.*;
import com.gdx.main.entities.hazard.Hazard;
import com.gdx.main.entities.hazard.JumpEnemy;
import com.gdx.main.entities.hazard.ShootEnemy;
import com.gdx.main.entities.hazard.WalkEnemy;
import com.gdx.main.entities.reward.HealReward;
import com.gdx.main.entities.reward.Reward;
import com.gdx.main.entities.reward.WeaponReward;
import com.gdx.main.entities.tile.BackTile;
import com.gdx.main.entities.tile.PlatformTile;
import com.gdx.main.entities.tile.Tile;
import com.gdx.main.physics.Vector2D;

public class Room {
	private static final int VARIATION = 3;
	
	private static final int BG = 0;
	private static final int BASIC = 1;
	private static final int REWARD = 2;
	private static final int HAZARD = 3;
	
	private Tile[][] room;
	private int[][] template;
	private boolean[] opening;
	
	private ArrayList<Entity> entList;
	private Vector2D position;
	
	public Room(int x, int y, ArrayList<Entity> e) {
		room = new Tile[Game.ROOM_WIDTH][Game.ROOM_HEIGHT];
		template = new int[Game.ROOM_WIDTH][Game.ROOM_HEIGHT];
		opening = new boolean[4];
		
		entList = new ArrayList<Entity>();
		position = new Vector2D(x, y);
	}
	
	// Load room templates and tiles based on spawn chances
	public void load() {
		String path = "";
		Random rand = new Random();
		for(int i = 0; i < 4; ++i)
			path += (!opening[i] ? '1' : '0');
		path += rand.nextInt(path.contentEquals("1111") ? VARIATION * 2 : VARIATION);
		
		FileHandle file = Gdx.files.internal("rooms/" + path + ".txt");
		String temp = file.readString();
		int iter = 0;
		
		while(!temp.equals("")) {
			if(Character.isDigit(temp.charAt(0))) {
				template[iter % Game.ROOM_WIDTH][iter / Game.ROOM_WIDTH] = temp.charAt(0) - '0';
				++iter;
			}
						
			temp = temp.substring(1);
		}
		
		for(int i = 0; i < Game.ROOM_WIDTH; ++i)
			for(int j = 0; j < Game.ROOM_HEIGHT; ++j) {
				if(template[i][j] == BG)
					room[i][j] = new BackTile(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT);
				// Basic platform tile
				else if(template[i][j] == BASIC) {
					if(j + 1 < Game.ROOM_HEIGHT && template[i][j + 1] != 1)
						room[i][j] = new PlatformTile(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, "tile/top");
					else if(j - 1 >= 0 && template[i][j - 1] != 1)
						room[i][j] = new PlatformTile(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, "tile/bottom");
					else
						room[i][j] = new PlatformTile(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, "tile/plat");
				}
				// Reward tiles
				else if(template[i][j] == REWARD) {
					room[i][j] = new BackTile(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT);
					int r = rand.nextInt(Game.getDifficulty() + 1);
					
					if(r < 10) {
						if(rand.nextBoolean() && (r > 70 ? true : rand.nextBoolean()))
							new Reward(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 25, "reward/score1", 16, 16, 1);
					}
					else if(r < 20) {
						if(rand.nextBoolean() || (r > 50 ? true : rand.nextBoolean()))
							new Reward(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 50, "reward/score2", 16, 16, 1);
					}
					else if(r < 40) {
						if(rand.nextBoolean() && (rand.nextBoolean() || rand.nextBoolean()))
							new HealReward(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 10);
					}
					else if(r < 60) {
						if(rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean())
							new WeaponReward(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT);
					}
					else if(r < 80) {
						if(rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean())
							new HealReward(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 20);
					}
					else {
						if(rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean() && rand.nextBoolean())
							new WeaponReward(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT);
						else
							new Reward(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 25, "reward/score1", 16, 16, 1);
					}
				}
				// Hazard tiles
				else if(template[i][j] == HAZARD) {
					room[i][j] = new BackTile(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT);
					int r = rand.nextInt(Game.getDifficulty() + 1);
					
					if(r < 10) {
						if(r > 90 ? true : rand.nextBoolean())
							new Hazard(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 20, 5, "hazard/station", 16, 16, 2, 30);
					}
					else if(r < 20) {
						if(rand.nextBoolean() || (r > 70 ? true : rand.nextBoolean()))
							new Hazard(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 10, 10, "hazard/spike", 16, 16, 2, 15);
					}
					else if(r < 40) {
						if(rand.nextBoolean() || (r > 80 ? true : rand.nextBoolean()))
							new WalkEnemy(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 16, 16);
					}
					else if(r < 50) {
						if(rand.nextBoolean() || rand.nextBoolean())
							new JumpEnemy(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 16, 16, 15, false);
					}
					else if(r < 60) {
						if(rand.nextBoolean() || (r > 80 ? true : rand.nextBoolean()))
							new JumpEnemy(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 16, 16, 10, true);
					}
					else if(r < 80) {
						if(rand.nextBoolean())
							new ShootEnemy(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 16, 16, 7, false);
					}
					else {
						if(rand.nextBoolean() || (r > 100 ? true : rand.nextBoolean()))
							new ShootEnemy(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 16, 16, 5, true);
						else
							new WalkEnemy(true, (int)position.x + i * Game.TILE_WIDTH, (int)position.y + j * Game.TILE_HEIGHT, 16, 16);
					}
				}
			}
	}
	
	public Vector2D getPosition() {
		return position;
	}
	
	public Tile getTile(int x, int y) {
		return room[x][y];
	}
	
	public void setTile(Tile t, int x, int y) {
		room[x][y] = t;
	}
	
	public void setOpening(int direction, boolean open) {
		opening[direction] = open;
	}
	
	public ArrayList<Entity> getEntities(){
		return entList;
	}
	
	public void removeEntities(){
		entList = new ArrayList<Entity>();
	}
	
	public void destroy() {
		for(int i = 0; i < Game.ROOM_HEIGHT; ++i)
			for(int j = 0; j < Game.ROOM_WIDTH; ++j)
				room[j][i].destroy();
	}
	
	public String toString() {
		String out = "";
		for(int i = 0; i < Game.ROOM_HEIGHT; ++i) {
			for(int j = 0; j < Game.ROOM_WIDTH; ++j)
				out += template[j][i] + " ";
			out += '\n';
		}
		
		return out;
	}
}
