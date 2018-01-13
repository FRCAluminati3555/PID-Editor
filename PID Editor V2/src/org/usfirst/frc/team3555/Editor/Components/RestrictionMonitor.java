package org.usfirst.frc.team3555.Editor.Components;

import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Editor.Handler;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfo;

public class RestrictionMonitor extends Updatable {
	private double[] bounds;
	
	private boolean monitoring;
	private boolean enteredBounds;
	
	public RestrictionMonitor(Handler handler, DeviceInfo device) {
		super(handler, device);
		
		bounds = new double[2];
	}
	
	@Override
	public void update() {
		if(monitoring && (boolean) device.getInfo(Properties.Enabled)) {
			double value = device.getValue();
			
			if(value >= bounds[0] && value <= bounds[1] && !enteredBounds)
				enteredBounds = true;
			else if(enteredBounds && (value < bounds[0] || value > bounds[1])) {
				enteredBounds = false;
				monitoring = false;
				handler.getDeviceInfoManager().sendData(device, Properties.Enabled, false);
			}
		}
	}
	
	public void end() {
		monitoring = false;
		enteredBounds = false;
	}

	public boolean isMonitoring() { return monitoring; }
	public void setMonitoring(boolean monitoring) { this.monitoring = monitoring; }
	
	public void setLowerBound(double limit) {
		this.bounds[0] = limit;
		enteredBounds = false;
	}
	
	public void setUpperBound(double limit) {
		this.bounds[1] = limit;
		enteredBounds = false;
	}
}
