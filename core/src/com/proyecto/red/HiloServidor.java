package com.proyecto.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.proyecto.pantallas.ScreenJuego;
import com.proyecto.utiles.Mundo;

public class HiloServidor extends Thread{
	private DatagramSocket socket;
	private boolean fin = false;
	private int creados=0;
	public HiloServidor() {
		try {
			socket = new DatagramSocket(6969);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	public void enviarMensaje(String msg, InetAddress ip, int puerto) {
			byte[] data = msg.getBytes();
			DatagramPacket dp = new DatagramPacket(data, data.length, ip, puerto);
			try {
				socket.send(dp);
			} catch (IOException e) {	
				e.printStackTrace();
			}
		
	}
	
	@Override
	public void run() {
		do {
			byte[] data = new byte[1024];
			DatagramPacket dp = new DatagramPacket(data, data.length);
			try {
				socket.receive(dp);

				
			} catch (IOException e) {
				e.printStackTrace();
			}
			procesarMensaje(dp);
			
		}while(!fin);
	}
	
	private void procesarMensaje(DatagramPacket dp) {
		String msg = (new String(dp.getData())).trim();
		String[] comando = msg.split("!");
		if(Mundo.app.getSv().getClientes().size()<2) {
			if(comando[0].equals("Conexion")) {
				Mundo.app.getSv().getClientes().add(new Cliente(dp.getAddress(), dp.getPort()));
				enviarMensaje("OK"+ "!" + Mundo.app.getSv().getClientes().size(), dp.getAddress(), dp.getPort());
				
				if(Mundo.app.getSv().getClientes().size()==2) {
					Mundo.app.setCambio(true);				
				}
				
			}
			
		}
		if(comando[0].equals("creado")) {
			creados++;
			if(creados==2) {
				ScreenJuego sj = (ScreenJuego) Mundo.app.getScreen();
				sj.setCreado(true);
			}
		}
		if(comando[0].equals("bajar")) {
			ScreenJuego sj = null;
			try {
				sj = (ScreenJuego) Mundo.app.getScreen();
			} catch (Exception e) {
				System.out.println("error");
			}
			if(sj.getJuego().getIndice() ==Integer.valueOf(comando[1])) {
				sj.getJuego().bajarPieza();	 
			}else {
				sj.getJuego2().bajarPieza();
			}
	
		
		}	
		if(comando[0].equals("girar")) {
			ScreenJuego sj = null;
			try {
				sj = (ScreenJuego) Mundo.app.getScreen();
			} catch (Exception e) {
				System.out.println("error");
			}
			if(sj.getJuego().getIndice() ==Integer.valueOf(comando[1])) {
				sj.getJuego().girarPieza(Integer.valueOf(comando[1]));	 
			}else {
				sj.getJuego2().girarPieza(Integer.valueOf(comando[1]) );	 
			}
		}
		if(comando[0].equals("mover")) {
			ScreenJuego sj = null;
			try {
				sj = (ScreenJuego) Mundo.app.getScreen();
			} catch (Exception e) {
				System.out.println("error");
			}
			if(sj.getJuego().getIndice() ==Integer.valueOf(comando[2])) {
				sj.getJuego().moverPieza(Integer.valueOf(comando[1]), Integer.valueOf(comando[2]));	 
			}else {
				sj.getJuego2().moverPieza(Integer.valueOf(comando[1]),Integer.valueOf(comando[2]));	 
			}
		}


	}
	

	public void enviarMensajeGeneral(String msj) {
			for(int i = 0; i < Mundo.app.getSv().getClientes().size(); i++) {
				enviarMensaje(msj, Mundo.app.getSv().getClientes().get(i).getIp(), Mundo.app.getSv().getClientes().get(i).getPuerto());
			}
		
	}
	
	
}
