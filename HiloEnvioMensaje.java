
public class HiloEnvioMensaje extends Thread {
	private Cliente c;
	
	public HiloEnvioMensaje(Cliente cc) {
		c = cc;
	}
	
	@Override
	public void run() {
		String mens = "";
		while(mens.compareTo("/3")!=0) {
			mens = c.enviarMensaje();
		}
			
	}
}
