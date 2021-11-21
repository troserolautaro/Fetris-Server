package com.proyecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.proyecto.juego.Juego;
import com.proyecto.utiles.Config;
import com.proyecto.utiles.Mundo;

public class ScreenJuego implements Screen {
 private Juego juego;
 private Juego juego2;
 private OrthographicCamera cam;
private boolean creado=false;
	@Override
	public void show() {
		Mundo.app.getSv().getHs().enviarMensajeGeneral("Empieza");
		iniciarCam();
		juego= new Juego(true);
		juego2= new Juego(false);
	}


	@Override
	public void render(float delta) {
		if(creado) {
			Gdx.gl.glClearColor(0, 0, 0, 0);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			update(delta);
			juego.render();
			juego2.render();	
		}
			
//			debug();			
		
	}
	
//	private void debug() {
//		if(Gdx.input.justTouched()) {
//	           System.out.println(cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0))); 
//		}
//	}
//		if(Gdx.input.isKeyPressed(Input.Keys.W)) {
//			
//			cam.position.y+=1;
//		}
//		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
//			
//			cam.position.y-=1;
//		}
//		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
//			cam.position.x-=1;
//			
//		}
//		if(Gdx.input.isKeyPressed(Input.Keys.D)) {
//			cam.position.x+=1;
//			
//		}
//		if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
//			cam.zoom-=1/Utiles.PPM;
//			
//		}
//		if(Gdx.input.isKeyPressed(Input.Keys.E)) {
//			cam.zoom+=1/Utiles.PPM;
//			
//		}
	
//	}

	private void update(float delta) {
		Mundo.batch.setProjectionMatrix(cam.combined);
		cam.update();
		juego.update(delta);
		juego2.update(delta);
	}


	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
	
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		juego.dispose();
		juego2.dispose();
	
	}
	private  void iniciarCam() {
		cam= new OrthographicCamera();
		cam.setToOrtho(false, Config.ANCHO/ 2, Config.ALTO / 2);
		cam.zoom=1f;
		
		cam.update();
	}
	
	public void setCreado(boolean creado) {
		this.creado=creado;
	}
	public Juego getJuego() {
		return juego;
	}
	public Juego getJuego2() {
		return juego2;
	}
}
