package com.proyecto.juego;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.proyecto.mapas.Mapa;
import com.proyecto.pantallas.ScreenFin;
import com.proyecto.piezas.Colores;
import com.proyecto.piezas.Cuadrado;
import com.proyecto.piezas.Pieza;
import com.proyecto.utiles.Assets;
import com.proyecto.utiles.Mundo;
import com.proyecto.utiles.Utiles;

public class Juego implements JuegoEventListener{
	private Mapa mapa;
	private boolean nueva=true;
	private float tiempoMov;
	private float intervaloCaida= 0.6f;
	private Pieza p;
	private int indice;
	private boolean fin =false;
	public Juego(boolean mapa) {
		Utiles.listeners.add(this);
		this.mapa = new Mapa(mapa);
		if(mapa) {
			indice=1;
		}else {
			indice=2;
		}
	}
	public void update(OrthographicCamera cam, float delta) {
			if(nueva) {
				nuevaPieza();
				nueva=!nueva;
			}
			Mundo.batch.setProjectionMatrix(cam.combined);
			cam.update();
			updatePieza();
	}
	public void nuevaPieza() {
		int ind = Utiles.r.nextInt(Colores.values().length-1);
		p = new Pieza(Assets.manager.get(Colores.values()[ind].getDir(),Texture.class), 12,mapa.getSpr().getX()+ mapa.getSpr().getWidth()/2 , mapa.getSpr().getY()+mapa.getSpr().getHeight() - 24,19,4, indice, ind);
		
	}
	
	public Mapa getMapa() {
		return mapa;
	}

	public void render() {
		if(!fin) {
			Mundo.batch.begin();
			mapa.render();
			p.render();	
			Mundo.batch.end();
		}
	}

	public void dispose() {
		mapa.dispose();
		p.dispose();
		
	}
	public void updatePieza() {
		tiempoMov+=Gdx.graphics.getDeltaTime();
		if(tiempoMov>intervaloCaida) {
			bajarPieza();
			
			tiempoMov=0;
			}
		
			
		}
	
