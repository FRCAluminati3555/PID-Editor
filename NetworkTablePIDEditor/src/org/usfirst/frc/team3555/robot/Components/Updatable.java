package org.usfirst.frc.team3555.robot.Components;

import org.usfirst.frc.team3555.robot.Handler;

public abstract class Updatable {
	protected Handler handler;
	protected int id;
	
	protected boolean active;
	
	public Updatable(Handler handler, int id) {
		this.handler = handler;
		this.id = id;
		
		active = true;
	}
	
	public abstract void update();
	public boolean isActive() { return active; }
}
