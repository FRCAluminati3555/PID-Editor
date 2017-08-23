package org.usfirst.frc.team3555.robot;

import org.usfirst.frc.team3555.robot.Components.Updater;
import org.usfirst.frc.team3555.robot.Data.DeviceInfoManager;
import org.usfirst.frc.team3555.robot.Data.NetworkReader;

import javafx.scene.Scene;
import javafx.scene.control.Button;

public class Handler {
	private Updater updater;
	private NetworkReader reader;
	private DeviceInfoManager deviceInfoManager;
	
	public Handler() {
		updater = new Updater();
		deviceInfoManager = new DeviceInfoManager(this);
		reader = new NetworkReader(this);
	}
	
	public void update() {
		updater.update();
	}
	
	public Updater getUpdater(){return updater;}
	public NetworkReader getReader(){return reader;}
	public DeviceInfoManager getDeviceInfoManager(){return deviceInfoManager;}
}
