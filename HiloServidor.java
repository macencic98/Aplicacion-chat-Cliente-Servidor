


public class HiloServidor extends Thread{
	private Servidor server;
	Cliente c;
	
	public HiloServidor(Servidor s) {
		
		server = s;	
	}
	
	
	@Override
	public void run() {
		while(true) {
			System.out.println("Esperando conexion....");
			server.aceptoConexion();
		}
		
		
		
		}

	
//	@Override
//	public  void run() {
//		synchronized(this) {while(true) {
//			c.enviarMensaje();
//			server.captoMensaje(c);
//		}
//		}
//	} 
	
	
	
}
