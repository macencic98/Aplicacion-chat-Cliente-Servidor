import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class HiloAtencionCliente extends Thread{
	private Socket cliente;
	private Servidor s;
	private String n;
	
	public HiloAtencionCliente(Socket c, Servidor ss) {
		cliente = c;
		s = ss;
		
	}
	
	@Override
	public void run() {
		ArrayList<Integer> salasConectado = new ArrayList<Integer>(); 
		int opcionLobby;
		int salaSeConecta = 0;
		n = s.captoMensaje(cliente);
		String message;
		int salaActual = 0;
		s.enviarMensaje(cliente, "BIENVENIDO AL LOBBY DE LA SALA DE CHAT DE ACES HIGH");
		
		s.enviarMensaje(cliente,"\n\nOpciones que usted puede realizar: \n"
				+ "/1 crear salas de chat\r\n" + 
				"/2 ver salas conectadas\r\n" + 
				"/3 salir de la sala de chat(lobby)\r\n" + 
				"/w+personaQueEstaenMiSalaDeChat -> enviar un mensaje privado a la persona que esta en mi sala de chat\r\n" + 
				"/d+NroSala -> Desconectarme de esa sala si es que estoy en ella\r\n" + 
				"/c+NroSala -> Conectarme a esa sala de chat\r\n" + 
				"/r+nroSala  -> si esta conectado, comienza a enviar mensajes a esa sala"+
				"\n/h ->Se ve el historial de mensajes de la sala actual\n"
				+"\n/t -> Ver los usuarios de la sala actual y su tiempo de conexion \n");
		s.enviarMensaje(cliente, "Disfrute su estadia !!\n\n");
		
		while(salaActual!=-1) 
			salaActual = s.procesarMensaje(s.captoMensaje(cliente),cliente,salasConectado,n,salaActual);
		
		
		
		}
	
	
}