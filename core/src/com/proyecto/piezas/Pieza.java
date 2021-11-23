package com.proyecto.piezas;

import com.badlogic.gdx.graphics.Texture;
import com.proyecto.utiles.Mundo;
import com.proyecto.utiles.Utiles;

public class Pieza {
	public int getPieza() {
		return pieza;
	}

	private Cuadrado[] tetromino= new Cuadrado[4];
	private boolean[][] tipoTmp;
	private boolean[][]	tipo;
	private Texture text;
	private int tamaño;
	private float x,y;
	private int filaX;
	private int filaY;
	private int pieza;
	private int ind;
	public Pieza(Texture text, int tamaño, float x, float y, int filaY, int filaX, int ind) {
		this.text=text;
		this.tamaño=tamaño;
		this.x=x;
		this.y=y;
		this.filaX=filaX;
		this.filaY=filaY;
		this.ind = ind;
		crearTetromino();
	}
	
	public Pieza(Texture text, int tamaño, float x, float y, int filaY, int filaX, int ind, int pieza) {
		this.text=text;
		this.tamaño=tamaño;
		this.x=x;
		this.y=y;
		this.filaX=filaX;
		this.filaY=filaY;
		this.ind = ind;
		this.pieza = pieza;
		crearTetromino(pieza);
	}

	private void crearTetromino(int pieza) {
		asignarPieza();
		for (int i = 0; i <tetromino.length; i++) {
			int j=0;
			boolean bandera=false;
			do {
				int k=0;
				do {
					if(tipoTmp[j][k]) {
						tetromino[i]=(new Cuadrado(text, tamaño, x+((k-1)*tamaño), y-(j*tamaño)));
						tipoTmp[j][k]=false;
						bandera=true;
					}
					k++;
				}while(k<tipoTmp[j].length && !bandera);
				j++;
			}while(j<tipoTmp.length && !bandera);
		}
		
	}

	public void crearTetromino(	) {
		pieza = buscarPieza();
		for (int i = 0; i <tetromino.length; i++) {
			int j=0;
			boolean bandera=false;
			do {
				int k=0;
				do {
					if(tipoTmp[j][k]) {
						tetromino[i]=(new Cuadrado(text, tamaño, x+((k-1)*tamaño), y-(j*tamaño)));
						tipoTmp[j][k]=false;
						bandera=true;
					}
					k++;
				}while(k<tipoTmp[j].length && !bandera);
				j++;
			}while(j<tipoTmp.length && !bandera);
		}
		//Termina crearse la pieza
	}
	
	public boolean[][] getTipoTmp() {
		return tipoTmp;
	}

	public Texture getText() {
		return text;
	}

	public int getTamaño() {
		return tamaño;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void girarTetromino(boolean[][] piezaRotada, float correcionX, float correcionY) {
		asignarPieza(piezaRotada);
		for (int i = 0; i <tetromino.length; i++) {
			int j=0;
			boolean bandera=false;
			do {
				int k=0;
				do {
					if(tipoTmp[j][k]) {
//						tetromino[i]=(new Cuadrado(this.text, this.tamaño, (filaX+k)*this.tamaño+ correcionX, (filaY-j)*this.tamaño+correcionY));
						tetromino[i].getSpr().setX((filaX+k) * this.tamaño+ correcionX);
						tetromino[i].getSpr().setY((filaY-j)*this.tamaño+correcionY);
						
						tipoTmp[j][k]=false;
						bandera=true;
					}
					k++;
				}while(k<tipoTmp[j].length && !bandera);
				j++;
			}while(j<tipoTmp.length && !bandera);
		}
		
	}
	
	public int getFilaX() {
		return filaX;
	}

	public void setFilaX(int filaX) {
		this.filaX = filaX;
	}
	
	private int buscarPieza() {
		int ind = Utiles.r.nextInt(Piezas.values().length);
		boolean[][] tmp = Piezas.values()[ind].getPieza();
		tipoTmp = new boolean [tmp.length][tmp[0].length];
		tipo = new boolean [tmp.length][tmp[0].length];
		clonarArray(tmp,tipoTmp);
		clonarArray(tmp,tipo);
		return ind;
	}
	
	private void asignarPieza(boolean[][] piezaRotada) {
		boolean[][] tmp = piezaRotada;
		tipoTmp = new boolean [tmp.length][tmp[0].length];
		tipo = new boolean [tmp.length][tmp[0].length];
		clonarArray(tmp,tipoTmp);
		clonarArray(tmp,tipo);
	}
	
	private void asignarPieza() {
		boolean[][] tmp = Piezas.values()[pieza].getPieza();;
		tipoTmp = new boolean [tmp.length][tmp[0].length];
		tipo = new boolean [tmp.length][tmp[0].length];
		clonarArray(tmp,tipoTmp);
		clonarArray(tmp,tipo);
	}
	
	private void clonarArray(boolean[][] tmp, boolean[][] ttmp) {
		for (int i = 0; i < tmp.length; i++) {
			for (int j = 0; j < tmp[i].length; j++) {
				ttmp[i][j]=tmp[i][j];
			}
		}
		
	}

	public boolean[][] getTipo() {
		return tipo;
	}

	public void render() {
		for (int i = 0; i <this.tetromino.length; i++) {
			this.tetromino[i].getSpr().draw(Mundo.batch);
		}
	}
	
	public void setX(float x) {
		this.x = x;
	}

	public int getFilaY() {
		return filaY;
	}

	public void setFilaY(int filaY) {
		this.filaY = filaY;
	}

	public void setY(float y) {
		this.y = y;
	}
	public Cuadrado[] getTetromino() {
		return tetromino;
	}

	public void dispose() {
		text.dispose();
		
	}

	public int getInd() {
		return ind;
	}
	}





