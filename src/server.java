import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class server {

	private int port;
	private String host;
	private ServerSocket serverSocket;
	protected static HashMap<String, file> filesCache;
	protected int numberRequest;

	/**
	 * Builder.
	 * 
	 * @param port:
	 *            Port where the server will listen.
	 * @param host:
	 *            Host where the server is located.
	 **/
	server(int port, String host) {
		this.port = port;
		this.host = host;
		filesCache = new HashMap<String, file>();
		init();
	}

	/** Start the server. **/
	private void init() {
		try {
			serverSocket = new ServerSocket(port, 6, InetAddress.getByName(host));
			// Attend always requests
			for (;;) {
				Socket clientSocket = serverSocket.accept();
				// Create a object type customerManager and passed the client
				// socket
				customerManager customerManager = new customerManager(clientSocket);
				customerManager.start();
				// Update
				numberRequest++;
				// If there are more than 100 requests
				if (numberRequest > 100)
					cleanFilesCaches();
			}
		} catch (UnknownHostException e) {
			System.out.println("Problems with the selected host");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Files that are not used will be deleted. **/
	synchronized private void cleanFilesCaches() {
		Iterator<Map.Entry<String, file>> itr = filesCache.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, file> entry = itr.next();
			if (entry.getValue().getAppearances() < 6) {
				// Delete the file that has little use
				itr.remove();
			}
		}
		// update
		numberRequest = 0;
	}

}
