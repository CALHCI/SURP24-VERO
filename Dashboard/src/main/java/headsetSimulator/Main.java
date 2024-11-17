package headsetSimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * This is only to test the Java Desktop Application: Control Panel.
 * This is not needed once you have the Meta Quest 3 streaming data to the server.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class Main {
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		URI serverUri = URI.create("ws://localhost:8080/websocket"); // Change to server URL
		Publisher publisher = new Publisher(serverUri);
		publisher.connect();

		try {
			Thread.sleep(1500); // Run for 1.5 seconds
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restore interrupted status
			logger.error("Main thread was interrupted", e);
		} catch (Exception e) {
			logger.error("An error occurred", e);
		} finally {
			publisher.close();
			logger.info("Publisher closed. Exiting application.");
			System.exit(0);
		}
	}
	
}