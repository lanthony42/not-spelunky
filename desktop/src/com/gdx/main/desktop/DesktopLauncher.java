package com.gdx.main.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.gdx.main.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Game.SCREEN_WIDTH;
		config.height = Game.SCREEN_HEIGHT;
		config.title = "Not Spelunky";
		config.addIcon("icon16.png", FileType.Internal);
		config.addIcon("icon32.png", FileType.Internal);
		config.forceExit = false;
		config.resizable = false;
		new LwjglApplication(new Game(), config);
	}
}
