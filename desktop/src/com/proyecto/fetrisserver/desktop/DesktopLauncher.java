package com.proyecto.fetrisserver.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.proyecto.fetrisserver.FetrisServer;
import com.proyecto.utiles.Config;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = Config.ALTO;
		config.width = Config.ANCHO;
		config.title = Config.NOMBRE;
		new LwjglApplication(new FetrisServer(), config);
	}
}
