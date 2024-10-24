package headsetSimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.net.Socket;

/**
 * ServerHandler class that sends random angles to a connected client every second.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class PublisherHandler implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(Publisher.class);
	private Socket socket;
	
	private static final int SCREEN_WIDTH = 1024;
	private static final int SCREEN_HEIGHT = 768;
	
	public PublisherHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		int x, y;
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				x = (int) (Math.random() * SCREEN_WIDTH);
				y = (int) (Math.random() * SCREEN_HEIGHT);
				logger.info("Sending x and y: {},{}", x, y);
				out.println(x + "," + y);
				Thread.sleep(1000);
			}
		} catch (Exception ex) {
			logger.error("Error in ServerHandler", ex);
		}
	}
	
}