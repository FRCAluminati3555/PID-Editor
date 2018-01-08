package org.usfirst.frc.team3555.robot;

import WrapperTalon.CANTalon;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Robot extends SampleRobot {
	private NetworkTable table;
	private TalonMonitorManager monitor;
	
    public Robot() { 
    	table = NetworkTable.getTable("CANTalon Table");
    	
    	monitor = new TalonMonitorManager();
    	monitor.add(new TalonMonitor(new CANTalon("Talon", 0)));
    }
    
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
        	table.putBoolean("Robot Enabled", true);
        	monitor.update(table);

        	Timer.delay(0.005);		
        }
        
        table.putBoolean("Robot Enabled", false);
    }
}