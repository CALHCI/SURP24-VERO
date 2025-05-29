package drawingSimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import java.util.TimerTask;

public class DrawingPanel extends JPanel {

    private final MqttPublisher mqttPublisher;
    private final String topic = "vr";
    private volatile boolean drawing = false;
    private int frequency = 4;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> drawingTask;
    private double currentX = -1, currentY = -1;

    private final List<List<Point2D.Double>> strokes = new ArrayList<>();
    private List<Point2D.Double> currentStroke = null;

    public DrawingPanel(MqttPublisher publisher) {
        this.mqttPublisher = publisher;
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                drawing = true;
                updateCoordinates(e);
                startPublishing();
                currentStroke = new ArrayList<>();
                synchronized (strokes) {
                    strokes.add(currentStroke);
                }
            }

            public void mouseReleased(MouseEvent e) {
                drawing = false;
                updateCoordinates(e);
                double xSnapshot = currentX;
                double ySnapshot = currentY;
                new Thread(() -> mqttPublisher.publish(topic, String.format("%.2f,%.2f", xSnapshot, ySnapshot))).start();
                synchronized (strokes) {
                    if (currentStroke != null) {
                        currentStroke.add(new Point2D.Double(xSnapshot, ySnapshot));
                    }
                }

                repaint();
                stopPublishing();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (drawing) {
                    drawing = false;
                    stopPublishing();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                updateCoordinates(e);
            }
        });
    }

    private void updateCoordinates(MouseEvent e) {
        int width = getWidth();
        int height = getHeight();
        currentX = (e.getX() / (double) width) * 100.0;
        currentY = (1.0 - (e.getY() / (double) height)) * 100.0;
        repaint();
    }

    private void startPublishing() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        long interval = 1000L / frequency;

        drawingTask = scheduler.scheduleAtFixedRate(() -> {
            if (drawing && currentX >= 0 && currentY >= 0) {
                double xSnapshot = currentX;
                double ySnapshot = currentY;
                new Thread(() -> mqttPublisher.publish(topic, String.format("%.2f,%.2f", xSnapshot, ySnapshot))).start();
                synchronized (strokes) {
                    if (currentStroke != null) {
                        currentStroke.add(new Point2D.Double(xSnapshot, ySnapshot));
                    }
                }

                SwingUtilities.invokeLater(this::repaint);
            }
        }, 0, interval, TimeUnit.MILLISECONDS);
    }


    private void stopPublishing() {
        if (drawingTask != null) {
            drawingTask.cancel(false);
            drawingTask = null;
        }
        if (scheduler != null) {
            scheduler.shutdown();
            scheduler = null;
        }
    }


    public void setFrequency(int freq) {
        this.frequency = freq;
        if (drawing) {
            stopPublishing();
            startPublishing();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        synchronized (strokes) {
            for (List<Point2D.Double> stroke : strokes) {
                Point prev = null;
                for (Point2D.Double pt : stroke) {
                    int x = (int) ((pt.x / 100.0) * getWidth());
                    int y = (int) (((100.0 - pt.y) / 100.0) * getHeight());

                    g2d.setColor(Color.BLUE);
                    g2d.fillOval(x - 2, y - 2, 5, 5);

                    if (prev != null) {
                        g2d.setColor(new Color(255, 0, 0, 128));
                        g2d.setStroke(new BasicStroke(2));
                        g2d.drawLine(prev.x, prev.y, x, y);
                    }

                    prev = new Point(x, y);
                }
            }
        }

        g2d.dispose();
    }

    public void clearCanvas() {
        synchronized (strokes) {
            strokes.clear();
        }
        repaint();
    }
}
