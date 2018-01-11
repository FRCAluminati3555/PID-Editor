package org.usfirst.frc.team3555.Robot;

import java.io.IOException;

import org.usfirst.frc.team3555.Util;
import org.usfirst.frc.team3555.Util.Controller;
import org.usfirst.frc.team3555.Util.Properties;
import org.usfirst.frc.team3555.Editor.DeviceInfo.DeviceInfoManager;
import org.usfirst.frc.team3555.Network.Client;
import org.usfirst.frc.team3555.Network.Server;
import org.usfirst.frc.team3555.Robot.Monitors.MonitorManager;

public class Test {
	public static void main(String[] args) throws IOException {
		
		MonitorManager manager = new MonitorManager();
		Server server = new Server(manager);
		server.start();
		
		DeviceInfoManager deviceManager = new DeviceInfoManager();
		Client client = new Client(deviceManager);
		deviceManager.setClient(client);
		client.start();
		
		int x = 0;
		while(x < 10) {
			System.out.println("-------------");
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			server.send(Util.genPacket(Controller.CANTalon, Properties.Current, 49, x++));
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			client.send(Util.genPacket(Controller.CANTalon, Properties.Current, 49, x++));
		}
		
		client.shutDown();
		server.shutDown();
	}
}
