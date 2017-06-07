package org.usfirst.frc.team3555.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Robot extends SampleRobot {
	private NetworkTable table;
	private CANTalon talon;
	
	private double p, i, d;
	private double setPoint;
	
    public Robot(){
    	table = NetworkTable.getTable("CANTalon Table");
    	
    	talon = new CANTalon(0);
    	talon.changeControlMode(CANTalon.TalonControlMode.Speed);
    	talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	talon.configEncoderCodesPerRev(500);
    	talon.setPID(p, i, d);
    	talon.setSetpoint(setPoint);
    	
    	talon.enableControl();
    }
    
    public void updateCANTalon(){
    	talon.setPID(p, i, d);
    	talon.setSetpoint(setPoint);
    }

    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
        	table.putNumber("value", talon.getSpeed());
        	
        	if(p != table.getNumber("p", 0) || i != table.getNumber("i", 0) || d != table.getNumber("d", 0) || setPoint != table.getNumber("setPoint", 0)){
        		p = table.getNumber("p", 0);
        		i = table.getNumber("i", 0);
        		d = table.getNumber("d", 0);
        		setPoint = table.getNumber("setPoint", 0);
        		
        		updateCANTalon();
        	}
        	
            Timer.delay(0.005);		
        }
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
