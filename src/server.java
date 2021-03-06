import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Carlos Cámara
 */

public class server {

	private int port;
	private String host;
	private ServerSocket serverSocket;
	protected static Map<String, file> filesCache;
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
		filesCache = new ConcurrentHashMap<String, file>();

		init();
	}

	/** Start the server. **/
	private void init() {
		try {

			serverSocket = new ServerSocket(port, 6, InetAddress.getByName(host));

			test();

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
	private void cleanFilesCaches() {
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

	public void test() {
		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				if (filesCache.size() > 0) {
					Iterator<Map.Entry<String, file>> itr = filesCache.entrySet().iterator();
					while (itr.hasNext()) {
						Map.Entry<String, file> entry = itr.next();
						try {
							Path path = Paths.get(entry.getKey());
							// Content file
							byte[] content = Files.readAllBytes(path);
							// If the content of the file is different
							if (!Arrays.equals(content, entry.getValue().getContent())) {
								filesCache.get(entry.getKey()).setContent(content);
							}
						} catch (IOException e) {
							System.out.println("Problems reading file");
						}
					}
				}
				// calling again
				test();
			}
			//Every 50 minutes	
		}, 3000000);
	}

}

