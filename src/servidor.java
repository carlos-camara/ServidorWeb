
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class servidor extends Thread {

	private Socket socketParaGestionCliente;
	private BufferedReader entrada;
	private DataOutputStream salida;
	private String metodo, nombreFichero, tipoFichero, ficheroError = "error.html";
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

			// Fragmentamos la información de la cabecera en varias variables
			gestionCabeceraPeticiones();

			// Mostrar los datos de la petición en el servidor
			while (entrada.ready()) {
				System.out.println(entrada.readLine());
			}
			System.out.println(" ");

			if (metodo.equals("GET")) {
				fichero = new File(nombreFichero);
				// Comprobamos la existencia del fichero para conocer el codigo
				// de respuesta que deberemos enviar
				gestionRespuesta((fichero.exists()) ? "HTTP/1.1 200 OK" : "HTTP/1.1 404 Not Found");
			}

		} catch (Exception e) {
			System.out.println("Problemas de comunicación con el cliente");
		}

	}

	public void gestionRespuesta(String codigo) throws Exception {
		// Lectura de ficheros
		leerFicheros((codigo.indexOf("200") > 0) ? nombreFichero : ficheroError);

		// Envio de datos cabecera
		salida.writeBytes(codigo + "Content-Type: " + tipoFichero + "\r\n" + "Content-Length: "
				+ String.valueOf(tamañoRespuesta) + "\r\n" + "Connection: close\r\n" + "\r\n");
		// Envio datos contenido fichero
		salida.write(contenido);

		salida.close();
	}

	public void leerFicheros(String nombre) throws IOException {
		Path path = Paths.get(nombre);
		tipoFichero = Files.probeContentType(path);
		fichero = (nombre.equals(ficheroError)) ? new File(nombre) : fichero;
		contenido = Files.readAllBytes(path);
		tamañoRespuesta = fichero.length();
	}

	public void gestionCabeceraPeticiones() {
		try {
			String informacionPeticion = entrada.readLine();
			StringTokenizer separador = new StringTokenizer(informacionPeticion);
			metodo = separador.nextToken();
			nombreFichero = separador.nextToken();
			nombreFichero = nombreFichero.replaceFirst("/", "");
			System.out.println(informacionPeticion);
		} catch (IOException e) {
			System.out.println("Problemas en la recepción de la petición");
		}
	}

}