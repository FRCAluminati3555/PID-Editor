package org.usfirst.frc.team3555.robot.Components;

import org.usfirst.frc.team3555.robot.Handler;

public class RestrictionMonitor extends Updatable {
	private PIDEditor editor;
	
	private double[] bounds;
	
	private boolean monitoring;
	private boolean enteredBounds;
	
	public RestrictionMonitor(Handler handler, PIDEditor editor, int id) {
		super(handler, id);
		this.editor = editor;
		
		bounds = new double[2];
	}
	
	@Override
	public void update() {
		handler.getDeviceInfoManager().getDevices().get(id).getDoubles().put("LowerBound", bounds[0]);
		handler.getDeviceInfoManager().getDevices().get(id).getDoubles().put("UpperBound", bounds[1]);
		
		if(monitoring && handler.getDeviceInfoManager().getBoolean("Enabled", id)) {
			double value = handler.getDeviceInfoManager().getDevices().get(id).getValue();
			
			if(value >= bounds[0] && value <= bounds[1] && !enteredBounds)
				enteredBounds = true;
			else if(enteredBounds && (value < bounds[0] || value > bounds[1])) {
				enteredBounds = false;
				monitoring = false;
				editor.disableAll();  
			}
		}
	}
	
	public void end() {
		monitoring = false;
		enteredBounds = false;
	}

	public boolean isMonitoring(){return monitoring;}
	public void setMonitoring(boolean monitoring){this.monitoring = monitoring;}
	public boolean isEnteredBounds(){return enteredBounds;}
	public void setEnteredBounds(boolean enteredBounds){this.enteredBounds = enteredBounds;}
	public double[] getBounds(){return bounds;}
	public void setBounds(double[] bounds){this.bounds = bounds;}
}
