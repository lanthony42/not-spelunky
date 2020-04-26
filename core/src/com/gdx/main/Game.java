package com.gdx.main;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.gdx.main.entities.*;
import com.gdx.main.entities.hazard.Hazard;
import com.gdx.main.entities.reward.HealReward;
import com.gdx.main.entities.reward.Reward;
import com.gdx.main.entities.reward.WeaponReward;
import com.gdx.main.entities.tile.Tile;
import com.gdx.main.graphics.Animator;
import com.gdx.main.graphics.Camera;

public class Game extends ApplicationAdapter {
	public static final float BASE_UPS = 60.0f;
	public static final float UPS_DELAY = 1 / BASE_UPS;
	public static final float RAMP_UP = 5;
	public static final float IMMUNE = 180;

	public static final int SCREEN_WIDTH = 1408;
	public static final int SCREEN_HEIGHT = 896;
	public static final float SCALE = 1 / 5.0f;

	public static final int ROOM_WIDTH = 12;
	public static final int ROOM_HEIGHT = 8;
	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT = 16;
	public static final int TROOM_WIDTH = ROOM_WIDTH * TILE_WIDTH;
	public static final int TROOM_HEIGHT = ROOM_HEIGHT * TILE_HEIGHT;

	private Viewport viewport;
	private SpriteBatch batch, gui;
	private BitmapFont font, bigFont;	
	private Texture healthGui, healthBar, hurt;
	private Animator back;
	
	private Input input;
	private static int level, difficulty;
	private boolean isNew = true, isPaused, isMenu;
	private int points, immunityTimer;
	private float timer;

	private static ArrayList<Entity> entities;
	private static int totalEnt; 
	private static World map;
	private static Player player;
	private static Camera camera;

	@Override // Used to (re)start game
	public void create() {
		totalEnt = 0;
		level = isNew ? 0 : 1;
		difficulty = (int)(RAMP_UP * level);
		createLevel();
		
		player = new Player(true, (int)map.getStart().getPosition().x + 16, (int)map.getStart().getPosition().y + 32);
		camera = new Camera(SCALE, true);
		camera.setParent(player);

		viewport = new ScreenViewport(camera.getCamera());
		batch = new SpriteBatch();
		gui = new SpriteBatch();		
		font = new BitmapFont(Gdx.files.internal("font.fnt"));
		bigFont = new BitmapFont(Gdx.files.internal("big.fnt"));
		healthGui = new Texture("gui/outline.png");
		healthBar = new Texture("gui/health.png");
		hurt = new Texture("gui/hurt.png");
		back = new Animator("back", 2, 30, true, true);
		
		input = new Input();
		isPaused = false;
		isMenu = true;
		immunityTimer = 0;
		points = 0;
		timer = 0;
	}
	
	// Remake level with difficulty parameter
	public void createLevel() {
		entities = new ArrayList<Entity>();
		
		if(map != null)
			map.destroy();
		map = new World(difficulty, entities);
		
		if(player != null) {
			player.setPosition(map.getStart().getPosition().x + 16, map.getStart().getPosition().y + 64);
			player.setDead(false);
			
			entities.add(player);
		}
		
		immunityTimer = 0;
	}

	@Override // Main access point
	public void render() {
		// Menu state
		if(isMenu) {
			input.update();
			if (input.isNewPressed(Input.KEY_ENTER)) {
				isMenu = false;
			}
			if (input.isNewPressed(Input.KEY_ESC)) {
				Gdx.app.exit();
			}
			
			back.update();
			
			drawMenu();
			//System.out.println(Math.round(1 / Gdx.graphics.getRawDeltaTime()) + " fps");
		}
		// Main game state
		else {
			timer += Math.min(0.5f, Gdx.graphics.getRawDeltaTime());
			while (timer >= UPS_DELAY) {
				input();
				if(!isPaused)
					update();

				timer -= UPS_DELAY;
			}

			draw();
			System.out.println(Math.round(1 / Gdx.graphics.getRawDeltaTime()) + " fps");
		}
	}

