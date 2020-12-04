
public class HiloCliente extends Thread{
	private Cliente c;
	public HiloCliente(Cliente cc) {
		c = cc;
	}
	
	
	
	@Override
	public void run() {
		HiloEnvioMensaje h= new HiloEnvioMensaje(c);
		HiloEscuchoMensaje hh = new HiloEscuchoMensaje(c);
		
		h.start();
		hh.start();
    }
	
}
