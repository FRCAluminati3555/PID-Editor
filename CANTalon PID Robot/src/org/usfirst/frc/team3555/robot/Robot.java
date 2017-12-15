package org.usfirst.frc.team3555.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Robot extends SampleRobot {
	private NetworkTable table;
	private TalonMonitorManager monitor;
	private CANTalon talonLeftFront;
	private CANTalon talonLeftBack;
	
	private CANTalon talonRightBack;
	private CANTalon talonRightFront;
	
//	private CANTalon climber1, climber2;
	
//	leftRear = new CANTalon(43);
//	leftFront = new CANTalon(41); 
//	
//	rightRear = new CANTalon(44);
//	rightFront = new CANTalon(42);
	
    public Robot() { 
    	table = NetworkTable.getTable("CANTalon Table");
    	
    	monitor = new TalonMonitorManager();
    	monitor.add(new TalonMonitor("Servo", new CANTalon(0)));
    	
//    	talonLeftFront = new CANTalon(41);
//    	monitor.add(new TalonMonitor("Left Front", talonLeftFront));
//    	
//    	talonLeftBack = new CANTalon(43);
//    	talonLeftBack.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//    	talonLeftBack.configEncoderCodesPerRev(360);
//    	monitor.add(new TalonMonitor("Left Back", talonLeftBack));
//    	
//    	talonRightFront = new CANTalon(42);
//    	monitor.add(new TalonMonitor("Right Front", talonRightFront));
//
//    	talonRightBack = new CANTalon(44);
//    	talonRightBack.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
//    	talonRightBack.configEncoderCodesPerRev(360);
//    	monitor.add(new TalonMonitor("Right Back", talonRightBack));
    	
//    	climber1 = new CANTalon(46);
//    	monitor.add(new TalonMonitor(climber1));
//    	
//    	climber2 = new CANTalon(47);
//    	monitor.add(new TalonMonitor(climber2));
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