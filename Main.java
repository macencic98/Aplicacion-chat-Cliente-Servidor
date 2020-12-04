
public class Main {
	public static void main(String[] args) {
			
		
		Servidor s = new Servidor(14000);
		//Cliente c= new Cliente("Maxi");

		///SALAS DEFAULT
		Sala sal1 = new Sala("SALA ACES HIGH 1",s);
		Sala sal2 = new Sala("SALA ACES HIGH 2",s);
		
		s.agregarSala(sal1);
		s.agregarSala(sal2);
		
		
		//c.conectarseServidor("192.168.1.33", 14000);

		HiloServidor hilo = new HiloServidor(s);
	///	HiloCliente hiloC = new HiloCliente(c);
		
		//hiloC.start();

			
		hilo.start();
		
	}
	
	
	
}
