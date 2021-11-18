package com.proyecto.red;

import java.net.InetAddress;

public class Cliente {

	private InetAddress ip;
	private int puerto;
//	private float score = 0;
	
	public Cliente(InetAddress ip, int puerto) {
		this.ip = ip;
		this.puerto = puerto;
	}

	public InetAddress getIp() {
		return ip;
	}

	public int getPuerto() {
		return puerto;
	}
	
//	public float getScore() {
//		return score;
//	}



}
