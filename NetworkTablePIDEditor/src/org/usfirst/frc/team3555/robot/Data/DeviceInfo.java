package org.usfirst.frc.team3555.robot.Data;

import java.util.HashMap;

import com.ctre.CANTalon;

public class DeviceInfo {
	public static boolean RobotEnabled;
	
	private HashMap<String, Double> doubles;
	private HashMap<String, Boolean> booleans;
	
	private CANTalon.TalonControlMode controlMode;
	
	public DeviceInfo() {
		doubles = new HashMap<>();
		booleans = new HashMap<>();
		
		doubles.put("P", 0.0);
		doubles.put("I", 0.0);
		doubles.put("D", 0.0);
		doubles.put("F", 0.0);
		
		doubles.put("SetPoint", 0.0);
		doubles.put("Voltage", 0.0);
		doubles.put("Velocity", 0.0);
		doubles.put("Position", 0.0);
		doubles.put("Current", 0.0);
		doubles.put("Temperature", 0.0);
		
		booleans.put("Enabled", false);
	}
	
	public String toString() {
		return "Mode: " + controlMode +  "\nDoubles: " + doubles.toString() + "\nBooleans: " + booleans.toString();
	}

	public HashMap<String, Double> getDoubles(){return doubles;}
	public HashMap<String, Boolean> getBooleans(){return booleans;}

	public CANTalon.TalonControlMode getControlMode(){return controlMode;}
	public void setControlMode(CANTalon.TalonControlMode controlMode){this.controlMode = controlMode;}
}
