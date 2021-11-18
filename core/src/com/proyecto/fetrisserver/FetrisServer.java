package com.proyecto.fetrisserver;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.proyecto.pantallas.ScreenJuego;
import com.proyecto.pantallas.ScreenLobby;
import com.proyecto.red.Servidor;
import com.proyecto.utiles.Assets;
import com.proyecto.utiles.Mundo;

public class FetrisServer extends Game {
	private Servidor sv;
	private boolean cambio;
	public void setCambio(boolean cambio) {
		this.cambio = cambio;
	}

	@Override
	public void create () {
		
		Assets.load();
		Assets.manager.finishLoading();
		
		Mundo.app = this;
		Mundo.batch= new SpriteBatch();
		sv = new Servidor();
		this.setScreen(new ScreenLobby());
	}

	@Override
	public void render () {
		super.render();
		if(cambio) {
			cambiar();
		}
	}
	
	@Override
	public void dispose () {
		Mundo.batch.dispose();
	}
	
	public Servidor getSv() {
		return sv;
	}
	
	public void cambiar() {
		this.screen.dispose();
		this.setScreen(new ScreenJuego());
		cambio=!cambio;
	}
}
