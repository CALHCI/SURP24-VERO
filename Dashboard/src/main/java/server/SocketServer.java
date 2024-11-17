package server;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class SocketServer extends WebSocketServer {
    private static final String ALLOWED_PATH = "/websocket";
    private final MessageHandler messageHandler;

    public SocketServer(String ip, int port, double[] originValues, double[] deltaXValues, double[] deltaYValues) {
        super(new InetSocketAddress(ip, port));

        Pose origin = new Pose(originValues[0], originValues[1], originValues[2], originValues[3], originValues[4], originValues[5]);
        Pose pX = new Pose(deltaXValues[0], deltaXValues[1], deltaXValues[2], deltaXValues[3], deltaXValues[4], deltaXValues[5]);
        Pose pY = new Pose(deltaYValues[0], deltaYValues[1], deltaYValues[2], deltaYValues[3], deltaYValues[4], deltaYValues[5]);
        Plane plane = new Plane(origin, pX, pY);

        this.messageHandler = new MessageHandler(plane);
    }

    public SocketServer(InetSocketAddress address, MessageHandler messageHandler) {
        super(address);
        this.messageHandler = messageHandler;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String requestedPath = handshake.getResourceDescriptor();
        if (requestedPath.equals(ALLOWED_PATH)) {
            System.out.println("Connected to " + conn.getRemoteSocketAddress());
        } else {
            System.out.println("Connection attempt to invalid path: " + requestedPath);
            conn.close();
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        messageHandler.handleMessage(message);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Disconnected from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket Server started on " + getAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }
}