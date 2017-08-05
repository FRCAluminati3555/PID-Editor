package org.usfirst.frc.team3555.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class NetworkReader implements ITableListener {

	private NetworkTable table;
	
	public NetworkReader(){
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("10.1.90.2");
		
		table = NetworkTable.getTable("CANTalon Table");
		table.addTableListener(this);
	}
	
	public void update(){
		
	}

	@Override
	public void valueChanged(ITable table, String string, Object value, boolean arg3) {

	}

	public NetworkTable getTable(){return table;}
	public void setP(double p){table.putNumber("p", p);}
	public void setI(double i){table.putNumber("i", i);}
	public void setD(double d){table.putNumber("d", d);}
	public void setSetPoint(double point){table.putNumber("setPoint", point);}
	public void setEnabled(boolean enabled){table.putBoolean("Talon Enabled", enabled);}
	public void setPIDDataSyncRequested(boolean requested){table.putBoolean("toSync", requested);}
	
	public double getP(){return (double) table.getNumber("p", 0);}
	public double getI(){return (double) table.getNumber("i", 0);}
	public double getD(){return (double) table.getNumber("d", 0);}
	public double getValue(){return (double) table.getNumber("value", 0);}
	public double getSetPoint(){return (double) table.getNumber("setPoint", 0);}
	public boolean isTalonEnabled(){return table.getBoolean("Talon Enabled", false);}
	public boolean isRobotEnabled(){return table.getBoolean("Robot Enabled", false);}
	public boolean isPIDDataSyncRequested(){return table.getBoolean("toSync", false);}
}
