package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * The purpose of this class is to fake table input values to test the editor
 * @author Sam
 */
public class Table {

	private NetworkTable table;
	
	public Table() {
//		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("127.0.0.1");
		
		table = NetworkTable.getTable("CANTalon Table");
		table.putNumber("1 P", 10);
		table.putNumber("1 I", 0.05);
		table.putNumber("1 D", 5);
		table.putNumber("1 F", 7);
		
		table.putNumber("1 SetPoint", 1000);
		table.putNumber("1 Velocity", 800);
		table.putNumber("1 Position", 600.5);
		table.putNumber("1 Temperature", 92);
		table.putNumber("1 Voltage", 8);
		table.putNumber("1 Current", 2);
		table.putBoolean("1 Enabled", false);
		table.putBoolean("Robot Enabled", true);
		
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String[] args) {
		new Table();
	}
}
