
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class customerManager extends Thread {

	private Socket customerManagerSocket;
	private BufferedReader out;
	private DataOutputStream input;
	private String method, filename, fileType, errorFile = "error.html";
	private long responseSize;
	private byte[] content;
	private File file;

	public customerManager(Socket clientSocket) {
		customerManagerSocket = clientSocket;
	}

	public void run() {
		try {
			// Receive and send data to the customer
			out = new BufferedReader(new InputStreamReader(customerManagerSocket.getInputStream()));
			input = new DataOutputStream(customerManagerSocket.getOutputStream());

			// Fragments the information of the header
			managementHeader();
			// Show the information of the request to the server
			while (out.ready()) {
				System.out.println(out.readLine());
			}
			System.out.println(" ");
			if (method.equals("GET")) {
				file = new File(filename);

				if (server.filesCache.containsKey(filename)) {
					managementResponse("HTTP/1.1 200 OK", false);
				} else {
					// Check if the file exists
					managementResponse((file.exists()) ? "HTTP/1.1 200 OK" : "HTTP/1.1 404 Not Found", true);
				}
			}
		} catch (Exception e) {
			System.out.println("Communication problems with the client");
		}

	}

	/**
	 * Function: Send the content of the header and the content of the file
	 * -Parameters: String code:Let us know if the file exists. Boolean readFile
	 * will help us to know if we have to read the file or not.
	 */
	private void managementResponse(String code, boolean readFile) throws Exception {
		// If you don´t need to read the file
		if (!readFile) {
			fileType = server.filesCache.get(filename).getFileType();
			server.filesCache.get(filename).setAppearances(server.filesCache.get(filename).getAppearances() + 1);
			responseSize = server.filesCache.get(filename).getResponseSize();
			content = server.filesCache.get(filename).getContent();
		} else {
			readFiles((code.indexOf("200") > 0) ? filename : errorFile);
			file file = new file(responseSize, code, content);
			server.filesCache.put(filename, file);
		}

		// Sending data header
		input.writeBytes(code + "Content-Type: " + fileType + "\r\n" + "Content-Length: " + String.valueOf(responseSize)
				+ "\r\n" + "Connection: close\r\n" + "\r\n");
		// Sending data content
		input.write(content);
		input.close();
	}

	/**
	 * Function: Read files -Parameters: String name:filename.
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
	 * Function: Manage the header of the request.
	 */
	private void managementHeader() {
		try {
			// Stores the header information of the request
			String informationRequest = out.readLine();
			StringTokenizer tokenizer = new StringTokenizer(informationRequest);
			method = tokenizer.nextToken();
			// Removed the first position
			filename = tokenizer.nextToken().replaceFirst("/", "");
			System.out.println(informationRequest);
		} catch (IOException e) {
			System.out.println("Problems receiving the request");
		}

	}

}