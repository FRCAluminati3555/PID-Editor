package org.usfirst.frc.team3555;

public class Util {
	public static enum Controller {
		CANTalon(0);
		
		private int value;
		private Controller(int value) {
			this.value = value;
		}
		
		public Controller valueOf(int value) {
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
		
		public Properties valueOf(int value) {
			for(Properties property : values())
				if(property.getValue() == value) return property;
			return null;
		}
		
		public int getValue() { return value; }
	}
}
