package controlPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.SocketServer;

/**
 * Client class to connect to the Meta Quest 3 and receive data.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class Subscriber implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);
	private String ip;
	private int port;
	private boolean running = false;
	private SocketServer server;
	
	public Subscriber(String ip, int port, double[] originValues, double[] deltaXValues, double[] deltaYValues) {
		this.ip = ip;
		this.port = port;
		this.server = new SocketServer(ip, port, originValues, deltaXValues, deltaYValues);
	}
	
	@Override
	public void run() {
		this.server.start();
		logger.info("Subscriber is running");
		this.running = true;
	}
	
	public void stop() {
		try {
			this.server.stop();
			this.running = false;
			logger.info("Subscriber has stopped");
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restore the interrupted status
			logger.info("Failed to stop the WebSocket Server: " + e.getMessage());
		}

	}
	
	public boolean isRunning() {
		return running;
	}
	
}