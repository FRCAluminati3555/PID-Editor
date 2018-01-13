package org.usfirst.frc.team3555.Editor;

import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfoManager;
import org.usfirst.frc.team3555.Network.Client;

import javafx.application.Application;

public class PIDLauncher {
	public static final DeviceInfoManager manager = new DeviceInfoManager();
	public static final Client client = new Client(manager);
	
	public static void main(String[] args) {
		manager.setClient(client);
		client.start();
		
		Application.launch(Display.class, args); 
	}
}
