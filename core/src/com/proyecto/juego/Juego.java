package com.proyecto.juego;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.proyecto.mapas.Mapa;
import com.proyecto.piezas.Colores;
import com.proyecto.piezas.Cuadrado;
import com.proyecto.piezas.Pieza;
import com.proyecto.utiles.Assets;
import com.proyecto.utiles.Config;
import com.proyecto.utiles.Mundo;
import com.proyecto.utiles.Utiles;

public class Juego implements JuegoEventListener{
	private Mapa mapa;
	private boolean nueva=true;
	private float tiempoMov;
	private float intervaloCaida= 0.6f;
	private Pieza pieza;
	private Pieza sigP;
	private Pieza piezaGuardada;
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
	
	
	public void update( float delta) {
			if(nueva) {
				sigPieza();
				nuevaPieza();
				nueva=!nueva;
			}
			updatePieza();
	}
	
	
	private void sigPieza() {	
		Mundo.app.getSv().getHs().enviarMensajeGeneral("crearPieza" + "!" + indice);
		pieza = new Pieza(sigP.getText(),sigP.getTama�o(), mapa.getSpr().getX()+ mapa.getSpr().getWidth()/2,mapa.getSpr().getY()+mapa.getSpr().getHeight() - 24,sigP.getFilaY(),sigP.getFilaX(), sigP.getInd(), sigP.getPieza());
		
		//		p= new Pieza(Assets.manager.get(Colores.values()[text].getDir(), Texture.class) ,12,mapa.getSpr().getWidth()/2, mapa.getSpr().getHeight() - 24,19,4, pieza, mapa.getSpr().getX(), mapa.getSpr().getY());
		
	}
	


	public void nuevaPieza() {
		int ind = Utiles.r.nextInt(Colores.values().length-1);
		if(indice==1) {
			sigP = new Pieza(Assets.manager.get(Colores.values()[ind].getDir(),Texture.class), 12,Config.ANCHO/4-24 , Config.ALTO/4 + 12 ,19,4,ind);
		}else {
			sigP = new Pieza(Assets.manager.get(Colores.values()[ind].getDir(),Texture.class), 12,Config.ANCHO/4+24 , Config.ALTO/4 + 12 ,19,4, ind);
		}
		Mundo.app.getSv().getHs().enviarMensajeGeneral("crearSigPieza" + "!" + ind + "!" + sigP.getPieza() + "!" + indice);
	
	}
	
	
	public Mapa getMapa() {
		return mapa;
	}

	public void render() {
		Mundo.batch.begin();
		mapa.render();
		pieza.render();	
		sigP.render();
		if(piezaGuardada!=null) {
			piezaGuardada.render();
		}
		Mundo.batch.end();
		
	}

	public void dispose() {
		
		
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
			if(mapa.getCuadrados().size()>0) {
				verifPosCuadrados();
			}
			for (int j = 0; j < pieza.getTetromino().length; j++) {
				mapa.getCuadrados().add(pieza.getTetromino()[j]);
				if(pieza.getTetromino()[j].getYGrilla(mapa.getSpr().getY())<mapa.getGrilla().length) {
					mapa.agregarAGrilla(pieza.getTetromino()[j]);
				}
			}
			Mundo.app.getSv().getHs().enviarMensajeGeneral("guardar"+ "!" + pieza.getFilaX()+ "!" +pieza.getFilaY()+ "!" + indice);
			verifLineaCompl();
			nueva=true;
			verifPerder();
			
		}
		
