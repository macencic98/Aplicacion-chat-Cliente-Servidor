
public class HiloEscuchoMensaje extends Thread {
	private Cliente c;
	
	public HiloEscuchoMensaje(Cliente cc) {
		c = cc;
	}
	
	@Override
	public void run() {
		String mens;
		do {
			
			mens = c.recibirMensaje();
	}while(mens.compareTo("/3")!=0);
	
		
		
		c.cerrarStreamsTeclado();
		c.cerrarOutputStream();
		c.cerrarInputStream();
		c.cerrarSocket();
	}
	
	
}
