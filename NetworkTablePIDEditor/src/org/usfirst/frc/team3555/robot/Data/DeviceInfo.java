package org.usfirst.frc.team3555.robot.Data;

import java.util.HashMap;

import com.ctre.CANTalon;

public class DeviceInfo {
	public static boolean RobotEnabled;
	
	private HashMap<String, Double> doubles;
	private HashMap<String, Boolean> booleans;
	private HashMap<String, Integer> integers;
	
	private CANTalon.TalonControlMode controlMode;
	private CANTalon.FeedbackDevice feedbackDevice;
	
	private String name;
	
	public DeviceInfo() {
		doubles = new HashMap<>();
		booleans = new HashMap<>();
		integers = new HashMap<>();
		
		integers.put("CodesPerRev", 0);
		
		doubles.put("P", 0.0);
		doubles.put("I", 0.0);
		doubles.put("D", 0.0);
		doubles.put("F", 0.0);
		
		doubles.put("SetPoint", 0.0);
		doubles.put("BatteryVoltage", 0.0);
		doubles.put("Voltage", 0.0);
		doubles.put("Velocity", 0.0);
		doubles.put("Position", 0.0);
		doubles.put("Current", 0.0);
		doubles.put("Temperature", 0.0);
		doubles.put("AnalogInPosition", 0.0);
		
		doubles.put("LowerBound", 0.0);
		doubles.put("UpperBound", 0.0);
		
		booleans.put("Enabled", false);
		booleans.put("ResetPosition", false);
	}
	
	public String toString() {
		return "--------------\nName: " + name + ",\nMode: " + controlMode +  ",\nDoubles: " + doubles.toString() + ",\nBooleans: " + booleans.toString() + "\n--------------";
	}

	public HashMap<String, Double> getDoubles() { return doubles; }
	public HashMap<String, Boolean> getBooleans() { return booleans; }
	public HashMap<String, Integer> getIntegers() { return integers; }
	
	public double getValue() {
		if(controlMode == CANTalon.TalonControlMode.Position)
			return doubles.get("Position");
		if(controlMode == CANTalon.TalonControlMode.Speed)
			return doubles.get("Velocity");
		if(controlMode == CANTalon.TalonControlMode.Current)
			return doubles.get("Current");
		if(controlMode == CANTalon.TalonControlMode.Voltage)
			return doubles.get("Voltage");
		return 0;
	}

	public CANTalon.TalonControlMode getControlMode() { return controlMode; }
	public void setControlMode(CANTalon.TalonControlMode controlMode) { this.controlMode = controlMode; }
	
	public CANTalon.FeedbackDevice getFeedbackDevice() { return feedbackDevice; }
	public void setControlMode(CANTalon.FeedbackDevice feedbackDevice) { this.feedbackDevice = feedbackDevice; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
}