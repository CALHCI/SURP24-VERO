package server;

import controlPanel.Blackboard;

import java.awt.geom.Point2D;

class MessageHandler {
    private final Plane plane;

    public MessageHandler(Plane plane) {
        this.plane = plane;
    }



    public void handleMessage(String message) {
        // Convert vrData
        VRData vrData = new VRData(message);
        Blackboard.getInstance().add(new Point2D.Double(vrData.getX(), vrData.getY()));
        System.out.println(vrData);
        Pose output = plane.getBarycentric(vrData);
        System.out.println(output);
    }
}
