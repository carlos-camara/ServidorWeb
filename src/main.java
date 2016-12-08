

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class main {

	public static void main(String[] args) {
		ServerSocket Server;
		try {
			Server = new ServerSocket(8080, 6, InetAddress.getByName("127.0.0.1"));
			while (true) {
				Socket cliente = Server.accept();
				servidor servidorGestorClientes = new servidor(cliente);
				servidorGestorClientes.start();
			}
		} catch (UnknownHostException e) {
			System.out.println("Problemas en la comunicación con el host seleccionado");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
