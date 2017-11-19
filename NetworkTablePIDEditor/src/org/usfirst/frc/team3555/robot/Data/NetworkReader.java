package org.usfirst.frc.team3555.robot.Data;

import org.usfirst.frc.team3555.robot.Handler;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class NetworkReader implements ITableListener {

	private NetworkTable table;
	private Handler handler;
	
	public NetworkReader(Handler handler){
		this.handler = handler;
		
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("10.1.90.2");
//		NetworkTable.setIPAddress("127.0.0.1");
		
		table = NetworkTable.getTable("CANTalon Table");
		table.addTableListener(this);
		
//		System.out.println("Table Connecting...");
//		
//		while(!table.isConnected()) {
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		System.out.println("Table Connected!");
	}
	
	
	/**
	 * Format: "ID Property"
	 * Ex: "5 Voltage"
	 */
	@Override
	public void valueChanged(ITable table, String string, Object value, boolean didNotExist) {
		System.out.println("Talon Changed");
		
		String[] definition = string.split(" ");
		if(definition.length != 2) {
			System.err.println("Did not get Proper Definition");
			return;
		}
		
		if(definition[0].equals("Robot")) {
			DeviceInfo.RobotEnabled = (boolean) value;
			return;
		}
		
		int id = Integer.valueOf(definition[0]);
		String property = definition[1];
		
		if(handler.getDeviceInfoManager().getDevices().get(id) == null) {
			handler.getDeviceInfoManager().getDevices().put(id, new DeviceInfo());
			table.putBoolean(id + " Enabled", false);
		}
		
		if(property.equals("Name")) {
			handler.getDeviceInfoManager().getDevices().get(id).setName(((String) value) + " (" + id + ")");
		}
		
		else if(handler.getDeviceInfoManager().getDevices().get(id).getDoubles().containsKey(property)) {
			if(handler.getDeviceInfoManager().getDevices().get(id).getDoubles().put(property, (Double) value) == null) { 
				System.err.println("Invalid Property: " + property);
			}
		} else if(handler.getDeviceInfoManager().getDevices().get(id).getBooleans().containsKey(property)) {
			if(handler.getDeviceInfoManager().getDevices().get(id).getBooleans().put(property, (Boolean) value) == null) { 
				System.err.println("Invalid Property: " + property);
			}
		}
	}

	public NetworkTable getTable() {return table;}
}
