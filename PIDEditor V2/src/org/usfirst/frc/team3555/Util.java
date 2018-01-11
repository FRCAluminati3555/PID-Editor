package org.usfirst.frc.team3555;

import java.io.DataInputStream;
import java.io.IOException;

import org.usfirst.frc.team3555.Network.Packets.BooleanPacket;
import org.usfirst.frc.team3555.Network.Packets.DoublePacket;
import org.usfirst.frc.team3555.Network.Packets.IntegerPacket;
import org.usfirst.frc.team3555.Network.Packets.Packet;

public class Util {
	public static enum Controller {
		CANTalon(0);
		
		private int value;
		private Controller(int value) {
			this.value = value;
		}
		
		public static Controller valueOf(int value) {
			for(Controller controller : values())
				if(controller.getValue() == value) return controller;
			return null;
		}
		
		public int getValue() { return value; }
	}
	
	public static enum Properties {
		Enabled(0),
		SetPoint(1), Velocity(2), Current(3), Voltage(4);
		
		private int value;
		private Properties(int value) {
			this.value = value;
		}
		
		public static Properties valueOf(int value) {
			for(Properties property : values())
				if(property.getValue() == value) return property;
			return null;
		}
		
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
		Properties property = Properties.valueOf(inputStream.readInt());
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
}
