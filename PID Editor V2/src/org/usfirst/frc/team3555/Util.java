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
		Enabled("Enabled"), Name("Name"),
		SetPoint("Set Point"), 
		
		RotationalVelocity("Rotational Velocity"), LinearVelocity("LinearVelocity"),  
		RotationalAcceleration("Rotational Acceleration"), LinearAcceleration("Linear Acceleration"),
	
		RotationalPosition("Rotational Position"), LinearPosition("Linear Position"), 
		AnalogInPosition("Analog Position"), AnalogInVelocity("Analog Velocity"),
		
		Temperature("Temperature"), Current("Current"), Voltage("Voltage"),
		
		P("P"), I("I"), D("D"), F("F"),
		FeedBackSensor("Sensor"), SensorUnitsPerRotation("Sensor Units"), DistancePerRotation("Distance Per Rotation"),

		ControlMode("Mode"),
		EnableLimitSwitch("Limit Switch Enable"), 
		ForwardLimitSwtichSource("Forward Limit Switch Source"), ForwardLimitSwitchNormal("Forward Limit Switch Normal"),
		ReverseLimitSwtichSource("Reverse Limit Switch Source"), ReverseLimitSwitchNormal("Reverse Limit Switch Normal"),
		
		EnableSoftLimit("Soft Limit Enable"), SoftLimitForward("Soft Limit Forward"), SoftLimitReverse("Soft Limit Reverse"),
		
		ResetSensorPosition("Reset Postion");
		
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
		
		private static int id;
		private static int genId() { return id++; }
		
		private String name;
		private int value;
		private Properties(String name) {
			this.name = name;
			this.value = genId();
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
