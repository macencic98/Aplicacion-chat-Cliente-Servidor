import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.junit.platform.commons.util.StringUtils;


public class Servidor {
	private ServerSocket servidor;
	public ArrayList<Socket> arrayClientes;
	private ArrayList<Sala> salasDeChat;
	
	
	
	public Servidor(int port) {
		try {
			servidor = new ServerSocket(port);
			arrayClientes = new ArrayList<Socket>();
			salasDeChat = new ArrayList<Sala>();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Servidor() {
		///Este constructor lo utilizo para realizar los test y no tener la necesidad de abrir algun puerto
		///para poder realizarlos
		arrayClientes = new ArrayList<Socket>();
		salasDeChat = new ArrayList<Sala>();
	}
	
	public void cerrarConexion() {
		try {
			servidor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void aceptoConexion() {
		try {
			
			Socket cliente = servidor.accept();
			if(cliente==null)
				return;
			arrayClientes.add(cliente);
			DataOutputStream mensaje = new DataOutputStream(cliente.getOutputStream());
			mensaje.writeUTF("BIENVENIDO AL CANAL DE CHAT DE ACES HIGH");
			
			HiloAtencionCliente hiloC = new HiloAtencionCliente(cliente,this);
			hiloC.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String captoMensaje(Socket c) {
		String mensaje=null;
		DataInputStream mensajeRecibeServidor;
		try {
			mensajeRecibeServidor = new DataInputStream(c.getInputStream());
			mensaje = mensajeRecibeServidor.readUTF();
	
			return mensaje;
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	
	public void enviarMensaje(Socket c, String mensaje) {
		try {
			DataOutputStream enviaMensajeServer = new DataOutputStream(c.getOutputStream());
			enviaMensajeServer.writeUTF(mensaje);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void eliminarSala(Sala s) {
		salasDeChat.remove(salasDeChat.indexOf(s));
	}
	
	
	public boolean agregarSala(String n) {
		if(existeSala(n))
			return false;
		Sala nuevaSala = new Sala(n,this);
		salasDeChat.add(nuevaSala);
		return true;
	}
	
	public void agregarSala(Sala nuevaSala) {
		salasDeChat.add(nuevaSala);
	}
	
	public String mostrarSalas() {
		String mensaje= "Listado Salas";
		int i = 1;
		if(salasDeChat.isEmpty())
			return mensaje;
		for(Sala s: salasDeChat) 
			mensaje=mensaje+"\nNombre Sala "+i++ +":"+s.getNombreSala()+"| Cant Usuarios: "+s.getCantidadUsuarios();	
	return mensaje;
	}
	
	public int getCantidadSalas() {
		return salasDeChat.size();
	}
	
	
	public void agregarJugadorASala(int n, Socket c, String nn) {
		salasDeChat.get(n-1).agregarJugadorSala(c,nn);
	}
	
	public void sacarJugadorSala(int n, Socket c, String nom) {
		salasDeChat.get(n-1).sacarJugadorSala(c, nom);
	}
	
	public void sacarJugador(ArrayList<Integer> salas, Socket c, String nom) {
			for(int n: salas) 
				salasDeChat.get(n-1).sacarJugadorSala(c,nom);
	}
	
	public void envioMensajeASala(String n, int sala) {
		try {
			//System.out.println("Transmito el mensaje a la sala "+sala);
			salasDeChat.get(sala-1).transmitoMensaje(n);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getUsuariosSala(int sala) {
		return salasDeChat.get(sala-1).mostrarUsuariosDeSala();
	}
	
	

	
	public int procesarMensaje(String message, Socket c, ArrayList<Integer> salasConectado, String nombre, int salaActual) {
		if(message.length()>=2 && message.charAt(0)=='/') {
			
			if(message.charAt(1)=='1') {
				if(salasConectado.size()==3)
				{
					enviarMensaje(c,"Usted no puede conectarse a mas de 3 salas");
					return salaActual;
				}
				if(!procesoCreacionSala(c,nombre)) {
					enviarMensaje(c,"Ya existe una sala con ese nombre!!!");
					return salaActual;
				}
				salasConectado.add(salasDeChat.size());
				enviarMensaje(c,"Usted se ha conectado a la sala "+salasDeChat.size()+", y comienza a enviar mensajes a esa sala");
				return salasDeChat.size();
			}
			
			if(message.charAt(1)=='2') {
			enviarMensaje(c,mostrarSalas());
			return salaActual;
			}
			
			if(message.charAt(1)=='3') {
				if(salasConectado.size()!=0)
				sacarJugador(salasConectado, c,nombre);
				enviarMensaje(c,"Estimado "+nombre+" usted ha sido desconectado del servidor");
				enviarMensaje(c,"/3");
				return -1;
			}
			
			
			if(message.charAt(1)=='c' && message.length()>2) 
			{
				if(!Sala.validarNumeroSala(message,2)) {
					 enviarMensaje(c,"Error: comando equivocado");
					 return salaActual;
				 }
				int nroSala = Integer.parseInt(message.substring(2));
				Integer nue = nroSala;
				if(nroSala>0 ) {
					
					if(salasConectado.contains(nue)) {
						enviarMensaje(c,"Usted ya esta conectado a la sala");
						return salaActual;
					}
					if(salasConectado.size()==3) {
						enviarMensaje(c,"Usted esta conectado a 3 salas en el momento");
					return salaActual;
					}
					
					///ESTA PARTE DEBE ESTAR SINCRONIZADA, YA QUE EN CASO DE CREARSE 2 AL MISMO TIEMPO,
					//HABRIA UN ERROR CON EL NUMERO DE SALA CORRESPONDIENTE A CADA UNA Y ESO NO PUEDE OCURRIR
					if(nroSala<=salasDeChat.size()) {synchronized(this) {
					agregarJugadorASala(nroSala, c, nombre);
					salasConectado.add(nroSala);
					enviarMensaje(c,"\nUsted ha sido conectado a la sala "+nroSala);
					return nroSala;
					}
					}
					else enviarMensaje(c,"No existe la sala a conectarse!!");
				return salaActual;
					}
			}
			
			
			if(message.charAt(1)=='d' && message.length()>2) {
				if(!Sala.validarNumeroSala(message,2)) {
					 enviarMensaje(c,"Error: comando equivocado");
					 return salaActual;
				 }
				int salaMeDesconecto = Integer.parseInt(message.substring(2));
				Integer nue = salaMeDesconecto;
				if(salasConectado.contains(nue)) {
					sacarJugadorSala(salaMeDesconecto, c, nombre);
					salasConectado.remove(salasConectado.indexOf(salaMeDesconecto));
					enviarMensaje(c,"Usted ha sido removido de la sala"+salaMeDesconecto);
					return 0;
				}
				else {
					enviarMensaje(c,"Usted no se encuentra conectado a esa sala");
				}
			}
		
			
			
			if(message.charAt(1)=='r' && message.length()>2) {
				
				 if(!Sala.validarNumeroSala(message,2)) {
					 enviarMensaje(c,"Error: comando equivocado");
					 return salaActual;
				 }
					 
				int salaMandoMensajes = Integer.parseInt(message.substring(2));
				Integer nue = salaMandoMensajes;
				if(salasConectado.contains(nue)) {
					enviarMensaje(c,"\nUsted ahora esta mandando mensajes a la sala "+salaMandoMensajes);
					return salaMandoMensajes;
					
				}
				else enviarMensaje(c, "Usted no esta conectado a esa sala");
			}
			
			
			if(message.charAt(1)=='w'  && salaActual!=0 && message.length()>=3 && message.contains(" ")) {
				String destinatario = message.substring(2,message.indexOf(' ', 2));
				Socket destino = salasDeChat.get(salaActual-1).existeJugador(destinatario);
				if(destino==null)
					enviarMensaje(c,"El usuario no existe en la sala actual!!!");
				else {
					Date date = new Date(System.currentTimeMillis());
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
				enviarMensaje(destino,"Susurro:\n["+dateFormat.format(date)+" "+hourFormat.format(date)+"] "+nombre+" dice: "+message.substring(message.indexOf(' ',2))+"\n");
				enviarMensaje(c,"Susuro enviado a "+destinatario+": "+message.substring(message.indexOf(' ',2))+"\n");
				}
				}
			
			
//			if(message.charAt(1) == 'h' && salaActual != 0) {
//				enviarMensaje(c,"\nEl historial de la sala:" + salasDeChat.get(salaActual - 1).getNombreSala() + " es el siguiente:\n" + salasDeChat.get(salaActual-1).getHistorial());
//				return salaActual;
//			}
			
			if (message.charAt(1) == 'h' && salaActual != 0) {
                enviarMensaje(c, "\nEl historial de la sala:" + salasDeChat.get(salaActual - 1).getNombreSala()
                        + " es el siguiente:\n" + salasDeChat.get(salaActual - 1).getHistorial());
                return salaActual;
            }
			
			if (message.charAt(1) == 't') {
                enviarMensaje(c,
                        "Sala: " + salasDeChat.get(salaActual - 1).getNombreSala()
                                + " | Tiempo de conexion (HH:mm:ss)\n"
                                + salasDeChat.get(salaActual - 1).getTiempoDeConexion());
            
			}

			}
	
		else if(salaActual!=0 ){
	
			Date date = new Date(System.currentTimeMillis());
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
			 message = "["+dateFormat.format(date)+" "+hourFormat.format(date)+"] "+nombre+": "+message;
			 envioMensajeASala("Sala "+salaActual+" "+salasDeChat.get(salaActual-1).getNombreSala()+"->"+message, salaActual);
			 salasDeChat.get(salaActual - 1).setHistorial(message+"\n");
		}
		else enviarMensaje(c,"\nComando erroneo: Usted no se encuentra conectado a ninguna sala momentaneamente!!");
			return salaActual;
	}
	
	
	private boolean procesoCreacionSala(Socket cliente, String nombre) {
		enviarMensaje(cliente, "Ingrese el nombre de la sala que quiere crear");
		String nombreSala = captoMensaje(cliente);
		
		if(!agregarSala(nombreSala))
			return false;
		
		agregarJugadorASala(getCantidadSalas(), cliente, nombre);
		return true;
	}
	
	
public boolean existeSala(String nombrePongo) {
		for(Sala s: salasDeChat)
			if(s.getNombreSala().compareTo(nombrePongo)==0)
				return true;
		return false;
	}
	
}

