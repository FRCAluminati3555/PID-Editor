package org.usfirst.frc.team3555.robot;

public class SquareWaveMonitor {
	private Handler handler;
	
	private long lastTime;
	private double frequency;
	
	private double[] setPoints;
	private int index;
	
	private boolean monitoring;
	
	public SquareWaveMonitor(Handler handler) {
		this.handler = handler;
	}
	
	public void update() {
		if(monitoring) {
			if(System.currentTimeMillis() > lastTime + frequency) {
				System.out.println("Change");
				
				index++;
				
				if(index >= setPoints.length)
					index = 0;
				
				handler.getReader().setSetPoint(setPoints[index]);
				
				lastTime = System.currentTimeMillis();
			}
		}
	}
	
	public void start() {
		double s1;
		
		try {
			s1 = Double.valueOf(handler.getSquareSetPoint1Field().getText());
		} catch(NumberFormatException ex) {
			return;
		}
		
		double s2;
		
		try {
			s2 = Double.valueOf(handler.getSquareSetPoint2Field().getText());
		} catch(NumberFormatException ex) {
			return;
		}
		
		double frequency;
		
		try {
			frequency = Double.valueOf(handler.getFrequencyField().getText());
		} catch(NumberFormatException ex) {
			return;
		}
		
		if(!handler.getDisplay().getGrapher().isGoing())
			handler.getEnableButton().fire();
//		handler.getSquareWaveMonitor().start(new double[]{s1, s2}, frequency);
		
		if(handler.getDisplay().getGrapher().isGoing()) {
			handler.getSquareWaveButton().setText("End Sqaure Wave");
			this.setPoints = new double[] {s1, s2};
			this.frequency = frequency;
			monitoring = true;
		}
	}
	
	public void end() {
		handler.getSquareWaveButton().setText("Start Sqaure Wave");
		monitoring = false;
	}

	public boolean isMonitoring(){return monitoring;}
}
