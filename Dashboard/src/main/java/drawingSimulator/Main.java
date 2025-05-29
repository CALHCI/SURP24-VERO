package drawingSimulator;

import javax.swing.*;
import java.awt.BorderLayout;

public class Main {
	public static void main(String[] args) {
		String broker = "tcp://test.mosquitto.org:1883";
		MqttPublisher mqttPublisher = new MqttPublisher(broker);
		mqttPublisher.connect();

		JFrame frame = new JFrame("Drawing Panel");
		DrawingPanel panel = new DrawingPanel(mqttPublisher);

		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(e -> panel.clearCanvas());

		JLabel freqLabel = new JLabel("Draw Frequency (points/sec):");

		JSlider freqSlider = new JSlider(1, 26, 5);
		freqSlider.addChangeListener(e -> panel.setFrequency(freqSlider.getValue()));
		freqSlider.setMajorTickSpacing(5);
		freqSlider.setMinorTickSpacing(1);
		freqSlider.setPaintTicks(true);
		freqSlider.setPaintLabels(true);

		JPanel sliderPanel = new JPanel(new BorderLayout(5, 0));
		sliderPanel.add(freqLabel, BorderLayout.WEST);
		sliderPanel.add(freqSlider, BorderLayout.CENTER);

		JPanel controlPanel = new JPanel(new BorderLayout());
		controlPanel.add(sliderPanel, BorderLayout.NORTH);
		controlPanel.add(clearButton, BorderLayout.SOUTH);

		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		frame.add(clearButton, BorderLayout.NORTH);
		frame.add(controlPanel, BorderLayout.SOUTH);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
