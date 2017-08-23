package org.usfirst.frc.team3555.robot.Components;

public abstract class Updatable {
	protected boolean active;
	
	public abstract void update();
	public boolean isActive(){return active;}
}
