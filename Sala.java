import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Sala {
	private ArrayList<Socket> listaClientes;
	private HashMap<String, Socket> listaCli;
	private HashMap<String, Date> tiempoClientes;
		
	private String nombreSala;
	private Servidor s;
	private String historial="";
	
	public Sala(String nombre, Servidor s) {
		nombreSala = nombre;

		listaClientes = new ArrayList<Socket>();
		listaCli = new HashMap<String,Socket>();
		tiempoClientes = new HashMap<String,Date>();
		this.s = s;
	}
	
	
	
	
	
	
	public String mostrarUsuariosDeSala() {
		String mensajeUsuarios ="Lista de usuarios en la sala "+nombreSala+"\n";
		DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");  
	    
		for (Map.Entry<String, Date> listaUsers : tiempoClientes.entrySet()) {
		    mensajeUsuarios+= "Usuario: "+ listaUsers.getKey()+"| Conectado desde: "+hourFormat.format(listaUsers.getValue());
		}
		    
		return mensajeUsuarios;
	}
	
	
	public int getCantidadUsuarios() {
		return this.listaClientes.size();
	}
	
	
	public String getNombreSala() {
		return this.nombreSala;
	}
	
	
	public void sacarJugadorSala(Socket n, String nombre) {
		listaClientes.remove(n);
		listaCli.remove(nombre);
	}

	
	public boolean agregarJugadorSala(Socket c, String nombre) {
		System.out.println("AGREGO AL CLIENTE");
		listaClientes.add(c);
		listaCli.put(nombre, c);
		Date date = new Date(System.currentTimeMillis());
		tiempoClientes.put(nombre,date);
		
		System.out.println("Hay "+listaClientes.size()+" CLIENTES en la sala de nombre" + nombreSala);
		
		return true;
	}
	
	
	public void transmitoMensaje(String mensaje, String destinatario) {
		
			try {
				DataOutputStream envio = new DataOutputStream(listaCli.get(destinatario).getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
	public void transmitoMensaje(String mensaje) throws IOException {
		for(Socket s: listaClientes) 
			this.s.enviarMensaje(s, mensaje);
		
	}
	
	public Socket existeJugador(String nombre) {
		 if(listaCli.containsKey(nombre))
			 return listaCli.get(nombre);
		 return null;
	}
	
	public String getHistorial() {
		return this.historial;
	}
	
	public void setHistorial(String mensajeNuevo) {
		this.historial += mensajeNuevo;
	}
	
	
	public String getTiempoDeConexion() {
        String tiempoDeConexion = "";

        long horaConexUser, horaAct = new Date(System.currentTimeMillis()).getTime();
        long dife;

        for (Map.Entry<String, Date> listaUsers : tiempoClientes.entrySet()) {
            horaConexUser = listaUsers.getValue().getTime();
            dife = horaAct - horaConexUser;
            int segundos = (int) dife / 1000;
            int minutos = segundos / 60;
            int hora = minutos / 60;

            tiempoDeConexion += listaUsers.getKey() + ">> " + hora + ":" + minutos % 60 + ":" + segundos % 60 + "\n";
        }

        return tiempoDeConexion;
    }
	
	
	public static boolean validarNumeroSala(String n, int index) {
		for(int i = index; i < n.length(); i++)
			if(n.charAt(i)<'0'|| n.charAt(i)>'9')
				return false;
		return true;
		
	}
	
	}
	

