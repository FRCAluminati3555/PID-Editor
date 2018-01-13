package org.usfirst.frc.team3555.Editor.Components;

import org.usfirst.frc.team3555.Editor.Handler;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfo;

public abstract class Updatable {
	protected Handler handler;
	protected DeviceInfo device;
	
	protected boolean active;
	
	public Updatable(Handler handler, DeviceInfo device) {
		this.handler = handler;
		this.device = device;
		
		active = true;
	}
	
	public abstract void update();
	public boolean isActive() { return active; }
}
