package server;

//import org.java_websocket.WebSocket;
//import org.java_websocket.handshake.ClientHandshake;
//import org.java_websocket.server.WebSocketServer;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.net.InetSocketAddress;

public class SocketServer {
    private final MessageHandler messageHandler;
    private MqttClient client;
    private String topic;

    public SocketServer(String broker, String topic, double[] originValues, double[] deltaXValues, double[] deltaYValues) {
        try {
            client = new MqttClient(broker, MqttClient.generateClientId());
        } catch (MqttException e) {
            System.err.println("Error creating MQTT client: " + e.getMessage());
        }
        this.topic = topic;

        Pose origin = new Pose(originValues[0], originValues[1], originValues[2], originValues[3], originValues[4], originValues[5]);
        Pose pX = new Pose(deltaXValues[0], deltaXValues[1], deltaXValues[2], deltaXValues[3], deltaXValues[4], deltaXValues[5]);
        Pose pY = new Pose(deltaYValues[0], deltaYValues[1], deltaYValues[2], deltaYValues[3], deltaYValues[4], deltaYValues[5]);
        Plane plane = new Plane(origin, pX, pY);

        this.messageHandler = new MessageHandler(plane);
    }

    public void start() {
        try {
            client.connect();
            client.subscribe(topic, (t, message) -> {
                messageHandler.handleMessage(new String(message.getPayload()));
            });
            System.out.println("Subscribed to topic: " + topic);
        } catch (Exception e) {
            System.err.println("MQTT error: " + e.getMessage());
        }
    }

    public void stop() {
        if (client != null) {
            try {
                client.disconnect();
                System.out.println("Disconnected from MQTT broker.");
            } catch (MqttException e) {
                System.err.println("Error while disconnecting: " + e.getMessage());
            } finally {
                client = null;
            }
        } else {
            System.out.println("Client is already disconnected or was never connected.");
        }
    }
}