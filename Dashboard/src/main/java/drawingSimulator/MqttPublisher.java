package drawingSimulator;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttPublisher {
    private static final Logger logger = LoggerFactory.getLogger(MqttPublisher.class);

    private MqttClient client;

    // Constructor that initializes the MqttClient
    public MqttPublisher(String broker) {
        try {
            client = new MqttClient(broker, MqttClient.generateClientId());
        } catch (MqttException e) {
            System.err.println("Error creating MQTT client: " + e.getMessage());
        }
    }

    // Method to connect to the broker
    public void connect() {
        try {
            client.connect();
            System.out.println("Connected to MQTT broker.");
        } catch (MqttException e) {
            System.err.println("Error connecting to MQTT broker: " + e.getMessage());
        }
    }

    // Method to publish a message to a topic
    public void publish(String topic, String message) {
        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(2);
            client.publish(topic, mqttMessage);
            System.out.println("Published message: " + message + " to topic: " + topic);
        } catch (MqttException e) {
            System.err.println("Error publishing message: " + e.getMessage());
        }
    }

    // Method to disconnect the MQTT client
    public void disconnect() {
        if (client != null) {
            try {
                client.disconnect();
                System.out.println("Disconnected from MQTT broker.");
            } catch (MqttException e) {
                System.err.println("Error while disconnecting: " + e.getMessage());
            } finally {
                client = null; // Clear the client reference
            }
        } else {
            System.out.println("Client is already disconnected or was never connected.");
        }
    }
}
