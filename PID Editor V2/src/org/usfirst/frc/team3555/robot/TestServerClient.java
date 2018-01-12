package org.usfirst.frc.team3555.robot;

import java.io.IOException;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfoManager;
import org.usfirst.frc.team3555.Network.Client;
import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.robot.Monitors.MonitorManager;

public class TestServerClient {
	
	/**
	 * Test the connectivity between the client and server all on the computer
	 */
	public static void main(String[] args) throws IOException {
		MonitorManager manager = new MonitorManager();
		Server server = new Server(manager);
		 
		server.start();
		
		DeviceInfoManager deviceManager = new DeviceInfoManager();
		Client client = new Client(deviceManager);
		deviceManager.setClient(client);
		client.start();
		
		client.send(Util.genPacket(Controller.CANTalon, Properties.SetPoint, 822, 0));
		
		try {
			Thread.sleep(2000); 
		} catch (InterruptedException e) { 
			e.printStackTrace();
		}
		
		client.shutDown();
		server.shutDown();
	}
}
