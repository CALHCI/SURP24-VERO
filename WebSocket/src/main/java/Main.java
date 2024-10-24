package org.example;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

class SocketServer extends WebSocketServer {
    private static final String ALLOWED_PATH = "/websocket";
    private final MessageHandler messageHandler;

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

class VRData {
    private final int x;
    private final int y;

    public VRData(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

class DataParser {
    public static VRData parseVRData(String data) {
        String[] parts = data.split(",");
        if (parts.length == 2) {
            int x = parseCoordinate(parts[0]);
            int y = parseCoordinate(parts[1]);
            return new VRData(x, y);
        } else {
            throw new IllegalArgumentException("Invalid message format: " + data);
        }
    }

    private static int parseCoordinate(String part) {
        try {
            return Integer.parseInt(part);
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + part);
            return -1;
        }
    }
}

class Pose {
    final double x, y, z, rx, ry, rz;

    public Pose(double x, double y, double z, double rx, double ry, double rz) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    @Override
    public String toString() {
        return String.format("Pose[x=%.2f, y=%.2f, z=%.2f, rx=%.2f, ry=%.2f, rz=%.2f]", x, y, z, rx, ry, rz);
    }
}

class Plane {
    private final Pose origin, pX, pY;

    public Plane(Pose origin, Pose pX, Pose pY) {
        this.origin = origin;
        this.pX = pX;
        this.pY = pY;
    }

    public Pose getBarycentric(VRData vrData) {
        double u = vrData.getX() / 100.0;
        double v = vrData.getY() / 100.0;

        double x = origin.x + u * (pX.x - origin.x) + v * (pY.x - origin.x);
        double y = origin.y + u * (pX.y - origin.y) + v * (pY.y - origin.y);
        double z = origin.z + u * (pX.z - origin.z) + v * (pY.z - origin.z);

        double rx = (origin.rx + pX.rx + pY.rx) / 3.0;
        double ry = (origin.ry + pX.ry + pY.ry) / 3.0;
        double rz = (origin.rz + pX.rz + pY.rz) / 3.0;

        return new Pose(x, y, z, rx, ry, rz);
    }
}

class MessageHandler {
    private final Plane plane;

    public MessageHandler(Plane plane) {
        this.plane = plane;
    }

    public void handleMessage(String message) {
        VRData vrData = DataParser.parseVRData(message);
        System.out.println(vrData); // VISUALIZE THIS DATA
        Pose output = plane.getBarycentric(vrData);
        System.out.println(output);
    }
}

public class Main {
    public static void main(String[] args) {
        Pose origin = new Pose(-201.74, -495.33, -250.59, 0, 2.191, -2.217);
        Pose pX = new Pose(148.54, -495.33, -250.59, 0, 2.191, -2.217);
        Pose pY = new Pose(-201.74, -495.33, 8.28, 0, 2.191, -2.217);
        Plane plane = new Plane(origin, pX, pY);

        MessageHandler messageHandler = new MessageHandler(plane);
        SocketServer socketServer = new SocketServer(new InetSocketAddress("localhost", 8080), messageHandler);

        socketServer.start();
    }
}


///* GENERATED BOILERPLATE FOR THREADING, BUFFERING, and LINEAR INTERPOLATION
//
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//
//public class PoseProcessor {
//    private static final int BUFFER_SIZE = 10; // Size of the buffer
//    private static final int OUTPUT_RATE = 30; // Output rate in Hz
//    private final BlockingQueue<Pose> poseBuffer = new LinkedBlockingQueue<>(BUFFER_SIZE);
//    private volatile boolean running = true;
//    private Pose lastKnownPose = null; // To store the last known pose
//
//    public void start() {
//        Thread processingThread = new Thread(this::processPoses);
//        processingThread.start();
//    }
//
//    public void stop() {
//        running = false;
//    }
//
//    public void receivePose(Pose pose) {
//        if (poseBuffer.remainingCapacity() > 0) {
//            poseBuffer.offer(pose);
//            lastKnownPose = pose; // Update the last known pose
//        }
//    }
//
//    private void processPoses() {
//        while (running) {
//            try {
//                Pose currentPose = poseBuffer.poll(); // Get the latest pose
//                if (currentPose != null) {
//                    // Process the current pose
//                    sendToRobotArm(currentPose);
//                } else {
//                    // If no current pose, we can interpolate
//                    interpolateAndSend();
//                }
//
//                // Sleep to maintain the output rate
//                Thread.sleep(1000 / OUTPUT_RATE);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//
//    private void sendToRobotArm(Pose pose) {
//        // Send the pose to the robot arm
//        System.out.println("Sending to robot arm: " + pose);
//    }
//
//    private void interpolateAndSend() {
//        if (lastKnownPose != null) {
//            // Generate interpolated poses
//            for (double t = 0; t <= 1; t += 0.1) { // Adjust step size for more or fewer interpolated poses
//                Pose interpolatedPose = interpolate(lastKnownPose, lastKnownPose, t);
//                sendToRobotArm(interpolatedPose);
//            }
//        }
//    }
//
//    private Pose interpolate(Pose start, Pose end, double t) {
//        double x = start.x + t * (end.x - start.x);
//        double y = start.y + t * (end.y - start.y);
//        double z = start.z + t * (end.z - start.z);
//        double rx = start.rx + t * (end.rx - start.rx);
//        double ry = start.ry + t * (end.ry - start.ry);
//        double rz = start.rz + t * (end.rz - start.rz);
//        return new Pose(x, y, z, rx, ry, rz);
//    }
//
//    public static void main(String[] args) {
//        PoseProcessor processor = new PoseProcessor();
//        processor.start();
//
//        // Simulate receiving poses
//        for (int i = 0; i < 100; i++) {
//            processor.receivePose(new Pose(i, i + 1, i + 2, 0, 0, 0));
//            try {
//                Thread.sleep(16); // Simulate 60 FPS
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//
//        processor.stop();
//    }
//}
//
//
// */
