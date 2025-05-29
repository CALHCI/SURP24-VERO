package controlPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

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

		int width = getWidth();
		int height = getHeight();
		double scaleX = (double) width / 100;
		double scaleY = (double) height / 100;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.scale(scaleX, scaleY);
		AffineTransform transform = AffineTransform.getScaleInstance(1, -1);
		transform.translate(0, -100); // Translate to flip around the bottom
		g2d.transform(transform);

		List<Point2D> pointsCopy = new ArrayList<>((Collection) Blackboard.getInstance().getPoints());

		// Loop to paint all points in the copied data structure
		for (Point2D p : pointsCopy) {
			g2d.fill(new Ellipse2D.Double(p.getX() - 1, p.getY() - 1, 2, 2));
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		repaint();
	}
	
}