		return moverse;
	}


	private void verifPosCuadrados() {
		ArrayList <Cuadrado> cuadrados = mapa.getCuadrados();
		Cuadrado[] tetromino = pieza.getTetromino();
		
		int corregir = 0;
		for (int i = 0; i < tetromino.length; i++) {
			int j = 0;
			boolean bandera = false;
			do {
				if(cuadrados.get(j).getSpr().getY() == tetromino[i].getSpr().getY() && cuadrados.get(j).getSpr().getX() == tetromino[i].getSpr().getX() ) {
					bandera=true;
					corregir+=1;
				}
				j++;
			}while(j<cuadrados.size() && bandera);
		}
		if(corregir>0) {
			for (int i = 0; i < tetromino.length; i++) {
					tetromino[i].getSpr().setY(tetromino[i].getSpr().getY()+ corregir * pieza.getTama�o());
				}
			pieza.setFilaY(pieza.getFilaY()+corregir);
		}
	}
	

	private void verifPerder() {
		int i = 0;
			do {
			if(pieza.getTetromino()[i].getYGrilla(mapa.getSpr().getY())>mapa.getGrilla().length-3){ 
				Mundo.app.getSv().getHs().enviarMensajeGeneral("termino" + "!" + indice);
				Mundo.app.getSv().getHs().setCreados(0);
				Utiles.listeners.removeAll(Utiles.listeners);
				
				Mundo.app.getSv().getClientes().removeAll(Mundo.app.getSv().getClientes());
				fin=true;
				Mundo.app.setLobby(true);
				
			}
			i++;
			}while(i<pieza.getTetromino().length && !fin);
			
	}
	
	
	public boolean verifMov(Cuadrado[] t, int dir) { //Verificar colisiones en X de las piezas
		boolean mov = true;
		int i=0;
		do {
			Cuadrado c=t[i];
		float posXAux=c.getSpr().getX();
		posXAux +=dir * c.getMovimiento();
		if(dir>0) {
			if(posXAux >=  mapa.getSpr().getX()+ mapa.getSpr().getWidth()- c.getTama�o()) {
				mov=false;
			}else 
				if(mapa.getCuadrados().size()>0) {
					if(colisionCuadrado(c,posXAux,c.getSpr().getY())){
						mov=false;
					}
				}
		}else {
			if(posXAux < mapa.getSpr().getX() + c.getTama�o()) {
				mov=false;
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
		Cuadrado[] t = 	pieza.getTetromino();
		if(verifMov(t,dir)) {//Si devuelve verdadero puede avanzar
			for (int i = 0; i <	t.length; i++) {
				float pos=t[i].getSpr().getX()+dir*t[i].getTama�o();
				t[i].getSpr().setX(pos);
			}
			pieza.setFilaX(pieza.getFilaX()+dir);
			Mundo.app.getSv().getHs().enviarMensajeGeneral("mover" + "!"+ pieza.getFilaX() +"!" + cliente);
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
		if(verifCaida(pieza.getTetromino())) {
			for (int i = 0; i < pieza.getTetromino().length; i++) {
				float pos=pieza.getTetromino()[i].getSpr().getY()- pieza.getTetromino()[i].getMovimiento();
				pieza.getTetromino()[i].getSpr().setY(pos);
			}
		pieza.setFilaY(pieza.getFilaY()-1);
		Mundo.app.getSv().getHs().enviarMensajeGeneral("bajar"+ "!" + pieza.getFilaY()+ "!" +pieza.getFilaX() + "!" + indice);
		}
	}

	private boolean colisionRotacion(boolean[][] nuevaPieza) {
			boolean girar=true;
			int xtmp=pieza.getFilaX();
			int ytmp=pieza.getFilaY();
			int i=0;
			do {	
				int j=0;
					do {
					if(nuevaPieza[i][j]) {
						int filaXAux = pieza.getFilaX()+j;
						int filaYAux = pieza.getFilaY()-i;
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
				pieza.setFilaX(xtmp);
				pieza.setFilaY(ytmp);	
			}
			return girar;
	}
	
			
			
	
	
	public void girarPieza() { //Odio este codigo
		boolean[][] nuevaPieza = new boolean[pieza.getTipo().length][pieza.getTipo()[0].length];
		for(int i = 0; i < pieza.getTipo().length; i++){
			for(int j = 0; j < pieza.getTipo()[i].length; j++){
				nuevaPieza[j][ pieza.getTipo().length - 1 - i ] = pieza.getTipo()[i][j]; //i=0 j=1
			}
		}
		if(colisionRotacion(nuevaPieza)) {
			Mundo.app.getSv().getHs().enviarMensajeGeneral("girar" + "!"+ pieza.getFilaX()+"!" + pieza.getFilaY() +"!" + indice);
			pieza.girarTetromino(nuevaPieza, mapa.getSpr().getX()+ pieza.getTama�o(), mapa.getSpr().getY()+pieza.getTama�o());
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
			if(mapa.getCuadrados().size()>0) {
				mapa.bajarCuadrados(indice);
			}
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
				a�adirLinea(0, bloqueBorrado);
			}
			
	}
	
	
	private void a�adirLinea(int y, int bloqueBorrado) {
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
	
	public void guardarPieza() {
		if(piezaGuardada== null) {
			piezaGuardada = new Pieza(pieza.getText(),
					pieza.getTama�o(),
					mapa.getSpr().getX()-24,
					mapa.getSpr().getY()+mapa.getSpr().getHeight()-32,
					19,
					4, 
					pieza.getInd(),
					pieza.getPieza());
			nueva=true;
		}else {
			
			Pieza auxP = new Pieza(
					pieza.getText(),
					pieza.getTama�o(),
					mapa.getSpr().getX()-24,
					mapa.getSpr().getY()+mapa.getSpr().getHeight()-32,
					19,
					4, 
					pieza.getInd(),
					pieza.getPieza());
			
			pieza= new Pieza(
					piezaGuardada.getText(),
					piezaGuardada.getTama�o(),
					pieza.getX(),
					pieza.getY(),
					19,
					4, 
					piezaGuardada.getInd(),
					piezaGuardada.getPieza());
			
			piezaGuardada = new Pieza(
					auxP.getText(),
					auxP.getTama�o(),
					auxP.getX(), 
					auxP.getY(),
					auxP.getFilaY(),
					auxP.getFilaX(),
					auxP.getInd(),
					auxP.getPieza());
			
		}
	
			
// new Pieza(sigP.getText(),sigP.getTama�o(), mapa.getSpr().getX()+ mapa.getSpr().getWidth()/2,mapa.getSpr().getY()+mapa.getSpr().getHeight() - 24,sigP.getFilaY(),sigP.getFilaX(), sigP.getInd(), sigP.getPieza());
	}
	public boolean getFin() {
		return fin;
	}
	public void setFin(boolean fin) {
		this.fin = fin;
	}
}

	
		

	
	


	


	



