package org.example;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends WebSocketClient {
    private static double theta = 0;

    public Client(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to the server");

        // Start sending data at 60 FPS
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                theta += Math.PI / 60;
                int x = (int) (Math.cos(theta) * 50 + 50);
                int y = (int) (Math.sin(theta) * 50 + 50);
                sendData(x, y) ;
            }
        }, 0, 1000 / 60); // 60 FPS
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Message from server: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected from the server");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Error: " + ex.getMessage());
    }

    private void sendData(int x, int y) {
        String data = x + "," + y;
        send(data);
        System.out.println("Sent: " + data);
    }

    public static void main(String[] args) {
        URI serverUri = URI.create("ws://localhost:8080/websocket"); // Change to server URL
        Client client = new Client(serverUri);
        client.connect();

        try {
            Thread.sleep(1000); // Run for 1 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}
