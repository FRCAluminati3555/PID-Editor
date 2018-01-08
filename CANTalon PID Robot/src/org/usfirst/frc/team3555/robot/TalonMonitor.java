package org.usfirst.frc.team3555.robot;

import WrapperTalon.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * I have plans to create my own network transport system that wil replace the network table and be more suited to this project 
 * @author Sam
 */
public class TalonMonitor {
//	private CANTalon talon;
//	private CANTalon.FeedbackDevice feedbackDevice;
	
	private CANTalon talon;
	private int id;
	
	public TalonMonitor(CANTalon talon) {
		this.talon = talon;
		
		id = talon.getDeviceID();
	}
	
	public void update(NetworkTable table) {
		table.putString(id + " Name", talon.getName());
		table.putNumber(id + " Velocity", talon.getSelectedSensorVelocity(0));
		table.putNumber(id + " Position", talon.getSelectedSensorPosition(0));
		table.putNumber(id + " Voltage", talon.getBusVoltage());
		table.putNumber(id + " Current", talon.getOutputCurrent());
		table.putNumber(id + " Temperature", talon.getTemperature());
		table.putNumber(id + " BatteryVoltage", DriverStation.getInstance().getBatteryVoltage());
		
		if(table.getBoolean(id + " Enabled", false) && !talon.isEnabled()) {
			talon.setEnabled(true);
			checkPIDF(table);
		} else if(!table.getBoolean(id + " Enabled", false) && talon.isEnabled()) 
			talon.setEnabled(false);
		
		if(table.getBoolean(id + " ResetPosition", false)) {
			talon.setSensorPosition(0);
			table.putBoolean(id + " ResetPosition", false);
		}
		
		checkPIDF(table);
		
		int mode = (int) table.getNumber(id + " Mode", 0);
		if(mode != talon.getControlMode().value) 
			talon.setControlMode(mode);
		
		int device = (int) table.getNumber(id + " FeedbackDevice", -1);
		if(device != talon.getFeedbackDevice().value) {
			talon.setFeedbackDevice(device);
		}
		
		int tempCodesPerRev = (int) table.getNumber(id + " CodesPerRev", -1);
		if(tempCodesPerRev != -1 && tempCodesPerRev != talon.getSensorUnitsPerRotation()) {
			talon.setSensorUnitsPerRotation(tempCodesPerRev);
		}
	}
	
	public void checkPIDF(NetworkTable table) {
		double tempP = table.getNumber(id + " P", 0), tempI = table.getNumber(id + " I", 0), 
				tempD = table.getNumber(id + " D", 0), tempF = table.getNumber(id + " F", 0),
				tempSetPoint = table.getNumber(id + " SetPoint", 0);
		
		if(tempP != talon.getP()) 
			talon.setP(tempP);
		
		if(tempI != talon.getI()) 
			talon.setI(tempI);
		
		if(tempD != talon.getD()) 
			talon.setD(tempD);
		
		if(tempF != talon.getF()) 
			talon.setF(tempF);
		
		if(tempSetPoint != talon.getSetPoint()) 
			talon.set(tempSetPoint);
	}
}