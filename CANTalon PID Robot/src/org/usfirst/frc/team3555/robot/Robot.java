package org.usfirst.frc.team3555.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Robot extends SampleRobot {
	private NetworkTable table;
	private TalonMonitorManager monitor;
	private CANTalon talon;
//	private CANTalon talon;
//	
//	private double p, i, d;
//	private double setPoint;
	
    public Robot(){
    	table = NetworkTable.getTable("CANTalon Table");
    	
    	monitor = new TalonMonitorManager();
    	
    	talon = new CANTalon(0);
    	talon.configEncoderCodesPerRev(500);
    	monitor.add(new TalonMonitor("Talon", talon));
    	
//    	talon = new CANTalon(0);
//    	talon.changeControlMode(CANTalon.TalonControlMode.Speed);
//    	talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
//    	talon.configEncoderCodesPerRev(500);
//    	talon.setPID(p, i, d);
//    	talon.setSetpoint(setPoint);
//    	
//    	table.putNumber("p", 0);
//    	table.putNumber("i", 0);
//    	table.putNumber("d", 0);
//    	table.putNumber("setPoint", 0);
//    	table.putNumber("value", talon.getSpeed());
    }
    
//    public void updateCANTalon(){
//    	talon.setPID(p, i, d);
//    	talon.setSetpoint(setPoint);
//    }

    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
//        	table.putNumber("value", talon.getSpeed());
//        	
        	table.putBoolean("Robot Enabled", true);
        	
        	monitor.update(table);
//        	
//        	if(table.getBoolean("Talon Enabled", false) && !talon.isEnabled()) {
//        		talon.enable();
//        		updateCANTalon();
//        	} else if(!table.getBoolean("Talon Enabled", false) && talon.isEnabled())
//        		talon.disable();
//        	
//        	if(p != table.getNumber("p", 0) || i != table.getNumber("i", 0) || d != table.getNumber("d", 0) || setPoint != table.getNumber("setPoint", 0)){
//        		p = table.getNumber("p", 0);
//        		i = table.getNumber("i", 0);
//        		d = table.getNumber("d", 0);
//        		setPoint = table.getNumber("setPoint", 0);
//        		
//        		updateCANTalon();
//        	}
        	
            Timer.delay(0.005);		
        }
        
        table.putBoolean("Robot Enabled", false);
    }
	
//	NetworkTable table;
//	
//	public Robot(){
//		table = NetworkTable.getTable("datatable");
//	}
//	
//	public void operatorControl() {
//		double x = 0;
//		double y = 0;
//		while (isOperatorControl() && isEnabled()) {
//			Timer.delay(.25);
//			table.putNumber("X", x);
//			table.putNumber("Y", y);
//			x+=.05;
//			y+=1;
//        }
//	}
}
