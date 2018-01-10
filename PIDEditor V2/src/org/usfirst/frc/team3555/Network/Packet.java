package org.usfirst.frc.team3555.Network;

import java.io.Serializable;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;

public class Packet implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	private Properties property;
	private Object value;
	private int id;
	
	public Packet(Controller controller, Properties property, Object value, int id) {
		this.controller = controller;
		this.property = property;
		this.value = value;
		this.id = id;
	}
	
	public Controller getController() { return controller; }
	public Properties getProperty() { return property; }
	public Object getValue() { return value; }
	public int getId() { return id; }
}