	public void input() {
		input.update();

		// Jump
		if (input.isNewPressed(Input.KEY_W)) {
			player.setJump(true);
		}
		// Duck
		else if (input.isNewPressed(Input.KEY_S)) {
			player.setDuck(true);
			player.setJump(false);
		}
		// Nothing Pressed
		if (!input.isPressed(Input.KEY_W)) {
			player.setJump(false);
		}
		if (!input.isPressed(Input.KEY_S)) {
			player.setDuck(false);
		}
		
		// Left
		if (input.isNewPressed(Input.KEY_A)) {
			player.setMoveStateX(Player.LEFT);
		}
		// Right
		else if (input.isNewPressed(Input.KEY_D)) {
			player.setMoveStateX(Player.RIGHT);
		}
		// Nothing Pressed
		else if (!input.isPressed(Input.KEY_A) && !input.isPressed(Input.KEY_D)) {
			player.setMoveStateX(Player.STATIC);
		}

		// Attack Up
		if (input.isNewPressed(Input.KEY_UP)) {
			player.setActState(Player.ATK_UP);
		}
		// Attack Down
		else if (input.isNewPressed(Input.KEY_DOWN)) {
			player.setActState(Player.ATK_DOWN);
		}
		// Attack Left
		else if (input.isNewPressed(Input.KEY_LEFT)) {
			player.setActState(Player.ATK_LEFT);
		}
		// Attack Right
		else if (input.isNewPressed(Input.KEY_RIGHT)) {
			player.setActState(Player.ATK_RIGHT);
		}
		// Enable Protection
		else if(input.isNewPressed(Input.KEY_SPACE)) {
			player.setActState(Player.PROTECT);
		}
		// Nothing Pressed
		else if(!input.isPressed(Input.KEY_UP) && !input.isPressed(Input.KEY_DOWN) && !input.isPressed(Input.KEY_LEFT) && !input.isPressed(Input.KEY_RIGHT) && !input.isPressed(Input.KEY_SPACE)) {
			player.setActState(Player.NORM);
		}
		
		// Window Actions
		if (input.isNewPressed(Input.KEY_P)) {
			isPaused = !isPaused;
		}
		if (input.isNewPressed(Input.KEY_ESC)) {
			Gdx.app.exit();
		}
		if (input.isNewPressed(Input.KEY_R)) {
			create();
		}
	}

	public void update() {
		// Check win conditions
		if(player.isWin()) {
			++level;
			player.incWeaponLevel();
			if (level == 1) player.takeDamage(player.getHealth() - Player.BASE_HEALTH);
			points += (difficulty + 1) * 100;
			difficulty = Math.min(World.ROOM_X.length * 10, (int)(RAMP_UP * level));
			
			isNew = false;
			createLevel();
		}
		
		// Update physics for entities
		for (int i = 0; i < entities.size(); ++i) {
			Entity e = entities.get(i);
			if (e.isActive()) {
				e.update();
			}
		}

		// Check object collisions
		map.clearEntList();
		for (Entity e : entities) {
			if (e.isActive() && !(e instanceof Tile) && !(e instanceof Projectile)) {
				map.placeEnt(e);
			}
		}
		
		for (int i = 0; i < entities.size(); ++i) {
			Entity ent = entities.get(i);
			
			if(ent.isActive() && !(ent instanceof Tile)) {
				Entity[] checkList = map.findCollisions(ent);
				for (Entity e : checkList) {
					if(ent.collide(e)) {
						// Player Collisions
						if(ent instanceof Player) {
							if(e instanceof com.gdx.main.entities.Object) {
								((com.gdx.main.entities.Object)e).move(((com.gdx.main.entities.Object)ent).getVelocity().x / (Game.BASE_UPS * 2));
							}
							
							if(e instanceof Hazard && !((com.gdx.main.entities.Object)e).isDead()) {
								if(immunityTimer >= IMMUNE)
									((Player)ent).takeDamage(((Hazard)e).getDamage());
							}
							else if(e instanceof Reward && !((com.gdx.main.entities.Object)e).isDead()) {
								if(e instanceof HealReward)
									player.takeDamage(-((HealReward)e).getHeal());
								else if(e instanceof WeaponReward)
									player.upgrade();
								
								points += ((Reward)e).getReward();
								e.destroy();
							}
						}		
						// Projectile Collisions
						else if(ent instanceof Projectile) {
							if(e instanceof Tile)
								ent.destroy();
							else if(e instanceof com.gdx.main.entities.Object && !(((Projectile)ent).isPlayer() && e instanceof Player)) {
								if(!((com.gdx.main.entities.Object)e).isDead() && ((Projectile)ent).isPlayer()) {
									((com.gdx.main.entities.Object)e).takeDamage(((Projectile)ent).getDamage());
									
									if(((Projectile)ent).isPlayer()) {
										if(!(e instanceof Reward))
											points += ((Projectile)ent).getDamage();
									
										if(!(e instanceof Reward)) {
											((com.gdx.main.entities.Object)e).move(((Projectile)ent).getKnockback());
											ent.destroy();
										}
										else
											((com.gdx.main.entities.Object)e).move(((Projectile)ent).getKnockback().mult(0.25f));
									}
										
								}
								else {
									if(e instanceof Player) {
										if(immunityTimer >= IMMUNE)
											((com.gdx.main.entities.Object)e).takeDamage(((Projectile)ent).getDamage());
										((com.gdx.main.entities.Object)e).move(((Projectile)ent).getKnockback());
										ent.destroy();
									}
									
									((com.gdx.main.entities.Object)e).move(((Projectile)ent).getKnockback().mult(0.25f));
								}
							}
						}
					}
				}
			}
		}
		
		// Other
		for (Entity e : entities) {
			if (e.isActive()) {
				e.postUpdate();
			}
		}
		
		// Immunity
		if(immunityTimer < IMMUNE) {
			++immunityTimer;
		}
		
		camera.update();
		batch.setProjectionMatrix(camera.getCamera().combined);
	}

