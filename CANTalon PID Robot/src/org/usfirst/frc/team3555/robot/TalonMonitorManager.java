package org.usfirst.frc.team3555.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class TalonMonitorManager {
	private ArrayList<TalonMonitor> monitors;
	
	public TalonMonitorManager() {
		monitors = new ArrayList<>();
	}
	
	public void update(NetworkTable table) {
		for(TalonMonitor m : monitors) {
			m.update(table);
		}
	}
	
	public void add(TalonMonitor monitor) {
		monitors.add(monitor);
	}
}
