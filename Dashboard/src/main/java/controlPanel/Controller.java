package controlPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Controller class that listens for button clicks and calls the appropriate methods in the Client class.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class Controller implements ActionListener {
	
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	private Subscriber subscriber;

//	private double[] originValues = {-0.18075, -0.17714, 0.59784, -0.74074, -1.74977, 1.74};
//	private double[] deltaXValues = {-0.43281, -0.05334, 0.59784, -0.74074, -1.74977, 1.74};
//	private double[] deltaYValues = {-0.18075, -0.17714, 0.84278, -0.74074, -1.74977, 1.74};

	private double[] originValues = {-0.32516, 0.39702, 0.28782, 1.35707, -1.0108, -1.06756};
	private double[] deltaXValues = {-0.52433, -0.23842, 0.2957, 1.33333, -0.9504, -1.14978};
	private double[] deltaYValues = {-0.32977, 0.42191, 0.85344, 1.15136, -1.12194, -0.71011};

	private final Main mainFrame;

	public Controller(Main mainFrame) {
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Start server")) {
			startClient();
		} else if (e.getActionCommand().equals("Stop server")) {
			stopClient();
		} else if (e.getActionCommand().equals("Exit")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("Configure")) {
			showSettingsDialog();
		}
	}
	
	private void startClient() {
		if (subscriber != null && subscriber.isRunning()) {
			JOptionPane.showMessageDialog(null, "Already connected to broker", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			logger.info("Starting subscriber");
			subscriber = new  Subscriber("tcp://localhost:1883", "vr", originValues, deltaXValues, deltaYValues);
			Thread subscriberThread = new Thread(subscriber);
			subscriberThread.start();
			logger.info(String.format("Using poses: \n\t{%s}\n\t{%s}\n\t{%s}",
					Arrays.toString(originValues),
					Arrays.toString(deltaXValues),
					Arrays.toString(deltaYValues)
			));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!subscriber.isRunning()) {
				JOptionPane.showMessageDialog(null, "Could not connect to broker", "Error", JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Connected to broker", "Success", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	private void stopClient() {
		if (subscriber != null && subscriber.isRunning()) {
			logger.info("Stopping subscriber");
			subscriber.stop();
			Blackboard.getInstance().clear();
			JOptionPane.showMessageDialog(null, "Disconnected from broker", "Success", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Not connected to broker", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void showSettingsDialog() {
		JDialog settingsDialog = new JDialog(mainFrame, "Settings", true);
		settingsDialog.setLayout(new BorderLayout());

		// Create a panel for the input fields
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(3, 7)); // 4 rows, 6 columns

		// Create input fields for Origin
		inputPanel.add(new JLabel("Origin"));
		JTextField[] originFields = new JTextField[6]; // Array for Origin fields
		for (int i = 0; i < originFields.length; i++) {
			originFields[i] = new JTextField();
			originFields[i].setText(String.valueOf(originValues[i])); // Set default value
			inputPanel.add(originFields[i]);
		}

		// Create input fields for "Δx"
		inputPanel.add(new JLabel("Δx"));
		JTextField[] deltaXFields = new JTextField[6]; // Array for Δx fields
		for (int i = 0; i < deltaXFields.length; i++) {
			deltaXFields[i] = new JTextField();
			deltaXFields[i].setText(String.valueOf(deltaXValues[i])); // Set default value
			inputPanel.add(deltaXFields[i]);
		}

		// Create input fields for "Δy"
		inputPanel.add(new JLabel("Δy"));
		JTextField[] deltaYFields = new JTextField[6]; // Array for Δy fields
		for (int i = 0; i < deltaYFields.length; i++) {
			deltaYFields[i] = new JTextField();
			deltaYFields[i].setText(String.valueOf(deltaYValues[i])); // Set default value
			inputPanel.add(deltaYFields[i]);
		}

		settingsDialog.add(inputPanel, BorderLayout.CENTER);

		// Submit button
		JButton submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// Convert input values to decimals
					for (int i = 0; i < originFields.length; i++) {
						originValues[i] = Double.parseDouble(originFields[i].getText());
					}

					for (int i = 0; i < deltaXFields.length; i++) {
						deltaXValues[i] = Double.parseDouble(deltaXFields[i].getText());
					}

					for (int i = 0; i < deltaYFields.length; i++) {
						deltaYValues[i] = Double.parseDouble(deltaYFields[i].getText());
					}


				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(settingsDialog, "Please enter valid decimal numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
				}

				settingsDialog.dispose(); // Close the dialog
			}
		});

		settingsDialog.add(submitButton, BorderLayout.SOUTH);

		settingsDialog.setSize(600, 300);
		settingsDialog.setLocationRelativeTo(mainFrame);
		settingsDialog.setVisible(true);
	}



}