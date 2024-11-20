package controlPanel;

import java.awt.geom.Point2D;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Blackboard class to store shared data between classes.
 * The Blackboard class is a singleton, meaning that only one instance of it can exist.
 *
 * @author Javier Gonzalez-Sanchez
 * @version 2.0
 */
public class Blackboard extends PropertyChangeSupport {
	
	private static Blackboard instance;
	
	private List<Point2D> points;
	
	private Blackboard() {
		super(new Object());
		points = new ArrayList<>();
	}
	
	public static Blackboard getInstance() {
		if (instance == null)
			instance = new Blackboard();
		return instance;
	}
	
	public void add(Point2D p) {
		points.add(p);
		firePropertyChange("points", null, points);
	}

	public void clear() {
		points.clear();
		firePropertyChange("points", null, points);
	}

	public Iterable<? extends Point2D> getPoints() {
		return points;
	}
	
}