package controlPanel;

import java.awt.*;
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
	
	private List<Point> points;
	
	private Blackboard() {
		super(new Object());
		points = new ArrayList<>();
	}
	
	public static Blackboard getInstance() {
		if (instance == null)
			instance = new Blackboard();
		return instance;
	}
	
	public void add(Point p) {
		points.add(p);
		firePropertyChange("points", null, points);
	}
	
	public Iterable<? extends Point> getPoints() {
		return points;
	}
	
}