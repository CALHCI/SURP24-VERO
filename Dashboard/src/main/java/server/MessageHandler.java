package server;

import controlPanel.Blackboard;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.Socket;

class MessageHandler {
    private final Plane plane;
    private final SocketPublisher publisher;

    public MessageHandler(Plane plane) {
        this.plane = plane;
        this.publisher = new SocketPublisher();
        try {
            this.publisher.connect("localhost", 30001);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void handleMessage(String message) {
        // Convert vrData
        VRData vrData = new VRData(message);
        Blackboard.getInstance().add(new Point2D.Double(vrData.getX(), vrData.getY()));
        System.out.println(vrData);
        Pose output = plane.getBarycentric(vrData);
        String command = "movej(" + output + ", v=10, a=10)";
        try {
            publisher.sendCommand(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Sent: " + command);
    }
}
