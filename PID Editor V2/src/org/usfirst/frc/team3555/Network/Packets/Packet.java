package org.usfirst.frc.team3555.Network.Packets;

import java.io.DataOutputStream;
import java.io.IOException;

import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;

public abstract class Packet {
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
	
	public void write(DataOutputStream output) throws IOException {
		output.writeInt(controller.getValue());
		output.writeInt(property.getValue());
		output.writeInt(id);
		
		if(value instanceof Boolean) {
			output.writeByte(0);
			output.writeBoolean((boolean) value);
		} else if(value instanceof Integer) {
			output.writeByte(1);
			output.writeInt((int) value);
		} else if(value instanceof Double) {
			output.writeByte(2);
			output.writeDouble((double) value);
		}
	}
	
	public String toString() { 
		return "Packet -> Controller: " + controller + ", Property: " + property + ", Value: " + value + ", Id: " + id; 
	}
	
	public Controller getController() { return controller; }
	public Properties getProperty() { return property; }
	public Object getValue() { return value; }
	public int getId() { return id; }
}
