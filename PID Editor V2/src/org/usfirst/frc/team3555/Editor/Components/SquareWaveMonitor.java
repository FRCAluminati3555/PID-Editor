package org.usfirst.frc.team3555.Editor.Components;

import org.usfirst.frc.team3555.Editor.Handler;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfo;
import org.usfirst.frc.team3555.Util.Properties;

public class SquareWaveMonitor extends Updatable {
	private long lastTime;
	private double frequency;
	
	private double[] setPoints;
	private int index;
	
	private boolean monitoring;
	
	public SquareWaveMonitor(Handler handler, DeviceInfo device) {
		super(handler, device);
		
		setPoints = new double[2];
	}
	
	@Override
	public void update() {
		if(monitoring) {
			if(System.currentTimeMillis() > lastTime + frequency) {
				index++;
				
				if(index >= setPoints.length)
					index = 0;
				
				handler.getDeviceInfoManager().sendData(device, Properties.SetPoint, setPoints[index]);
				
				lastTime = System.currentTimeMillis();
			}
		}
	}
	
	public boolean start() {
		if(frequency == 0)
			return false;
		if(setPoints[0] == 0 && setPoints[1] == 0)
			return false;
		monitoring = true;
		return true;
	}
	
	public void end() {
		monitoring = false;
	}

	public boolean isMonitoring(){return monitoring;}
	public double[] getSetPoints(){return setPoints;}

	public void setFrequency(double frequency){this.frequency = frequency;}
}
