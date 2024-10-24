package controlPanel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * CobotPanel class to draw the robot arm and animate it.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class DrawPanel extends JPanel implements PropertyChangeListener {
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// loop to paint all points in a data structure
		for (Point p : Blackboard.getInstance().getPoints()) {
			g.fillOval(p.x, p.y, 10, 10);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		repaint();
	}
	
}