	public boolean verifCaida(Cuadrado[] t) { //Verificar colisiones en Y de las piezas
		boolean moverse =true;
		float posYAux;
		int i=0;
		
		do { //Verificar si esta colisionando con el mapa
			Cuadrado c=t[i];
		posYAux=c.getSpr().getY();
		posYAux -=c.getMovimiento();
		if(posYAux <= mapa.getSpr().getY() ) {
				moverse=false;
		}else if(mapa.getCuadrados().size()>0){
				if(colisionCuadrado(c,c.getSpr().getX(),posYAux)) { //Si devuelve falso no colisiona, por ende puede moverse
						moverse=false;
					}
				
			}
		
		i++;
		}while(i<t.length && moverse);
		if(!moverse) {//Si colisiona y no puede moverse en el eje vertical, guardar el sprite y generar una nueva pieza
			for (int j = 0; j < p.getTetromino().length; j++) {
				mapa.getCuadrados().add(p.getTetromino()[j]);
				mapa.agregarAGrilla(p.getTetromino()[j]);	
			}
			Mundo.app.getSv().getHs().enviarMensajeGeneral("guardar"+ "!" + indice);
			verifPerder();
			verifLineaCompl();
			nueva=true;
			
		}
		
		return moverse;
	}


private void verifPerder() {
	int i = 0;
		do {
		if(p.getTetromino()[i].getYGrilla(mapa.getSpr().getY())>mapa.getGrilla().length-2){ 
			Mundo.app.getSv().getHs().enviarMensajeGeneral("termino" + "!" + indice);
			fin=true;
			Mundo.app.setScreen(new ScreenFin());
		}
		i++;
		}while(i<p.getTetromino().length && !fin);
		
	}

public boolean verifMov(Cuadrado[] t, int dir) { //Verificar colisiones en X de las piezas
	boolean mov = true;
	int i=0;
	do {
		Cuadrado c=t[i];
	float posXAux=c.getSpr().getX();
	posXAux +=dir * c.getMovimiento();
	if(dir>0) {
		if(posXAux >=  mapa.getSpr().getX()+ mapa.getSpr().getWidth()- c.getTamaño()) {
			mov=false;
			p.setFilaX(9);
		}else 
			if(mapa.getCuadrados().size()>0) {
				if(colisionCuadrado(c,posXAux,c.getSpr().getY())){
					mov=false;
				}
			}
	}else {
		if(posXAux < mapa.getSpr().getX() + c.getTamaño()) {
			mov=false;
			p.setFilaX(0);
		}else {
			if(mapa.getCuadrados().size()>0) {
				if(colisionCuadrado(c,posXAux,c.getSpr().getY())){
					mov=false;
				}	
			}
		}
	}
	i++;
	}while(i<t.length && mov);
	return mov;
	}


public void moverPieza(int dir, int cliente) {
	Cuadrado[] t = 	p.getTetromino();
	if(verifMov(t,dir)) {//Si devuelve verdadero puede avanzar
		for (int i = 0; i <	t.length; i++) {
			float pos=t[i].getSpr().getX()+dir*t[i].getTamaño();
			t[i].getSpr().setX(pos);
		}
		p.setFilaX(p.getFilaX()+dir);
		Mundo.app.getSv().getHs().enviarMensajeGeneral("mover" + "!"+ p.getFilaX() +"!" + cliente);
	}
	
}

public boolean colisionCuadrado(Cuadrado c, float posAuxX, float posAuxY) {
	boolean colision = false;
	int i=0;
	do {
		if(posAuxY==mapa.getCuadrados().get(i).getSpr().getY() && posAuxX == mapa.getCuadrados().get(i).getSpr().getX()) {
			colision=true;
		}
		i++;
	}while(i<mapa.getCuadrados().size() && !colision);
		

	return colision;
}

public void bajarPieza() {
	if(verifCaida(p.getTetromino())) {
		for (int i = 0; i < p.getTetromino().length; i++) {
			float pos=p.getTetromino()[i].getSpr().getY()- p.getTetromino()[i].getMovimiento();
			p.getTetromino()[i].getSpr().setY(pos);
		}
	p.setFilaY(p.getFilaY()-1);
	Mundo.app.getSv().getHs().enviarMensajeGeneral("bajar"+ "!" + p.getFilaY() + "!" + indice);
	}
}

private boolean colisionRotacion(boolean[][] nuevaPieza) {
		boolean girar=true;
		int xtmp=p.getFilaX();
		int ytmp=p.getFilaY();
		int i=0;
		do {	
			int j=0;
				do {
				if(nuevaPieza[i][j]) {
					int filaXAux = p.getFilaX()+j;
					int filaYAux = p.getFilaY()-i;
					if(filaXAux<0) {
						xtmp+=1;
						}else if(filaXAux>mapa.getGrilla()[0].length-1) {
						xtmp-=1;
						}
					if(filaYAux<0) {
						ytmp+=1;
					}
					if(mapa.getCuadrados().size()>0) {
						if(xtmp+j < 10  && xtmp+j >=0) {
							if(ytmp-i>=0 && ytmp - i < 20) {
								if(mapa.getGrilla()[ytmp-i][xtmp+j]) {
									girar=false;
								}
							}
						}
					}
				}
				j++;
			}while(j<nuevaPieza[i].length && girar);
			i++;
		}while(i<nuevaPieza.length && girar);

		if(girar) {
			p.setFilaX(xtmp);
			p.setFilaY(ytmp);	
		}
		return girar;
		}
	
			
			
	
	
