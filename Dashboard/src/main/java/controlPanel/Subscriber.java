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
	private boolean running = false;
	private SocketServer server;
	
	public Subscriber(String broker, String topic, double[] originValues, double[] deltaXValues, double[] deltaYValues) {
		this.server = new SocketServer(broker, topic, originValues, deltaXValues, deltaYValues);
	}
	
	@Override
	public void run() {
		this.running = true;
		this.server.start();
		logger.info("Subscriber is running");

	}
	
	public void stop() {
		this.server.stop();
		this.running = false;
		logger.info("Subscriber has stopped");
	}
	
	public boolean isRunning() {
		return running;
	}
	
}