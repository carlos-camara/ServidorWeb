
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Author: Carlos Cámara
 */

public class CustomerManager extends Thread {

	private Socket customerManagerSocket;
	private BufferedReader input;
	private DataOutputStream out;
	private String method, filename, fileType, errorFile = "error.html";
	private long responseSize;
	private byte[] content;
	private File file;

	public CustomerManager(Socket clientSocket) {
		customerManagerSocket = clientSocket;
	}

	public void run() {
		try {
			// Receive and send data to the customer
			input = new BufferedReader(new InputStreamReader(customerManagerSocket.getInputStream()));
			out = new DataOutputStream(customerManagerSocket.getOutputStream());

			// Fragments the information of the header
			managementHeader();
			// Show the information of the request to the server
			while (input.ready()) {
				// System.out.println(input.readLine());
				Server.gui.getDisplay().append(input.readLine() + "\n");
			}
			System.out.println(" ");
			if (method.equals("GET")) {
				file = new File(filename);
				if (Server.filesCache.containsKey(filename)) {
					managementResponse("HTTP/1.1 200 OK", false);
				} else {
					// Check if the file exists
					managementResponse((file.exists()) ? "HTTP/1.1 200 OK" : "HTTP/1.1 404 Not Found", true);
				}
			}
		} catch (Exception e) {
			// System.out.println("Communication problems with the client");
		}

	}

	/**
	 * Send the content of the header and the content of the file
	 * 
	 * @param code
	 *            :Let us know if the file exists.
	 * @param readFile
	 *            :will help us to know if we have to read the file or not.
	 */
	private void managementResponse(String code, boolean readFile) throws Exception {
		// If you don´t need to read the file
		if (!readFile) {
			fileType = Server.filesCache.get(filename).getFileType();
			Server.filesCache.get(filename).setAppearances(Server.filesCache.get(filename).getAppearances() + 1);
			responseSize = Server.filesCache.get(filename).getResponseSize();
			content = Server.filesCache.get(filename).getContent();
		} else {
			readFiles((code.indexOf("200") > 0) ? filename : errorFile);
			AdaptedFile file = new AdaptedFile(responseSize, code, content);
			Server.filesCache.put(filename, file);
		}
		// Sending data header
		out.writeBytes(code + "Content-Type: " + fileType + "\r\n" + "Content-Length: " + String.valueOf(responseSize)
				+ "\r\n" + "Connection: close\r\n" + "\r\n");
		// Sending data content
		out.write(content);
		out.close();
	}

	/**
	 * Read files
	 * 
	 * @param name
	 *            : Name of the file to be read.
	 */
	private void readFiles(String name) {
		Path path = Paths.get(name);
		try {
			fileType = Files.probeContentType(path);
			// If there have been problems with the file
			if (name.equals(errorFile))
				file = new File(name);
			// Content file
			content = Files.readAllBytes(path);
			// Length file
			responseSize = file.length();
		} catch (IOException e) {
			System.out.println("Problems reading file");
		}
	}

	/**
	 * Manage the header of the request.
	 */
	private void managementHeader() {
		try {
			// Stores the header information of the request
			String informationRequest = input.readLine();
			StringTokenizer tokenizer = new StringTokenizer(informationRequest);
			method = tokenizer.nextToken();
			// Removed the first position
			filename = tokenizer.nextToken().replaceFirst("/", "");
			Server.gui.getDisplay().append(informationRequest + "\n");
		} catch (IOException e) {
			System.out.println("Problems receiving the request");
		}

	}

}