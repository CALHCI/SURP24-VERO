import javax.websocket.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.util.Scanner;

public class Main {
	
	public static class VRData {
		private final double x;
		private final double y;
		
		public VRData(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public static class Pose {
		private final double x;
		private final double y;
		private final double z;
		private final double rx;
		private final double ry;
		private final double rz;
		
		public Pose(double x, double y, double z, double rx, double ry, double rz) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.rx = rx;
			this.ry = ry;
			this.rz = rz;
		}

//        public Pose(byte[] responseInBytes) {
//            // TODO: Parse bytes to create a Pose
//        }
		
		@Override
		public String toString() {
			return String.format("Pose[x=%.2f, y=%.2f, z=%.2f, rx=%.2f, ry=%.2f, rz=%.2f]", x, y, z, rx, ry, rz);
		}
	}
	
	public static class Plane {
		private final Pose origin;
		private final Pose pX;
		private final Pose pY;
		
		public Plane(Pose origin, Pose pX, Pose pY) {
			this.origin = origin;
			this.pX = pX;
			this.pY = pY;
		}
		
		public Pose getBarycentric(VRData vrData) {
			double u = vrData.x / 100;
			double v = vrData.y / 100;
			
			// Apply xVector * u and yVector * v to the origin
			double x = origin.x + u * (pX.x - origin.x) + v * (pY.x - origin.x);
			double y = origin.y + u * (pX.y - origin.y) + v * (pY.y - origin.y);
			double z = origin.z + u * (pX.z - origin.z) + v * (pY.z - origin.z);
			
			double rx = (origin.rx + pX.rx + pY.rx) / 3;
			double ry = (origin.ry + pX.ry + pY.ry) / 3;
			double rz = (origin.rz + pX.rz + pY.rz) / 3;
			
			return new Pose(x, y, z, rx, ry, rz);
		}
	}
	
	public static class URCommunicationHandler {
		private final Socket socket;
		private final OutputStream out;
		private final InputStream in;
		
		public URCommunicationHandler(String address, int port) throws IOException {
			this.socket = new Socket(address, port);
			this.out = socket.getOutputStream();
			this.in = socket.getInputStream();
		}
		
		public Pose capturePose() throws IOException, InterruptedException {
			waitForUserInput();
			out.write("\n".getBytes());
			out.flush();
			Thread.sleep(1000);
			byte[] responseInBytes = readInputStream(in);
			//return new Pose(responseInBytes);
			return new Pose(0, 0, 0, 0, 0, 0); // Dummy pose
		}
		
		public void close() throws IOException {
			socket.close();
		}
		
		private void waitForUserInput() throws IOException {
			System.out.println("Press space to capture pose...");
			Scanner scanner = new Scanner(System.in);
			while (!scanner.nextLine().equals(" ")) {
				// wait for space input
			}
		}
		
		private byte[] readInputStream(InputStream inputStream) throws IOException {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int bytesRead;
			while (inputStream.available() > 0 && (bytesRead = inputStream.read()) != -1) {
				buffer.write(bytesRead);
			}
			return buffer.toByteArray();
		}
	}
	
	@ClientEndpoint
	public static class WebSocketClientHandler {
		private Session userSession;
		private static Plane plane;
		
		public WebSocketClientHandler(URI endpointURI, Plane plane) {
			WebSocketClientHandler.plane = plane;
			try {
				WebSocketContainer container = ContainerProvider.getWebSocketContainer();
				container.connectToServer(this, endpointURI);
			} catch (Exception e) {
				System.err.println("Failed to connect to WebSocket: " + e.getMessage());
			}
		}
		
		@OnOpen
		public void onOpen(Session userSession) {
			this.userSession = userSession;
			System.out.println("Connected to WebSocket");
		}
		
		@OnMessage
		public void onMessage(String message) {
			String[] parts = message.split(",");
			if (parts.length == 2) {
				try {
					double x = Double.parseDouble(parts[0]);
					double y = Double.parseDouble(parts[1]);
					VRData vrData = new VRData(x, y);
					Pose output = plane.getBarycentric(vrData);
					System.out.println(output);
				} catch (NumberFormatException e) {
					System.err.println("Invalid number format in message: " + message);
				}
			} else {
				System.err.println("Invalid message format: " + message);
			}
		}
		
