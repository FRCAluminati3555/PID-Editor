package org.usfirst.frc.team3555.robot;

import javafx.animation.KeyFrame;

public class Handler {
	private Grapher grapher;
	private NetworkReader reader;
	
	public Handler(Grapher grapher, NetworkReader reader){
		this.grapher = grapher;
		this.reader = reader;
	}
	
	public void start(KeyFrame kf){
		grapher.start(kf);
	}
	
	public void sync(){
		grapher.update(reader.getValue(), reader.getSetPoint());
	}
}
