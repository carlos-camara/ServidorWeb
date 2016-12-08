

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class servidor extends Thread {

	private Socket socketParaGestionCliente = null;
	private BufferedReader entrada = null;
	private DataOutputStream salida = null;
	private StringTokenizer separador;
	private String cabecera = " ", metodo = " ", nombreFichero = " ", protocolo = " ", informacionPeticion = " ",
			tipoFichero = " ", respuestaError = "<b>NOT FOUND </b>";
	private long tamañoRespuesta;
	private byte[] contenido;
	private File fichero;

	public servidor(Socket cliente) {
		socketParaGestionCliente = cliente;
	}

	public void run() {
		try {
			// Recibir y enviar datos con el cliente
			entrada = new BufferedReader(new InputStreamReader(socketParaGestionCliente.getInputStream()));
			salida = new DataOutputStream(socketParaGestionCliente.getOutputStream());

			gestionCabeceraPeticiones();

			// Mostrar los datos de la petición en el servidor
			while (entrada.ready()) {
				System.out.println(informacionPeticion);
				informacionPeticion = entrada.readLine();
			}
			System.out.println(" ");

			// Comprobar si el fichero existe y gestionar las peticiones en
			// función de ello
			fichero = new File(nombreFichero);
			if (fichero.exists()) {
				gestionRespuesta("HTTP/1.1 200 OK");
			} else {
				gestionRespuesta("HTTP/1.1 404 Not Found");
			}

		} catch (Exception e) {
			System.out.println("Problemas de comunicación con el cliente");
		}

	}

	public void gestionRespuesta(String codigo) throws Exception {

		// Leer fichero y enviar un fichero o mandar una respuesta si no lo
		// encuentra
		if (codigo.indexOf("200") > 0) {
			leerFicheros(nombreFichero);
		} else {
			tamañoRespuesta = respuestaError.length();
		}

		// Envio de datos al cliente
		salida.writeBytes(codigo);
		salida.writeBytes("Content-Type: " + tipoFichero + "\r\n");
		salida.writeBytes("Content-Length: " + String.valueOf(tamañoRespuesta) + "\r\n");
		salida.writeBytes("Connection: close\r\n");
		salida.writeBytes("\r\n");

		if (codigo.indexOf("200") > 0) {
			try {
				for (int i = 0; i < contenido.length; i++) {
					salida.write(contenido);
				}
			} catch (IOException e) {
				// System.out.println("Problemas al leer el fichero");
			}
		} else {
			salida.writeBytes(respuestaError);
		}

		salida.close();
	}

	public void leerFicheros(String nombreFichero) throws IOException {
		Path path = Paths.get(nombreFichero);
		tipoFichero = Files.probeContentType(path);
		contenido = Files.readAllBytes(path);
		tamañoRespuesta = fichero.length();
	}

	public void gestionCabeceraPeticiones() {
		try {
			cabecera = entrada.readLine();
			separador = new StringTokenizer(cabecera);
			metodo = separador.nextToken();
			nombreFichero = separador.nextToken();
			nombreFichero = nombreFichero.replaceFirst("/", "");
			protocolo = separador.nextToken();
			informacionPeticion = cabecera;
		} catch (IOException e) {
			System.out.println("Problemas en la recepción de la petición");
		}
	}

}