import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
public class Cliente {
	public Socket conexionServidor;
	private String nombre;
	private DataOutputStream envioDatos;
	private DataInputStream reciboDatos;
	private InputStreamReader teclado;
	private BufferedReader leoTeclado;

	
	public Cliente(String nombre){
		this.nombre = nombre;
	}
	
	public boolean conectarseServidor(String ip, int puerto) {
		
		try {
			conexionServidor = new Socket(ip,puerto);
			reciboDatos = new DataInputStream(conexionServidor.getInputStream());
			envioDatos = new DataOutputStream(conexionServidor.getOutputStream());
			envioDatos.writeUTF(nombre);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public  String enviarMensaje() {
		String mensaje ="";
		 teclado = new InputStreamReader(System.in);
		 leoTeclado = new BufferedReader (teclado);
		try {
			mensaje = leoTeclado.readLine();
			envioDatos.writeUTF(mensaje);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mensaje;
	}
	
	
	public String recibirMensaje() {
		String recibo="";
		try {
			 recibo = reciboDatos.readUTF();
			System.out.println(recibo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return recibo;
	}
	
	
	protected Socket getSocket() {
		return this.conexionServidor;
	}
	
	
	public void cerrarInputStream() {
		try {
			reciboDatos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void cerrarOutputStream() {
		try {
			envioDatos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cerrarStreamsTeclado() {
		try {
			teclado.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			leoTeclado.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void cerrarSocket() {
		try {
			conexionServidor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