		@OnClose
		public void onClose(Session userSession, CloseReason reason) {
			System.out.println("Session closed: " + reason);
		}
		
		@OnError
		public void onError(Session userSession, Throwable throwable) {
			System.err.println("WebSocket error: " + throwable.getMessage());
		}
		
		public void close() {
			try {
				if (userSession != null && userSession.isOpen()) {
					userSession.close();
				}
			} catch (IOException e) {
				System.err.println("Error closing WebSocket session: " + e.getMessage());
			}
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		// UR communication handler
		URCommunicationHandler urHandler = new URCommunicationHandler("129.65.88.247", 30001);
		
		// Capture 3 poses to create a plane
		Pose origin = urHandler.capturePose();
		Pose pX = urHandler.capturePose();
		Pose pY = urHandler.capturePose();
		Plane plane = new Plane(origin, pX, pY);
		
		// VR WebSocket client handler
		URI uri = URI.create("ws://localhost/vr");
		WebSocketClientHandler client = new WebSocketClientHandler(uri, plane);
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Shutting down...");
			client.close();
			System.out.println("Client closed.");
			try {
				urHandler.close();
			} catch (IOException e) {
				System.err.println("Error closing UR connection: " + e.getMessage());
			}
		}));
	}
}


/* GENERATED BOILERPLATE FOR THREADING, BUFFERING, and LINEAR INTERPOLATION

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PoseProcessor {
    private static final int BUFFER_SIZE = 10; // Size of the buffer
    private static final int OUTPUT_RATE = 30; // Output rate in Hz
    private final BlockingQueue<Pose> poseBuffer = new LinkedBlockingQueue<>(BUFFER_SIZE);
    private volatile boolean running = true;
    private Pose lastKnownPose = null; // To store the last known pose

    public void start() {
        Thread processingThread = new Thread(this::processPoses);
        processingThread.start();
    }

    public void stop() {
        running = false;
    }

    public void receivePose(Pose pose) {
        if (poseBuffer.remainingCapacity() > 0) {
            poseBuffer.offer(pose);
            lastKnownPose = pose; // Update the last known pose
        }
    }

    private void processPoses() {
        while (running) {
            try {
                Pose currentPose = poseBuffer.poll(); // Get the latest pose
                if (currentPose != null) {
                    // Process the current pose
                    sendToRobotArm(currentPose);
                } else {
                    // If no current pose, we can interpolate
                    interpolateAndSend();
                }

                // Sleep to maintain the output rate
                Thread.sleep(1000 / OUTPUT_RATE);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void sendToRobotArm(Pose pose) {
        // Send the pose to the robot arm
        System.out.println("Sending to robot arm: " + pose);
    }

    private void interpolateAndSend() {
        if (lastKnownPose != null) {
            // Generate interpolated poses
            for (double t = 0; t <= 1; t += 0.1) { // Adjust step size for more or fewer interpolated poses
                Pose interpolatedPose = interpolate(lastKnownPose, lastKnownPose, t);
                sendToRobotArm(interpolatedPose);
            }
        }
    }

    private Pose interpolate(Pose start, Pose end, double t) {
        double x = start.x + t * (end.x - start.x);
        double y = start.y + t * (end.y - start.y);
        double z = start.z + t * (end.z - start.z);
        double rx = start.rx + t * (end.rx - start.rx);
        double ry = start.ry + t * (end.ry - start.ry);
        double rz = start.rz + t * (end.rz - start.rz);
        return new Pose(x, y, z, rx, ry, rz);
    }

    public static void main(String[] args) {
        PoseProcessor processor = new PoseProcessor();
        processor.start();

        // Simulate receiving poses
        for (int i = 0; i < 100; i++) {
            processor.receivePose(new Pose(i, i + 1, i + 2, 0, 0, 0));
            try {
                Thread.sleep(16); // Simulate 60 FPS
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        processor.stop();
    }
}


 */