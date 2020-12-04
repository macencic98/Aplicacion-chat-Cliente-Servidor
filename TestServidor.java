import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import junit.framework.Assert;
import java.net.Socket;

class TestServidor {
	Servidor server = new Servidor();
	@Test
	void testCreacionServidor() {
		assertEquals(0, server.getCantidadSalas());
	}
	
	@Test
	void testAgregoSala() {
		server.agregarSala("Aces High");
		assertEquals(1,server.getCantidadSalas());
	}
	
	@Test 
	void testSalaMismoNombre() {
		assertTrue(server.agregarSala("Aces High 2"));///Demuestro que puedo agregar una sala con un nombre X
		
		assertFalse(server.agregarSala("Aces High 2"));
		//Demuestro que si ya hay una sala con ese nombre X, no puedo crear otra con el mismo nombre
	}

	@Test 
	void testSalaCreacionCon0Usuarios() {
		Sala ss = new Sala("ss",server);
		assertEquals(0,ss.getCantidadUsuarios());
		
	}
	
	@Test 
	void testAgregoClienteASala() {
		Socket n = null;
		String nombre = "Jorge";
		Sala ss = new Sala("asd",server);
		
		ss.agregarJugadorSala(n, nombre);
		assertEquals(1,ss.getCantidadUsuarios());
	}
	
	
	@Test
	void testSacoClienteSala() {
		Socket n = null;
		String nombre = "Jorge";
		Sala ss = new Sala("Sala de Aces High",server);
		
		ss.agregarJugadorSala(n, nombre);
		int cantidadInicial = ss.getCantidadUsuarios();
		
		ss.sacarJugadorSala(n, nombre);
		assertEquals(cantidadInicial-1,ss.getCantidadUsuarios());
	}
		
}

