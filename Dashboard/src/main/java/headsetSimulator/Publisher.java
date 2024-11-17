package headsetSimulator;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

public class Publisher extends WebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(Publisher.class);

    private boolean running = true;
    private static double theta = 0;

    public Publisher(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        logger.info("Connected to websocket server.");

        // Start sending data at 60 FPS
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                theta += Math.PI / 60;
                double x = (Math.cos(theta) * 50 + 50);
                double y = (Math.sin(theta) * 50 + 50);
                sendData(x, y) ;
            }
        }, 0, 1000 / 60); // 60 FPS
    }

    @Override
    public void onMessage(String message) {
        logger.info("Message from the server: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.info("Disconnected from the server.");
        running = false;
    }

    @Override
    public void onError(Exception ex) {
        logger.error("Error: " + ex.getMessage());
        running = false;
    }

    private void sendData(double x, double y) {
        if (isOpen()) {
            String data = x + "," + y;
            try {
                send(data);
            } catch (Exception e) {
                logger.error("WebSocket is not connected. Cannot send data: " + e.getMessage());
                running = false;
            }
            logger.info("Sent data: ." + data);
        }
    }

    public void close() {
        if (isOpen()) {
            super.close();
        }
    }
}
