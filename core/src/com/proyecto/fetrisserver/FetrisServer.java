package com.proyecto.fetrisserver;

import java.rmi.server.ServerCloneException;

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
	private ScreenJuego sj;
	private ScreenLobby sl;
//	private ScreenFin sf;
	private boolean fin=false;
	private boolean lobby = false;

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
//		if(fin) {
//			fin();
//		}
		if(lobby) {
			lobby();
		}
	}
	@Override
	public void dispose () {
		sv.getHs().enviarMensajeGeneral("cerro");
		Mundo.batch.dispose();
	}
	
	public Servidor getSv() {
		return sv;
	}
	
	private void cambiar() {
		screen.dispose();
		this.setScreen(sj = new ScreenJuego());
		cambio=!cambio;
		super.render();
	}
	public void setLobby(boolean lobby) {
		this.lobby = lobby;
	}

//	private void fin() {
//		screen.dispose();
//		this.setScreen(sf = new ScreenFin(true));
//		fin=!fin;
//		super.render();
//	}
	private void lobby() {
		screen.dispose();
		this.setScreen(sl= new ScreenLobby());
		lobby=!lobby;
		super.render();
	}
	
	public void setFin(boolean fin) {
		this.fin = fin;
	}


	public void setCambio(boolean cambio) {
		this.cambio = cambio;
	}
}
