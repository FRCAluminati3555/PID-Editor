package org.usfirst.frc.team3555.robot.Data;

import java.util.HashMap;

import org.usfirst.frc.team3555.robot.Handler;

import com.ctre.CANTalon;

public class DeviceInfoManager {
	private Handler handler;
	private HashMap<Integer, DeviceInfo> devices;
	
	public DeviceInfoManager(Handler handler) {
		this.handler = handler;
		
		devices = new HashMap<>();
	}

	public double getDouble(String property, int id){return devices.get(id).getDoubles().get(property);}
	public boolean getBoolean(String property, int id){return devices.get(id).getBooleans().get(property);}
	
	public void setDouble(String property, double value, int id) {
		if(devices.get(id).getDoubles().containsKey(property)) { 
			devices.get(id).getDoubles().put(property, value);
			handler.getReader().getTable().putNumber(id + " " + property, value);
		}
	}
	
	public void setBoolean(String property, boolean value, int id) {
		if(devices.get(id).getBooleans().containsKey(property)) { 
			devices.get(id).getBooleans().put(property, value);
			handler.getReader().getTable().putBoolean(id + " " + property, value);
		}
	}
	
	public void setControlMode(CANTalon.TalonControlMode mode, int id) {
		devices.get(id).setControlMode(mode);
		handler.getReader().getTable().putNumber(id + " Mode", mode.value);
	}
	
	public HashMap<Integer, DeviceInfo> getDevices(){return devices;}
}
