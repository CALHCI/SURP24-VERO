package server;

import controlPanel.Blackboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketPublisher {
    private static SocketPublisher instance;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void connect(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to robot controller at" + ip + ":" + port);
    }

    public void disconnect() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (socket != null) socket.close();
        System.out.println("Disconnected");
    }

    public void sendCommand(String command) throws IOException {
        out.println(command);
        System.out.println("Sent: " + command);
    }

    public static SocketPublisher getInstance() {
        if (instance == null)
            instance = new SocketPublisher();
        return instance;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketPublisher publisher = SocketPublisher.getInstance();
        publisher.connect("localhost", 5000);
        publisher.sendCommand("help");
        Thread.sleep(1000);
        publisher.disconnect();
    }
}
