package com.proyecto.mapas;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.proyecto.piezas.Cuadrado;
import com.proyecto.utiles.Config;
import com.proyecto.utiles.Mundo;

public class Mapa {
	
	private boolean[][] grilla= new boolean[20][10];
	private Texture text;
	private Sprite spr;
	private ArrayList <Cuadrado> cuadrados= new ArrayList<Cuadrado>();
	private boolean primer;
	public Mapa(boolean primer) {
		text= new Texture("Tetriminos/Board/Board.png");
		spr = new Sprite(text);
		this.primer= primer;
		if(primer) {
			spr.setBounds(36, Config.ALTO/120, (grilla[0].length+2)*12,(grilla.length+2)*12);
		}else {
			spr.setBounds(Config.ANCHO/3, Config.ALTO/120, (grilla[0].length+2)*12,(grilla.length+2)*12);
		}
	}
	public void render() {
		this.spr.draw(Mundo.batch);
		for (int i = 0; i < cuadrados.size(); i++) {
			cuadrados.get(i).getSpr().draw(Mundo.batch);
		}
	}
	public boolean[][] getGrilla() {
		return grilla;
	}
	public Sprite getSpr() {
		return spr;
	}
	public void mirarGrilla() {
		for (int i =grilla.length-1; i > -1; i--) {
			for (int j = 0; j < grilla[i].length; j++) {
				System.out.print((grilla[i][j])? "X" : "O");
				System.out.print("|");
			}
			System.out.println();
		}
		System.out.println();	
	}
	public void agregarAGrilla(Cuadrado t) {
			grilla[t.getYGrilla(spr.getY())][t.getXGrilla(spr.getX())]=true;
	}
	public void quitarAGrilla(Cuadrado t) {
		grilla[t.getYGrilla(spr.getY())][t.getXGrilla(spr.getX())]=false;
	}
	public ArrayList<Cuadrado> getCuadrados() {
		return cuadrados;
	}
	
	public void bajarCuadrados(int indice) {
		boolean bandera=false;
		for (int i =grilla.length-1; i > -1; i--) {
			int tmp=0;
			for (int j = 0; j < grilla[i].length; j++) {
				if(grilla[i][j]) {
					bandera=true;
				}else {
					tmp++;
				}
			}
			if(tmp==grilla[i].length && bandera) {
				Mundo.app.getSv().getHs().enviarMensajeGeneral("bajarlinea"+ "!" + i + "!" + indice );
				for (int j = 0; j < cuadrados.size(); j++) {
					if(cuadrados.get(j).getYGrilla(spr.getY()) > i) {
						quitarAGrilla(cuadrados.get(j));
						cuadrados.get(j).getSpr().setY(cuadrados.get(j).getSpr().getY()-(cuadrados.get(j).getMovimiento()));
						agregarAGrilla(cuadrados.get(j));
						
					}
				}
				
			}
		}
	
	}
	public void ordBurbCuadrados(){
		Cuadrado auxiliar;
		for (int i = 0; i < cuadrados.size(); i++) {
			for(int j=i + 1; j < cuadrados.size(); j++) {
				if(cuadrados.get(i).getYGrilla(spr.getY()) > cuadrados.get(j).getYGrilla(spr.getY())) {
					auxiliar = cuadrados.get(i);
					cuadrados.set(i, cuadrados.get(j));
					cuadrados.set(j, auxiliar);
				}
			}
			
		}

	
  }
public void masAltoMasBajo(){
		Cuadrado auxiliar;
		for (int i = 0; i < cuadrados.size(); i++) {
			for(int j=i + 1; j < cuadrados.size(); j++) {
				if(cuadrados.get(i).getYGrilla(spr.getY()) < cuadrados.get(j).getYGrilla(spr.getY())) {
					auxiliar = cuadrados.get(i);
					cuadrados.set(i, cuadrados.get(j));
					cuadrados.set(j, auxiliar);
				}
			}
			
		}
}
	
	public void subirCuadrados(int y) {
		for (int i = 0; i < cuadrados.size(); i++) {
			if(cuadrados.get(i).getYGrilla(spr.getY())>=y) {
				quitarAGrilla(cuadrados.get(i));
				cuadrados.get(i).getSpr().setY(cuadrados.get(i).getSpr().getY()+cuadrados.get(i).getMovimiento());
				if(cuadrados.get(i).getYGrilla(spr.getY())<grilla.length) {
					agregarAGrilla(cuadrados.get(i));
				}
				
			}
		}

	}
	public void dispose() {
		text.dispose();
	}

	
}
