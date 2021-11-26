package com.proyecto.juego;

import java.util.EventListener;

public interface JuegoEventListener extends EventListener {
	public void enviarLineas(int lineas);
	public void recibirLineas(int lineas);
	public void cambioVelocidad(float vel);
}	
