package org.usfirst.frc.team3555.Editor;

import org.usfirst.frc.team3555.Editor.Components.Updater;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfoManager;
import org.usfirst.frc.team3555.Network.Client;

public class Handler {
	private Updater updater;
	private DeviceInfoManager deviceInfoManager;
	private Client client;
	
	public Handler() {
		updater = new Updater();
		deviceInfoManager = PIDLauncher.manager;
		client = PIDLauncher.client;
	}
	
	public void update() {
		updater.update();
	}
	
	public Updater getUpdater() { return updater; }
	public DeviceInfoManager getDeviceInfoManager() { return deviceInfoManager; }
	public Client getClient() { return client;}
}
