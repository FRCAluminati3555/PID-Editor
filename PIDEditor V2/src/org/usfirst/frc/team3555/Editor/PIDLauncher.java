package org.usfirst.frc.team3555.Editor;

import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfoManager;
import org.usfirst.frc.team3555.Network.Client;

public class PIDLauncher {
	public static void main(String[] args) {
		Client client = null;
		DeviceInfoManager manager = new DeviceInfoManager(client);

		client = new Client(manager);
		client.start();
	}
}