	public void girarPieza(int cliente) { //Odio este codigo
		boolean[][] nuevaPieza = new boolean[p.getTipo().length][p.getTipo()[0].length];
		for(int i = 0; i < p.getTipo().length; i++){
			for(int j = 0; j < p.getTipo()[i].length; j++){
				nuevaPieza[j][ p.getTipo().length - 1 - i ] = p.getTipo()[i][j]; //i=0 j=1
			}
		}
		if(colisionRotacion(nuevaPieza)) {
			Mundo.app.getSv().getHs().enviarMensajeGeneral("girar" + "!"+ p.getFilaX()+"!" + p.getFilaY() +"!" + cliente);
			p.girarTetromino(nuevaPieza, mapa.getSpr().getX()+ p.getTamaño(), mapa.getSpr().getY()+p.getTamaño());
		}
		
		}
		

	
	public void verifLineaCompl() {
		mapa.ordBurbCuadrados();
		int lineas =0;
		for (int i = 0; i < mapa.getGrilla().length; i++) { //Posicion Y
			int tmp=0;
			for (int j = 0; j < mapa.getGrilla()[i].length; j++) {//Posicion X
				if(mapa.getGrilla()[i][j]) {
					tmp++;
				}
			}
			if(tmp==mapa.getGrilla()[i].length) {
				borrarLinea(i);  
				lineas++;
			}
		}
		if(lineas>0) {
			mapa.bajarCuadrados(indice);
			enviarLineas(lineas);
		}
		}
	
	
	@Override
	public void enviarLineas(int lineas) {
		for (int i = 0; i < Utiles.listeners.size(); i++) {
			if(Utiles.listeners.get(i)!=this) {
				Utiles.listeners.get(i).recibirLineas(lineas);
			}
		}
		
		
	}
	
	@Override
	public void recibirLineas(int lineas) {
			int bloqueBorrado = Utiles.r.nextInt(mapa.getGrilla()[0].length );
			Mundo.app.getSv().getHs().enviarMensajeGeneral("enviar"+ "!" +lineas + "!" +bloqueBorrado + "!" + indice);
			for (int i = 0; i < lineas; i++) {
				mapa.masAltoMasBajo();
				mapa.subirCuadrados(0);
				añadirLinea(0, bloqueBorrado);
			}
			
	}
	
	private void añadirLinea(int y, int bloqueBorrado) {
		for (int i = 0; i < mapa.getGrilla()[y].length; i++) {
			if(i!=bloqueBorrado) {
				mapa.getCuadrados().add((new Cuadrado(Assets.manager.get(Colores.AMARILLO.getDir(),Texture.class), 12, i*12 + mapa.getSpr().getX()+12 , y + mapa.getSpr().getY() +12 )));
			}
			
		}
		for (int i = 0; i < mapa.getCuadrados().size(); i++) {
			mapa.agregarAGrilla(mapa.getCuadrados().get(i));
		}
	}
	
	public void borrarLinea(int y) {
		Mundo.app.getSv().getHs().enviarMensajeGeneral("borrar" + "!" + y + "!" +  indice);
		ArrayList<Cuadrado> tmpBorrar = new ArrayList <Cuadrado>();
		for (int j = 0; j < mapa.getCuadrados().size(); j++) {
				if(y==mapa.getCuadrados().get(j).getYGrilla(mapa.getSpr().getY())) {
					mapa.getGrilla()[y][mapa.getCuadrados().get(j).getXGrilla(mapa.getSpr().getX())]=false;
					tmpBorrar.add(mapa.getCuadrados().get(j));
				}
		}
		for (int i = 0; i < tmpBorrar.size(); i++) {
			int j=0;
			boolean bandera=false;
			do {
				if(tmpBorrar.get(i)==mapa.getCuadrados().get(j)) {
					mapa.getCuadrados().remove(mapa.getCuadrados().get(j));
					bandera=true;
				}
				j++;
			}while(j<mapa.getCuadrados().size() && !bandera);
		}
		tmpBorrar.removeAll(tmpBorrar);	
	}

	public int getIndice() {
		return indice;
	}
	
	

}

	
		

	
	


	


	



