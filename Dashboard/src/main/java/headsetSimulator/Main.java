package headsetSimulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is only to test the Java Desktop Application: Control Panel.
 * This is not needed once you have the Meta Quest 3 streaming data to the server.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 1.0
 */
public class Main {
	static double theta = 0;
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static final String BROKER = "tcp://localhost:1883";
	private static final String TOPIC = "vr";
	
	public static void main(String[] args) {
		MqttPublisher mqttPublisher = new MqttPublisher(BROKER);

		mqttPublisher.connect();


		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				theta += Math.PI / 30;
				double x = (Math.cos(theta) * 50 + 50);
				double y = (Math.sin(theta) * 50 + 50);
				mqttPublisher.publish(TOPIC, x + "," + y);
			}
		}, 0, 1000 / 4);

		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restore interrupted status
			logger.error("Main thread was interrupted", e);
		} catch (Exception e) {
			logger.error("An error occurred", e);
		} finally {
			mqttPublisher.disconnect();
			logger.info("Publisher closed. Exiting application.");
			System.exit(0);
		}
	}
	
}