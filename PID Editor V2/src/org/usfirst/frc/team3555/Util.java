package org.usfirst.frc.team3555;

import java.io.DataInputStream;
import java.io.IOException;

import org.usfirst.frc.team3555.Network.Packets.BooleanPacket;
import org.usfirst.frc.team3555.Network.Packets.DoublePacket;
import org.usfirst.frc.team3555.Network.Packets.IntegerPacket;
import org.usfirst.frc.team3555.Network.Packets.Packet;

import javafx.scene.control.TextField;

public class Util {
	public static enum Controller {
		DriverStation(0), CANTalon(1);
		
		public static Controller valueOf(int value) {
			for(Controller controller : values())
				if(controller.getValue() == value) return controller;
			return null;
		}
		
		private int value;
		private Controller(int value) {
			this.value = value;
		}
		
		public int getValue() { return value; }
	}
	
	public static enum Properties {
		Enabled("Enabled", 0), Name("Name", 1),
		SetPoint("Set Point", 2), Velocity("Velocity", 3), Current("Current", 4), Voltage("Voltage", 5), Position("Position", 6), Temperature("Temperature", 7),
		P("P", 7), I("I", 8), D("D", 9), F("F", 10),
		FeedBackSensor("Sensor", 11), SensorUnitsPerRotation("Sensor Units", 12),
		ControlMode("Mode", 14),
		ResetSensorPosition("Reset Postion", 14);
		
		public static Properties getProperty(int value) {
			for(Properties property : values())
				if(property.getValue() == value) return property;
			return null;
		}
		
		public static Properties getProperty(String name) {
			for(Properties property : values())
				if(property.toString().equals(name)) return property;
			return null;
		}
		
		private String name;
		private int value;
		private Properties(String name, int value) {
			this.value = value;
			this.name = name;
		}
		
		public String toString() { return name; }
		public int getValue() { return value; }
	}
	
	public static Packet genPacket(Controller controller, Properties property, Object value, int id) {
		if(value instanceof Boolean)
			return new BooleanPacket(controller, property, (boolean) value, id);
		if(value instanceof Integer)
			return new IntegerPacket(controller, property, (int) value, id);
		if(value instanceof Double) 
			return new DoublePacket(controller, property, (double) value, id);
		return null;
	}
	
	public static Packet genPacket(DataInputStream inputStream) throws IOException {
		Controller controller = Controller.valueOf(inputStream.readInt());
		Properties property = Properties.getProperty(inputStream.readInt());
		int id = inputStream.readInt();

		switch(inputStream.readByte()) {
			case 0:
				return new BooleanPacket(controller, property, inputStream.readBoolean(), id);
			case 1:
				return new IntegerPacket(controller, property, inputStream.readInt(), id);
			case 2:
				return new DoublePacket(controller, property, inputStream.readDouble(), id);
		}
		
		return null;
	}
	
	public static double getValue(TextField from) {
		double temp;
		
		try {
			temp = Double.valueOf(from.getText());
		} catch(NumberFormatException ex) {
			temp = 0;
		}
		
		return temp;
	}
}