	public void draw() {
		Gdx.gl.glClearColor(0.07f, 0.04f, 0.01f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render game entities
		batch.begin();

		Entity[] entList = map.findRenderable(camera);
		// Render Tiles
		for (Entity e : entList) {
			e.setActive(true);
			
			if (e.isActive() && e instanceof Tile)
				e.draw(batch);
		}
		
		// Render Hazards and Reward
		for (Entity e : entList) {
			if (e.isActive() && (e instanceof Hazard || e instanceof Reward))
				e.draw(batch);
		}
		
		// Render Projectiles
		for (Entity e : entities) {
			if (e.isActive() && e instanceof Projectile)
				e.draw(batch);
		}
		
		// Render Player
		player.draw(batch);

		batch.flush();
		batch.end();
		
		// Render GUI
		gui.begin();
		
		font.draw(gui, "Score   " + points, SCREEN_WIDTH - 250, SCREEN_HEIGHT - 25);

		if(player.isDamage() && !player.isDead() && immunityTimer >= IMMUNE) {
			gui.draw(hurt, 0, 0);
		}
		if(player.isDead()) {
			bigFont.draw(gui, "You   Died!", SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2 - 50);
			font.draw(gui, "Press  'R'  to  Restart", SCREEN_WIDTH / 2 - 152, SCREEN_HEIGHT / 2 - 100);
		}
		
		if(player.isWin())
			font.draw(gui, "Loading...", 20, 25);
		else if(isPaused)
			font.draw(gui, "Paused...", 20, 25);
		else
			font.draw(gui, "Level   " + level, 20, 25);
		
		gui.draw(healthBar, 28, SCREEN_HEIGHT - 90, (Math.max(0, player.getHealth()) / (float)Player.BASE_HEALTH) * 335, 40);
		gui.draw(healthGui, 20, SCREEN_HEIGHT - 90, 350, 45);
		font.draw(gui, "Attack   Level  " + player.getWeaponLevel(), 30, SCREEN_HEIGHT - 20);
		
		// Tutorial state
		if(level == 0) {
			if(player.getPositionX() < TROOM_WIDTH * 0.8)
				bigFont.draw(gui, "Use  'WASD'  to  move !", 170, 140);
			else if(player.getPositionX() < TROOM_WIDTH * 1.5)
				bigFont.draw(gui, "Hold  'S'  for  higher  jumps !", 450, 500);
			else if(player.getPositionX() < TROOM_WIDTH * 2.2)
				bigFont.draw(gui, "Use  Arrow  Keys  to  Shoot", 150, SCREEN_HEIGHT / 2);
			else if(player.getPositionX() < TROOM_WIDTH * 2.6)
				bigFont.draw(gui, "Space  helps  tank  damage", 500, 70);
			else if(player.getPositionX() < TROOM_WIDTH * 4)
				bigFont.draw(gui, "Keep  on  going  left !!!", 450, SCREEN_HEIGHT - 100);
		}
		
		gui.flush();
		gui.end();
		
		player.notDamage();
	}
	
	public void drawMenu() {
		gui.begin();
		
		gui.draw(back.getSprite(), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		bigFont.draw(gui, "NOT   SPELUNKY", SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2 + 100);
		font.draw(gui, "The  Game  !", SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2 + 50);
		
		font.draw(gui, "Press  'Enter'  to  Start!", SCREEN_WIDTH / 2 - 145, 50);
		font.draw(gui, "By Anthony Louie", 5, SCREEN_HEIGHT - 10);
		
		gui.flush();
		gui.end();
	}

	public static int getDifficulty() {
		return difficulty;
	}
	
	public static World getWorld() {
		return map;
	}
	
	public static ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public static Entity getEntity(int uid) {
		for(Entity e : entities)
			if(e.getUID() == uid)
				return e;
		
		return null;
	}
	
	public static int getTotalEnt() {
		return totalEnt;
	}
	
	public static Player getPlayer() {
		return player;
	}
	
	public static void increaseEnt() {
		++totalEnt;
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(SCREEN_WIDTH, SCREEN_HEIGHT);
	}

	@Override
	public void dispose() {
		batch.dispose();
		gui.dispose();

		for (Entity e : entities)
			e.dispose();
	}
}
